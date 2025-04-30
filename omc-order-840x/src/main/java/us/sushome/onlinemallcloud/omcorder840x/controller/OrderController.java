package us.sushome.onlinemallcloud.omcorder840x.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.RateLimiter;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import us.sushome.onlinemallcloud.omccommon.api.ResultVo;
import us.sushome.onlinemallcloud.omccommon.api.cache.vo.OrderKeyPrefix;
import us.sushome.onlinemallcloud.omccommon.api.cache.vo.SeckillKeyPrefix;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.SeckillGoodVo;
import us.sushome.onlinemallcloud.omccommon.api.mq.MqProviderServiceApi;
import us.sushome.onlinemallcloud.omccommon.api.order.vo.NewOrderVo;
import us.sushome.onlinemallcloud.omccommon.api.order.vo.OrderVo;
import us.sushome.onlinemallcloud.omccommon.api.user.vo.UserVo;
import us.sushome.onlinemallcloud.omccommon.cache.RedisService;
import us.sushome.onlinemallcloud.omccommon.result.CodeMsg;
import us.sushome.onlinemallcloud.omccommon.result.Result;
import us.sushome.onlinemallcloud.omccommon.utils.DesECBUtil;
import us.sushome.onlinemallcloud.omccommon.utils.StringUtils;
import us.sushome.onlinemallcloud.omcorder840x.component.SeckillStatusService;
import us.sushome.onlinemallcloud.omcorder840x.service.OmOrderMainService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static us.sushome.onlinemallcloud.omccommon.constants.OrderConstants.SECKILL_CLOSING;

@RestController
@RequestMapping("/openApi/order")
public class OrderController{
    private static Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    OmOrderMainService omOrderMainService;

    @Autowired
    RedisService redisService;

    @DubboReference(interfaceClass = MqProviderServiceApi.class)
    MqProviderServiceApi mqProviderServiceApi;

    @Autowired
    SeckillStatusService seckillStatusService;

    // 每秒发放10个令牌，可以按需调整
    private static final RateLimiter rateLimiter = RateLimiter.create(10.0);


    @SaCheckPermission("/openApi/order")
    @PostMapping("/getOrderById")
    public Result<OrderVo> getOrderById(@RequestBody String body){
        JSONObject jsonObject = JSONObject.parseObject(body);
        String id = jsonObject.getString("id");
        if(id == null){
            return Result.error(CodeMsg.PRAMS_ERROR);
        }else{
            return Result.success(omOrderMainService.getOrderById(id,null));
        }
    }

    //用于用户抢购订单后获取抢购状态
    @PostMapping("/getMyOrderById")
    public Result<OrderVo> getMyOrderById(@RequestBody String body){
        JSONObject jsonObject = JSONObject.parseObject(body);
        String id = jsonObject.getString("id");
        if(id == null){
            return Result.error(CodeMsg.PRAMS_ERROR);
        }else{
            UserVo userVo = (UserVo) StpUtil.getSession().get("userInfo");
            String userId = userVo.getUserId();
            return Result.success(omOrderMainService.getOrderById(id,userId));
        }
    }


    @PostMapping("/getMyOrderList")
    public Result<Map<String,Object>> getMyOrderList(@RequestBody String body){
        JSONObject jsonObject = JSONObject.parseObject(body);
        Integer status = jsonObject.getInteger("status");
        Integer type = jsonObject.getInteger("type");
        String goodName = jsonObject.getString("goodName");
        Integer pageNum = jsonObject.getInteger("pageNum");
        Integer pageSize = jsonObject.getInteger("pageSize");
        UserVo userVo = (UserVo) StpUtil.getSession().get("userInfo");
        String userId = userVo.getUserId();
        if(pageNum == null) {
            pageNum = 1;
        }
        if(pageSize == null) {
            pageSize = 10;
        }
        return Result.list(omOrderMainService.getOrderList(null,userId,status,type,null,null,goodName,pageNum,pageSize));
    }

    @SaCheckPermission("/openApi/order")
    @PostMapping("/getAllOrderList")
    public Result<Map<String,Object>> getAllOrderList(@RequestBody String body){
        JSONObject jsonObject = JSONObject.parseObject(body);
        String orderId = jsonObject.getString("orderId");
        String userId = jsonObject.getString("userId");
        Integer status = jsonObject.getInteger("status");
        Integer type = jsonObject.getInteger("type");
        String phone = jsonObject.getString("phone");
        String payDate = jsonObject.getString("payDate");
        String goodName = jsonObject.getString("goodName");
        Integer pageNum = jsonObject.getInteger("pageNum");
        Integer pageSize = jsonObject.getInteger("pageSize");
        if(pageNum == null) {
            pageNum = 1;
        }
        if(pageSize == null) {
            pageSize = 10;
        }
        return Result.list(omOrderMainService.getOrderList(orderId,userId,status,type,phone,payDate,goodName,pageNum,pageSize));
    }

    @PostMapping("/newOrder")
    public Result<NewOrderVo> newOrderByHttp(@RequestBody OrderVo orderVo){
        UserVo userVo = (UserVo) StpUtil.getSession().get("userInfo");
        String userId = userVo.getUserId();
        orderVo.setUserId(userId);
        //预创建订单 id
        orderVo.setOrderId(UUID.randomUUID().toString().replace("-",""));
        ResultVo<NewOrderVo> resultVo = omOrderMainService.newOrder(orderVo);
        return Result.resultByPrams(resultVo.getMessage(),resultVo.getData());
    }

    @SaCheckPermission("/openApi/order")
    @PostMapping("/newOrderByAdmin")
    public Result<NewOrderVo> newOrderByAdmin(@RequestBody OrderVo orderVo){
        //预创建订单 id
        orderVo.setOrderId(UUID.randomUUID().toString().replace("-",""));
        ResultVo<NewOrderVo> resultVo = omOrderMainService.newOrder(orderVo);
        return Result.resultByPrams(resultVo.getMessage(),resultVo.getData());
    }


    @SaCheckPermission("/openApi/order")
    @PostMapping("/initSeckillStatus")
    public Result<Map<String,Boolean>> initSeckillStatus(){
        return Result.success(seckillStatusService.initSeckillStatus());
    }

    //@AccessLimit(seconds = 5, maxAccessCount = 5,needLogin = true)
    @GetMapping("/getSeckillPath")
    public Result<String> getSeckillPath(@RequestParam("seckillGoodId") String seckillGoodId){
        UserVo userVo = (UserVo) StpUtil.getSession().get("userInfo");
        String userId = userVo.getUserId();
        if(StringUtils.isEmpty(seckillGoodId)){
            return Result.error(CodeMsg.PRAMS_ERROR);
        }
        if(StringUtils.isEmpty(userId)){
            return Result.error(CodeMsg.TOKEN_ERROR);
        }else{
            SeckillGoodVo seckillGoodVo = seckillStatusService.getSeckillGoodVo(seckillGoodId);
            if(seckillGoodVo == null){
                return Result.error(CodeMsg.GOODS_NOT_EXIST);
            }else{
                if(Objects.equals(seckillGoodVo.getStatus(), SECKILL_CLOSING)){
                    return Result.error(CodeMsg.SECKILL_NOT_OPEN);
                }
                LocalTime startTime = seckillGoodVo.getStartTime().toLocalTime();
                LocalTime endTime = seckillGoodVo.getEndTime().toLocalTime();
                LocalTime now = LocalTime.now();
                if(now.isBefore(startTime) || now.isAfter(endTime)){
                    return Result.error(CodeMsg.SECKILL_NOT_START);
                }
                String path;
                try{
                    path = DesECBUtil.encryptMD5(UUID.randomUUID().toString().replace("-",""));
                } catch (Exception e) {
                    return Result.error(CodeMsg.SERVER_ERROR);
                }
                redisService.set(SeckillKeyPrefix.SK_PATH,userId + "_" + seckillGoodId,path);
                return Result.success(path);
            }
        }
    }

    @PostMapping("{path}/newSeckillOrder")
    public Result<String> newSeckillOrder(@PathVariable String path,@RequestBody String body){
        // 限流判断：尝试获取令牌（等待最多 500ms）
        //Web 接口 / 秒杀抢购等 👉 用 tryAcquire()，不让用户请求一直阻塞；
        //后台任务 / 异步操作等 👉 用 acquire()，可以慢慢等没关系。
        if (!rateLimiter.tryAcquire(500, TimeUnit.MILLISECONDS)) {
            logger.warn("限流中，令牌获取失败");
            return Result.error(CodeMsg.ACCESS_LIMIT_REACHED);
        }
        UserVo userVo = (UserVo) StpUtil.getSession().get("userInfo");
        String userId = userVo.getUserId();
        JSONObject jsonObject = JSONObject.parseObject(body);
        String seckillGoodId = jsonObject.getString("seckillGoodId");
        OrderVo orderVo = jsonObject.getObject("order",OrderVo.class);
        if(StringUtils.isEmpty(userId)){
            return Result.error(CodeMsg.TOKEN_ERROR);
        }else if(StringUtils.isEmpty(seckillGoodId)){
            return Result.error(CodeMsg.PRAMS_ERROR);
        }else if(StringUtils.isEmpty(path)){
            return Result.error(CodeMsg.PRAMS_ERROR);
        }else{
            String generatedPath = redisService.get(SeckillKeyPrefix.SK_PATH,userId + "_" + seckillGoodId,String.class);
            if(StringUtils.isEmpty(generatedPath)){
                return Result.error(CodeMsg.REQUEST_ILLEGAL);
            } else if(!generatedPath.equals(path)){
                return Result.error(CodeMsg.ACCESS_LIMIT_REACHED);
            }else{
                if(seckillStatusService.isSeckillGoodOver(seckillGoodId)){
                    return Result.error(CodeMsg.SECKILL_OVER);
                }else{
                    LocalDate today = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String dateStr = today.format(formatter);// 生成日期字符串
                    OrderVo order = redisService.get(OrderKeyPrefix.ORDER,userId+"_"+ dateStr+"_"+seckillGoodId,OrderVo.class);
                    if(order == null){
                        //当天为购买 继续通过数据库寻找
                        order = omOrderMainService.getTodayOrderByUserOrder(seckillGoodId,userId);
                    }
                    if(order != null){
                        return Result.error(CodeMsg.REPEATE_SECKILL);
                    }
                    //预减库存
                    long stock = redisService.decrement(SeckillKeyPrefix.SK_GOOD_STOCK,seckillGoodId);
                    if(stock < 0){
                        seckillStatusService.setSeckillGoodOver(seckillGoodId);
                        return Result.error(CodeMsg.SECKILL_OVER);
                    }
                    orderVo.setUserId(userId);
                    //预创建订单 id
                    String orderId = UUID.randomUUID().toString().replace("-","");
                    orderVo.setOrderId(orderId);
                    mqProviderServiceApi.sendOrderMessage(orderVo);
                    return Result.success(orderId);
                }
            }
        }
    }

    @PostMapping("/deleteOrder")
    public Result<Boolean> deleteOrder(@RequestBody String body){
        JSONObject jsonObject = JSONObject.parseObject(body);
        String id = jsonObject.getString("id");
        if(id == null){
            return Result.error(CodeMsg.PRAMS_ERROR);
        }else{
            return Result.success(omOrderMainService.deleteOrder(id));
        }
    }

    @SaCheckPermission("/openApi/order")
    @PostMapping("/updateOrderInfo")
    public Result<CodeMsg> updateOrderInfoByHttp(@RequestBody OrderVo orderVo){
        CodeMsg codeMsg = omOrderMainService.updateOrderInfo(orderVo);
        return Result.info(codeMsg);
    }

    //@PostMapping("/getMqttToken")
    //public Result<String> getMqttToken(){
    //    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //    UserVo userVo = (UserVo) authentication.getPrincipal();
    //    String userId = userVo.getUserId();
    //    return Result.success(JwtUtils.generateMqttTokenByUserId(userId));
    //}
}
