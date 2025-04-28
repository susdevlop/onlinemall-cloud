package us.sushome.onlinemallcloud.omccommon.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import us.sushome.onlinemallcloud.omccommon.api.cache.RedisLockServiceApi;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Service
public class RedisLockService implements RedisLockServiceApi {
    private static Logger logger = LoggerFactory.getLogger(RedisLockService.class);

    private final StringRedisTemplate redisTemplate;

    @Autowired  // 使用构造器注入替代@Resource
    public RedisLockService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private final String LOCK_SUCCESS = "OK";

    private final Long RELEASE_SUCCESS = 1L;

    /**
     * 获取锁
     *
     * @param lockKey     锁
     * @param uniqueValue 能够唯一标识请求的值，以此保证锁的加解锁是同一个客户端
     * @param expireTime  过期时间, 单位：milliseconds
     * @return true if lock is acquired, false acquire timeout
     */
    @Override
    public boolean lock(String lockKey, String uniqueValue, long expireTime) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(
                lockKey,
                uniqueValue,
                expireTime,
                TimeUnit.MILLISECONDS);
        return result != null && result;
    }

    @Override
    public boolean unlock(String lockKey, String uniqueValue) {
        // 使用Lua脚本保证操作的原子性
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                "return redis.call('del', KEYS[1]) " +
                "else return 0 " +
                "end";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        Long result = redisTemplate.execute(redisScript, Collections.singletonList(lockKey), uniqueValue);
        return RELEASE_SUCCESS.equals(result);
    }


}
