package us.sushome.onlinemallcloud.omcorder840x.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import us.sushome.onlinemallcloud.omccommon.api.ResultVo;
import us.sushome.onlinemallcloud.omccommon.api.cache.vo.OrderKeyPrefix;
import us.sushome.onlinemallcloud.omccommon.api.cache.vo.SeckillKeyPrefix;
import us.sushome.onlinemallcloud.omccommon.api.goods.GoodsServiceApi;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.GoodInfoVo;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.GoodSkuVo;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.SeckillGoodVo;
import us.sushome.onlinemallcloud.omccommon.api.mq.MqProviderServiceApi;
import us.sushome.onlinemallcloud.omccommon.api.order.vo.NewOrderVo;
import us.sushome.onlinemallcloud.omccommon.api.order.vo.OrderDetailVo;
import us.sushome.onlinemallcloud.omccommon.api.order.vo.OrderVo;
import us.sushome.onlinemallcloud.omccommon.api.user.UserServiceApi;
import us.sushome.onlinemallcloud.omccommon.api.user.vo.UserVo;
import us.sushome.onlinemallcloud.omccommon.cache.RedisLockService;
import us.sushome.onlinemallcloud.omccommon.cache.RedisService;
import us.sushome.onlinemallcloud.omccommon.result.CodeMsg;
import us.sushome.onlinemallcloud.omccommon.utils.StringUtils;
import us.sushome.onlinemallcloud.omcorder840x.domain.OmOrderInfo;
import us.sushome.onlinemallcloud.omcorder840x.model.OmOrder;
import us.sushome.onlinemallcloud.omcorder840x.model.OmOrderdetail;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static us.sushome.onlinemallcloud.omccommon.constants.OrderConstants.*;

@Service
public class OmOrderMainService{
    private static Logger logger = LoggerFactory.getLogger(OmOrderMainService.class);

    @Resource
    private IOmOrderService omOrderService;

    @Resource
    private IOmOrderdetailService omOrderdetailService;

    @Resource
    private IOmCouponService omCouponService;

    @Resource
    private GoodsServiceApi goodsServiceApi;

    @Resource
    private UserServiceApi userServiceApi;

    //@Autowired
    //RedisLockService redisLockService;

    @Autowired
    RedisService redisService;

    @DubboReference(interfaceClass = MqProviderServiceApi.class)
    MqProviderServiceApi mqProviderServiceApi;



    public OrderVo getOrderById(String orderId,String userId) {
        LambdaQueryWrapper<OmOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OmOrder::getOmOrderId, orderId)
                .eq(Objects.nonNull(userId), OmOrder::getOmOrderUserid, userId);
        OmOrder order = omOrderService.getOne(queryWrapper);
        if (order == null) {
            return null;
        }
        OrderVo orderVo = omOrderService.convertOmOrderToOrderVo(order);
        List<OmOrderdetail> orderDetails = omOrderdetailService.list(new QueryWrapper<OmOrderdetail>().eq("om_orderdetail_orderid", orderId));
        List<OrderDetailVo> detailVoList = new ArrayList<>();
        orderDetails.forEach(omOrderdetail -> {
            OrderDetailVo detailVo = omOrderdetailService.convertOmOrderDetailToOrderDetailVo(omOrderdetail);
            GoodInfoVo goodInfoVo = goodsServiceApi.getGoodInfoBySkuId(detailVo.getSkuId());
            detailVo.setGoodInfo(goodInfoVo);
            detailVoList.add(detailVo);
        });
        orderVo.setOrderDetails(detailVoList);
        return orderVo;
    }


    public IPage<OrderVo> getOrderList(String orderId, String userId, Integer status, Integer type,
                                       String phone, String payDateStr, String goodName,
                                       Integer pageNum, Integer pageSize) {
        // 1. 解析日期参数（保持不变）
        LocalDate payDate = null;
        if(!StringUtils.isBlank(payDateStr) && !StringUtils.trim(payDateStr).isEmpty()){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            payDate = LocalDate.parse(payDateStr, formatter);
        }

        // 2. 获取分页数据（SQL中已包含ORDER BY om_order_addtime DESC）
        IPage<OmOrderInfo> orderInfoIPage = omOrderService.getOrderInfoList(
                orderId, userId, status, type, phone, payDate, goodName, pageNum, pageSize);

        // 3. 按原始顺序处理记录（重要修改）
        List<OmOrderInfo> orderInfoList = orderInfoIPage.getRecords();

        // 使用LinkedHashMap保持插入顺序
        Map<String, OrderVo> orderIdToOrderVoMap = new LinkedHashMap<>();
        Map<String, GoodInfoVo> skuIdToGoodInfoVoMap = new HashMap<>();

        // 4. 按查询结果的原始顺序处理
        for (OmOrderInfo omOrderInfo : orderInfoList) {
            String currentOrderId = omOrderInfo.getOmOrderId();
            OrderVo orderVo = orderIdToOrderVoMap.computeIfAbsent(currentOrderId, id -> {
                OrderVo vo = omOrderService.convertOmOrderToOrderVo(omOrderInfo);
                vo.setOrderDetails(new ArrayList<>());
                return vo;
            });

            OrderDetailVo orderDetailVo = omOrderdetailService.omOrderInfoToOrderDetailVo(omOrderInfo);
            String skuId = orderDetailVo.getSkuId();

            // 缓存商品信息
            GoodInfoVo goodInfoVo = skuIdToGoodInfoVoMap.computeIfAbsent(skuId,
                    id -> goodsServiceApi.getGoodInfoBySkuId(skuId));
            orderDetailVo.setGoodInfo(goodInfoVo);

            orderVo.getOrderDetails().add(orderDetailVo);
        }

        // 5. 创建分页结果（保持原始顺序）
        IPage<OrderVo> orderVoIPage = new Page<>();
        orderVoIPage.setRecords(new ArrayList<>(orderIdToOrderVoMap.values())); // LinkedHashMap.values()保持顺序
        orderVoIPage.setTotal(orderInfoIPage.getTotal());
        orderVoIPage.setSize(orderInfoIPage.getSize());
        orderVoIPage.setCurrent(orderInfoIPage.getCurrent());
        orderVoIPage.setPages(orderInfoIPage.getPages());

        return orderVoIPage;
    }




    //根据付款方式返回交易凭证
    public ResultVo<NewOrderVo> payOrder(String orderId, Integer payWay, BigDecimal payAmount,String userId) {
        ResultVo<NewOrderVo> resultVo = new ResultVo<NewOrderVo>();
        if(Objects.equals(payWay, ALIPAY)){
            resultVo.setMessage(CodeMsg.ORDER_PAYWAY_NOT_OPEN);
            NewOrderVo newOrderVo = new NewOrderVo();
            Map<String,Object> payInfo = new HashMap<>();
            newOrderVo.setPayInfo(payInfo);
            return resultVo;
        }else if(Objects.equals(payWay, WECHAT)){
            resultVo.setMessage(CodeMsg.ORDER_PAYWAY_NOT_OPEN);
            NewOrderVo newOrderVo = new NewOrderVo();
            Map<String,Object> payInfo = new HashMap<>();
            newOrderVo.setPayInfo(payInfo);
            return resultVo;
        }else{
            resultVo.setMessage(CodeMsg.ORDER_PAYWAY_UNKNOW);
            return resultVo;
        }
    }
    //需要的数据：
    /*
    * 手机号、收货地址、收货名
    * 如果是普通订单： orderDetails: [{
                    skuId: selectedSku.id,
                    count: selectedGoodCount
                }],
    * 秒杀订单：
    * orderDetails: [{
                    seckillId: xxx,
                }],
    */
    @Transactional
    public ResultVo<NewOrderVo> newOrder(OrderVo order) {
        OmOrder omOrder = omOrderService.convertOrderVoToOmOrder(order);
        String orderId = omOrder.getOmOrderId();
        ResultVo<NewOrderVo> resultVo = new ResultVo<NewOrderVo>();
        if(StringUtils.isVoid(omOrder.getOmOrderPhone())){
            resultVo.setMessage(CodeMsg.ORDER_PHONE_IS_NULL);
            return resultVo;
        }
        if(StringUtils.isVoid(omOrder.getOmOrderAddress())){
            resultVo.setMessage(CodeMsg.ORDER_ADDRESS_IS_NULL);
            return resultVo;
        }
        if(StringUtils.isVoid(omOrder.getOmOrderConsignee())){
            resultVo.setMessage(CodeMsg.ORDER_CONSIGNEE_IS_NULL);
            return resultVo;
        }
        String userId = omOrder.getOmOrderUserid();
        UserVo userVo = userServiceApi.getUserInfo(userId);
        Integer orderType = omOrder.getOmOrderType();
        if(Objects.equals(orderType, GENERAL_ORDER) || Objects.equals(orderType, SECKILL_ORDER)) {//处理普通订单
            BigDecimal payAmount = BigDecimal.valueOf(0);
            //处理orderDetails
            List<OmOrderdetail> legitimateOrderDetailList = new ArrayList<>();
            List<OmOrderdetail> omOrderDetailList = omOrderdetailService.convertOrderDetailVoListToOmOrderDetailList(order.getOrderDetails());
            HashMap<String,GoodInfoVo> skuToGoodInfoVoMap = new HashMap<>();
            if(Objects.equals(orderType, GENERAL_ORDER)){
                for (int i = 0; i < omOrderDetailList.size(); i++) {
                    OmOrderdetail omOrderdetail = omOrderDetailList.get(i);
                    String skuId = omOrderdetail.getOmOrderdetailSkuid();
                    Integer count = omOrderdetail.getOmOrderdetailCount();
                    if(!StringUtils.isVoid(skuId) && count != null){
                        if(skuToGoodInfoVoMap.containsKey(skuId)){
                            //已存在相同的 sku，忽略当前 item
                            continue;
                        }else{
                            GoodInfoVo goodInfoVo = goodsServiceApi.getGoodInfoBySkuId(skuId);
                            skuToGoodInfoVoMap.put(skuId,goodInfoVo);
                            GoodSkuVo skuInfo = goodInfoVo.getSkuList().get(0);
                            omOrderdetail.setOmOrderdetailId(UUID.randomUUID().toString().replace("-",""));
                            omOrderdetail.setOmOrderdetailOrderid(orderId);
                            omOrderdetail.setOmOrderdetailGoodname(goodInfoVo.getName());
                            BigDecimal uniPrice = skuInfo.getPrice();
                            BigDecimal sumPrice = uniPrice.multiply(BigDecimal.valueOf(count));
                            omOrderdetail.setOmOrderdetailUnitprice(uniPrice);
                            omOrderdetail.setOmOrderdetailSumprice(sumPrice);
                            payAmount = payAmount.add(sumPrice);
                            omOrderdetail.setOmOrderdetailAddtime(LocalDateTime.now());
                            omOrderdetail.setOmOrderdetailUpdatetime(LocalDateTime.now());
                            legitimateOrderDetailList.add(omOrderdetail);
                        }
                    }
                }

            }
            if(Objects.equals(orderType, SECKILL_ORDER)){
                if(omOrderDetailList.size() == 1){
                    OmOrderdetail omOrderdetail = omOrderDetailList.get(0);
                    String seckillGoodId = omOrderdetail.getOmOrderdetailSeckillid();
                    if(!StringUtils.isVoid(seckillGoodId)){
                        omOrderdetail.setOmOrderdetailId(UUID.randomUUID().toString().replace("-",""));
                        omOrderdetail.setOmOrderdetailOrderid(orderId);
                        SeckillGoodVo seckillGoodVo = goodsServiceApi.getSeckillGoodInfoById(seckillGoodId);
                        BigDecimal uniPrice = seckillGoodVo.getPrice();
                        payAmount = uniPrice;

                        omOrderdetail.setOmOrderdetailCount(1);
                        omOrderdetail.setOmOrderdetailSkuid(seckillGoodVo.getGoodSkuId());
                        omOrderdetail.setOmOrderdetailGoodname(seckillGoodVo.getName());
                        omOrderdetail.setOmOrderdetailUnitprice(uniPrice);
                        omOrderdetail.setOmOrderdetailSumprice(uniPrice);
                        omOrderdetail.setOmOrderdetailAddtime(LocalDateTime.now());
                        omOrderdetail.setOmOrderdetailUpdatetime(LocalDateTime.now());
                        legitimateOrderDetailList.add(omOrderdetail);
                    }
                }
            }
            if(legitimateOrderDetailList.isEmpty()){
                resultVo.setMessage(CodeMsg.ORDER_ILLEGALITY_ORDER);
                return resultVo;
            }
            omOrder.setOmOrderSum(payAmount);
            omOrder.setOmOrderStatus(NEW_UNPAID);
            omOrder.setOmOrderDate(LocalDate.now());
            omOrder.setOmOrderAddtime(LocalDateTime.now());
            omOrder.setOmOrderUpdatetime(LocalDateTime.now());
            if(omOrder.getOmOrderCouponid() != null){
                //暂不允许使用卡券
                omOrder.setOmOrderCouponid(null);
            }
            boolean stockIsEnough = true;//库存充足且减扣库存成功
            //减库存后下单 无论什么订单，除了库存不足都能够创建订单，只不过订单时支付或未支付，
            // 减库存成功后将根据 stockIsEnough 来决定是否创建订单
            if(Objects.equals(orderType, GENERAL_ORDER)){
                //遍历所有 sku 是否库存足够，都足够就加入 actionList
                List<Map<String,Object>> decrActionList = new ArrayList<>();
                //组装库存扣减数据 组装成功stockIsEnough为 true
                for (OmOrderdetail orderdetail : legitimateOrderDetailList) {
                    if (stockIsEnough) {
                        String skuId = orderdetail.getOmOrderdetailSkuid();
                        Integer count = orderdetail.getOmOrderdetailCount();
                        try {
                            /*
                             * 普通订单:
                             * 如果库存不足，就返回库存不足，订单不创建
                             * 如果库存足够，就减真实库存,然后创建订单
                             * 就判定支付方式，如果是余额支付并且支付成功
                             * 普通商品不使用 redis缓存库存
                             */
                            Integer skuStock = goodsServiceApi.getSkuInventoryById(skuId);
                            if (skuStock != null && skuStock >= count) {
                                Map<String, Object> decrAction = new HashMap<>();
                                decrAction.put("skuId", skuId);
                                decrAction.put("count", count);
                                decrActionList.add(decrAction);
                            } else {
                                stockIsEnough = false;
                                resultVo.setMessage(CodeMsg.GOODS_STOCK_NOT_ENOUGH);
                                break;
                            }
                        } catch (Exception e) {
                            stockIsEnough = false;
                            resultVo.setMessage(CodeMsg.SERVER_ERROR);
                        }
                    } else {
                        //某一件商品库存不足，直接跳出循环
                        break;
                    }
                }
                try{
                    //一次性减扣 sku 库存
                    if(stockIsEnough){
                        //扣减库存 减库存其中如果失败，就回滚
                        boolean result = goodsServiceApi.decrementGoodSkuInventoryByDecrActionList(decrActionList);
                        if(!result){
                            stockIsEnough = false;
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                            resultVo.setMessage(CodeMsg.GOODS_STOCK_NOT_ENOUGH);
                        }
                    }
                } catch (Exception e) {
                    stockIsEnough = false;
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    resultVo.setMessage(CodeMsg.SERVER_ERROR);
                }
            }
            if(Objects.equals(orderType, SECKILL_ORDER)){
                /*
                 * 秒杀订单：
                 * 直接减真实库存，如果因为乐观锁减库存失败，回补 redis 库存
                 */
                OmOrderdetail omOrderdetail = legitimateOrderDetailList.get(0);
                String seckillId = omOrderdetail.getOmOrderdetailSeckillid();

                Integer skuStock = goodsServiceApi.getSeckillGoodInventoryById(seckillId);
                if(skuStock != null){
                    if(skuStock >= 1){
                        Boolean result = goodsServiceApi.decrementSeckillGoodInventory(seckillId);
                        if(!result){
                            stockIsEnough = false;
                            resultVo.setMessage(CodeMsg.GOODS_STOCK_NOT_ENOUGH);
                            //并发减库存失败，回补 redis 库存
                            redisService.increment(SeckillKeyPrefix.SK_GOOD_STOCK,seckillId);
                        }
                    }
                }else{
                    stockIsEnough = false;
                }
            }

            if(stockIsEnough){
                //库存充足且减扣库存成功
                Integer payWay = omOrder.getOmOrderPayway();
                BigDecimal userBalance = userVo.getBalance();//用户余额
                if(Objects.equals(payWay, ALIPAY) || Objects.equals(payWay, WECHAT)){
                    //支付宝支付或微信支付
                    if(Objects.equals(orderType, GENERAL_ORDER)){
                        //普通订单直接生成支付凭证
                        resultVo = payOrder(omOrder.getOmOrderId(),payWay,payAmount,userId);
                    }else{
                        //秒杀订单需获取订单生成成功后再发起新的支付请求
                        resultVo.setMessage(CodeMsg.ORDER_PAY_AFTER_NEW_ORDER);
                    }
                }else if(Objects.equals(payWay, BALANCE)){
                    if(payAmount.compareTo(userVo.getBalance())>0){
                        //余额不足
                        resultVo.setMessage(CodeMsg.ORDER_BALANCE_NOT_ENOUGH);
                    }else{//余额支付减用户余额并把订单状态改为已支付
                        BigDecimal newBalance = userBalance.subtract(payAmount);
                        userVo.setBalance(newBalance);
                        userServiceApi.updateUserInfo(userVo);
                        omOrder.setOmOrderStatus(UN_SHIP);
                        omOrder.setOmOrderPaydate(LocalDateTime.now());
                        resultVo.setMessage(CodeMsg.ORDER_PAY_SUCCESS);//将当前订单返回信息设置为支付成功
                    }
                }

                //新建订单，订单创建后只能通过resultVo的 data 去覆盖订单信息
                boolean newOrderResult = omOrderService.save(omOrder);
                boolean newOrderDetailResult = omOrderdetailService.saveBatch(legitimateOrderDetailList);
                OrderVo orderVo = omOrderService.convertOmOrderToOrderVo(omOrder);//转换成 vo
                orderVo.setOrderDetails(omOrderdetailService.convertOmOrderDetailListToOrderDetailVoList(legitimateOrderDetailList));
                if(newOrderResult && newOrderDetailResult){
                    //订单创建成功
                    if(Objects.equals(orderType, SECKILL_ORDER)){
                        //如果是秒杀订单，需要把订单信息缓存到 redis 中
                        OmOrderdetail omOrderdetail = legitimateOrderDetailList.get(0);
                        String seckillId = omOrderdetail.getOmOrderdetailSeckillid();
                        LocalDate today = LocalDate.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String dateStr = today.format(formatter);// 生成日期字符串
                        redisService.set(OrderKeyPrefix.ORDER,userId+"_"+ dateStr+"_"+seckillId,orderVo);
                    }

                    if(Objects.equals(omOrder.getOmOrderStatus(), NEW_UNPAID)){
                        //订单创建成功，但是未支付，需要生成延时队列
                        //开发测试使用 2 分钟时间 ，生产环境使用 30 分钟时间 60*30
                        mqProviderServiceApi.sendDelayOrderMessage(orderVo,60*2);
                    }
                    NewOrderVo newOrderVo = resultVo.getData();
                    if(newOrderVo == null){
                        newOrderVo = new NewOrderVo();
                    }
                    newOrderVo.setOrderInfo(orderVo);
                    resultVo.setData(newOrderVo);
                }else{
                    //新建订单失败，需要回补库存，因为当前是库存充足且减扣库存成功的状态,回补普通商品或秒杀商品库存
                    resultVo.setMessage(CodeMsg.ORDER_CREATE_FAILED);
                    if(Objects.equals(payWay, BALANCE)){
                        userVo.setBalance(userBalance);
                        userServiceApi.updateUserInfo(userVo);
                    }
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    orderVo.setOrderId(UN_CREATE_ORDER);
                    mqProviderServiceApi.sendDelayOrderMessage(orderVo,0);//传 0 默认为 1 秒
                }
            }
            //如果库存不足，那么已经写好resultVo.setMessage，直接可以返回resultVo
        }else{
            resultVo.setMessage(CodeMsg.ORDER_ILLEGALITY_ORDER);
        }
        return resultVo;
    }

    public CodeMsg updateOrderInfo(OrderVo order) {
        OmOrder omOrder = omOrderService.convertOrderVoToOmOrder(order);
        String orderSn = omOrder.getOmOrderSn();
        String orderExpressName = omOrder.getOmOrderExpress();
        if(!StringUtils.isVoid(orderSn) && !StringUtils.isVoid(orderExpressName)){
            omOrder.setOmOrderStatus(SHIPPED);
            omOrder.setOmOrderUpdatetime(LocalDateTime.now());
        }
        Boolean result = omOrderService.updateById(omOrder);
        if(result){
            return CodeMsg.SUCCESS;
        }else{
            return CodeMsg.ORDER_UPDATE_FAILED;
        }
    }

    public OrderVo getTodayOrderByUserOrder(String seckillId, String userId) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = today.format(formatter);// 生成日期字符串
        OmOrderInfo omOrderInfo = omOrderService.getTodayOrderByUserOrder(dateStr,seckillId,userId);
        return omOrderService.convertOmOrderToOrderVo(omOrderInfo);
    }

    public Boolean deleteOrder(String id) {
        return omOrderService.removeById(id);
    }
}
