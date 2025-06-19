package somt.common.redis;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Repository
public class RedisRepository {
    private final RedisTemplate<String,Object> redisTemplate;

    public void save(String key,String vale,Long limit){
        redisTemplate.opsForValue().set(key,vale,limit, TimeUnit.MICROSECONDS);
    }

    public Object getValue(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key){
        redisTemplate.delete(key);
    }


    public void modify(String key, String newValue){
        Long limit = redisTemplate.getExpire(key,TimeUnit.MILLISECONDS);
        delete(key);
        save(key,newValue,limit);
    }

}
