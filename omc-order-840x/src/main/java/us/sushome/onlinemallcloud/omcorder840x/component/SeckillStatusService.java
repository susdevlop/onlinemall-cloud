package us.sushome.onlinemallcloud.omcorder840x.component;

import jakarta.annotation.PostConstruct;
import org.apache.dubbo.config.annotation.DubboReference;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.sushome.onlinemallcloud.omccommon.cache.RedisService;
import us.sushome.onlinemallcloud.omccommon.api.cache.RedisServiceApi;
import us.sushome.onlinemallcloud.omccommon.api.cache.vo.SeckillKeyPrefix;
import us.sushome.onlinemallcloud.omccommon.api.goods.GoodsServiceApi;
import us.sushome.onlinemallcloud.omccommon.api.goods.vo.SeckillGoodVo;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SeckillStatusService {
    //自认为这个地方写的乱七八糟

    @DubboReference(interfaceClass = GoodsServiceApi.class)
    private GoodsServiceApi goodsServiceApi;

    @Autowired
    RedisService redisService;

    private final Map<String,Boolean> localOverMap = new ConcurrentHashMap<>();//用于内存标记，标记库存是否为空，从而减少对redis的访问

    private final Map<String,SeckillGoodVo> seckillIdToSeckillGoodVo = new ConcurrentHashMap<>();

    public SeckillGoodVo getSeckillGoodVo(String id) {
        SeckillGoodVo seckillGoodVo = seckillIdToSeckillGoodVo.get(id);
        if (seckillGoodVo == null) {
            seckillGoodVo = goodsServiceApi.getSeckillGoodInfoById(id);
            seckillIdToSeckillGoodVo.put(id, seckillGoodVo);
            return seckillGoodVo;
        }else{
            return seckillIdToSeckillGoodVo.get(id);
        }
    }

    @PostConstruct
    public void afterPropertiesSet(){
        initSeckillStatus();
    }

    public Boolean isSeckillGoodOver(String id) {
        return localOverMap.getOrDefault(id, false);
    }

    public void setSeckillGoodOver(String id) {
        localOverMap.put(id, true);
    }



    // 新增：存储秒杀商品的时间范围（Key: 商品ID, Value: Map{"startTime": LocalTime, "endTime": LocalTime}）
    private final Map<String, Map<String, LocalTime>> seckillTimeMap = new ConcurrentHashMap<>();

    private void updateSeckillTimeMap(List<SeckillGoodVo> seckillGoodsList){
        seckillTimeMap.clear();
        //更新秒杀时间表
        for (SeckillGoodVo good : seckillGoodsList) {
            if (good.getStatus() == 1 || good.getStatus() == 2) {
                Map<String, LocalTime> timeRange = new HashMap<>();
                timeRange.put("startTime", good.getStartTime().toLocalTime());
                timeRange.put("endTime", good.getEndTime().toLocalTime());
                seckillTimeMap.put(good.getId(), timeRange);
            }
        }
    }

    // 新增方法：检查当前是否有活动中的秒杀
    private boolean hasActiveSeckill() {
        LocalTime now = LocalTime.now();
        return seckillTimeMap.values().stream()
            .anyMatch(timeRange -> {
                LocalTime start = timeRange.get("startTime");
                LocalTime end = timeRange.get("endTime");
                return now.isAfter(start) && now.isBefore(end);
            });
    }

    @Scheduled(cron = "0 0 0 * * ?") // 每天 0 点执行
    public Map<String,Boolean> initSeckillStatus(){
        try{
            System.out.println("初始化商品库存和秒杀开始时间等状态");
            List<SeckillGoodVo> seckillGoodsList = goodsServiceApi.getOpeningSeckillGoodList();
            localOverMap.clear();
            updateSeckillTimeMap(seckillGoodsList);
            for(SeckillGoodVo seckillGoodVo : seckillGoodsList) {
                if(seckillGoodVo.getStatus() == 1 || seckillGoodVo.getStatus() == 2){
                    //LocalTime startTime = seckillGoodVo.getStartTime().toLocalTime();
                    //LocalTime endTime = seckillGoodVo.getEndTime().toLocalTime();
                    //LocalTime now = LocalTime.now();
                    //boolean unStart = now.isBefore(startTime) || now.isAfter(endTime);
                    //if(unStart){
                    //    seckillIdToSeckillGoodVo.put(seckillGoodVo.getId(), seckillGoodVo);
                    //    Integer stock = seckillGoodVo.getStock();
                    //    redisServiceApi.set(SeckillKeyPrefix.SK_GOOD_STOCK,seckillGoodVo.getId(),stock);
                    //    System.out.println("stock"+stock+"\n");
                    //    System.out.println("stock == 0"+ (stock == 0));
                    //    localOverMap.put(seckillGoodVo.getId(), stock == 0);
                    //}//已开启的秒杀活动无法重新初始化
                    seckillIdToSeckillGoodVo.put(seckillGoodVo.getId(), seckillGoodVo);
                    Integer stock = seckillGoodVo.getStock();
                    redisService.set(SeckillKeyPrefix.SK_GOOD_STOCK,seckillGoodVo.getId(),stock);
                    System.out.println("stock"+stock+"\n");
                    System.out.println("stock == 0"+ (stock == 0));
                    localOverMap.put(seckillGoodVo.getId(), stock == 0);
                }
            }
            System.out.println("初始化商品库存和秒杀开始时间等状态"+"\n"+localOverMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return localOverMap;
    }
    //假设 6 点开启秒杀活动，现在 5 点，秒杀活动 现在未开始，定时任务会不执行，如果修改时间为 5 点，
    // 就执行initSeckillStatus，initSeckillStatus会判断是否正在秒杀，如果不在秒杀，就会重新更新秒杀时间和状态等信息
    //更新时间如果变成当前是秒杀时间，下面的定时任务就会继续执行

    @Scheduled(fixedRate = 30000)
    public void syncStockStatusFromRedis() {
        try {
            if(!hasActiveSeckill()){
                //当前没有秒杀活动，不更新状态。如果要更改秒杀活动时间，应通过接口去执行initSeckillStatus更新
                 return;
            }
            List<SeckillGoodVo> seckillGoodsList = goodsServiceApi.getOpeningSeckillGoodList();
            updateSeckillTimeMap(seckillGoodsList);
            for (SeckillGoodVo seckillGoodVo : seckillGoodsList) {
                seckillIdToSeckillGoodVo.put(seckillGoodVo.getId(), seckillGoodVo);
                //System.out.println("更新抢购商品信息:"+seckillGoodVo.getId()+" "+seckillGoodVo.getName()+" "+seckillGoodVo.getStock() +seckillGoodVo.getStartTime()+" "+seckillGoodVo.getEndTime());
                if(seckillGoodVo.getStatus() == 1 || seckillGoodVo.getStatus() == 2){
                    String id = seckillGoodVo.getId();
                    if(!isSeckillGoodOver(id)){
                        System.out.println("秒杀未结束，更新实时库存");
                        //秒杀已开启，且秒杀未结束，从 redis 中读取库存
                        Integer inventory = redisService.get(SeckillKeyPrefix.SK_GOOD_STOCK, id, Integer.class);
                        System.out.println(seckillGoodVo.getName()+"库存"+inventory);
                        if (inventory != null && inventory > 0) {
                            localOverMap.put(id, false); // 库存 > 0，标记为未售罄
                        }else{
                            localOverMap.put(id,true);
                        }
                    }
                }
            }
            System.out.println("定时更新秒杀商品"+localOverMap);
        } catch (Exception e) {
            // 打印错误日志，但不中断定时任务
            System.err.println("定时更新秒杀商品状态失败: " + e.getMessage());
        }
    }
}