package us.sushome.onlinemallcloud.omccommon.cache;

import com.alibaba.fastjson2.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import us.sushome.onlinemallcloud.omccommon.api.cache.RedisServiceApi;
import us.sushome.onlinemallcloud.omccommon.api.cache.vo.KeyPrefix;
import us.sushome.onlinemallcloud.omccommon.utils.StringUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService implements RedisServiceApi {
    private static Logger logger = LoggerFactory.getLogger(RedisService.class);

    private final StringRedisTemplate redisTemplate;

    @Autowired  // 使用构造器注入替代@Resource
    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
        // 生成真正的存储于redis中的key
        String realKey = prefix.getPrefix() + key;
        // 通过key获取存储于redis中的对象（这个对象是以json格式存储的，所以是字符串）
        String strValue = redisTemplate.opsForValue().get(realKey);
        // 将json字符串转换为对应的对象
        return stringToBean(strValue, clazz);
    }

    @Override
    public <T> boolean set(KeyPrefix prefix, String key, T value) {
        // 将对象转换为json字符串
        String strValue = beanToString(value);
        if(StringUtils.isVoid(strValue)){
            return false;
        }
        // 生成实际存储于redis中的key
        String realKey = prefix.getPrefix() + key;
        // 获取key的过期时间
        int expires = prefix.expireSeconds();
        if(expires <= 0){
            // 设置key的过期时间为redis默认值（由redis的缓存策略控制）
            redisTemplate.opsForValue().set(realKey, strValue);
        }else {
            redisTemplate.opsForValue().set(realKey,strValue,expires, TimeUnit.SECONDS);
        }
        return true;
    }

    @Override
    public boolean exists(KeyPrefix keyPrefix, String key) {
        String realKey = keyPrefix.getPrefix() + key;
        Boolean exists = redisTemplate.hasKey(realKey);
        return exists != null && exists;
    }

    @Override
    public long increment(KeyPrefix keyPrefix, String key) {
        String realKey = keyPrefix.getPrefix() + key;
        Long increment = redisTemplate.opsForValue().increment(realKey);
        return increment != null ? increment : -100;
    }

    @Override
    public long increment(KeyPrefix keyPrefix, String key, long delta) {
        String realKey = keyPrefix.getPrefix() + key;
        Long increment = redisTemplate.opsForValue().increment(realKey, delta);
        return increment != null ? increment : -100;
    }

    @Override
    public long decrement(KeyPrefix keyPrefix, String key) {
        String realKey = keyPrefix.getPrefix() + key;
        Long decrement = redisTemplate.opsForValue().decrement(realKey);
        return decrement != null ? decrement : -100;
    }

    @Override
    public long decrement(KeyPrefix keyPrefix, String key, long delta) {
        String realKey = keyPrefix.getPrefix() + key;
        Long decrement = redisTemplate.opsForValue().decrement(realKey, delta);
        return decrement!= null? decrement : -100;
    }

    @Override
    public boolean delete(KeyPrefix keyPrefix, String key) {
        String realKey = keyPrefix.getPrefix() + key;
        try{
            return Boolean.TRUE.equals(redisTemplate.delete(key));
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * 将对象转换为对应的json字符串
     *
     * @param value 对象
     * @param <T>   对象的类型
     * @return 对象对应的json字符串
     */
    public static <T> String beanToString(T value){
        if(value == null){
            return null;
        }
        Class<?> clazz = value.getClass();
        if(clazz == Integer.class){
            return ""+ value;
        }else if(clazz == Long.class){
            return ""+ value;
        }else if(clazz == Double.class){
            return value.toString();
        }else if(clazz == Float.class){
            return value.toString();
        }else if(clazz == String.class){
            return ((String) value).trim();
        }else{
            return JSON.toJSONString(value).trim();
        }
    }

    /**
     * 根据传入的class参数，将json字符串转换为对应类型的对象
     *
     * @param str json字符串
     * @param clazz    类型
     * @param <T>      类型参数
     * @return json字符串对应的对象
     */
    public static <T> T stringToBean(String str, Class<T> clazz) {
        if(StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        if(clazz == int.class || clazz == Integer.class) {
            return clazz.cast(Integer.valueOf(str));
        }else if(clazz == long.class || clazz == Long.class) {
            return clazz.cast(Long.valueOf(str));
        }else if(clazz == double.class || clazz == Double.class) {
            return clazz.cast(Double.valueOf(str));
        }else if(clazz == float.class || clazz == Float.class) {
            return clazz.cast(Float.valueOf(str));
        }else if(clazz == boolean.class || clazz == Boolean.class) {
            return clazz.cast(Boolean.valueOf(str));
        }else if(clazz == String.class) {
            return clazz.cast(str);
        }else{
            return JSON.parseObject(str,clazz);
        }
    }

}
