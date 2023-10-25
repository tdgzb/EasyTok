package com.utlis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 设置过期时间
     *
     * @param key      键
     * @param time     过期时间
     * @param timeUnit 时间单位
     * @return 成功与否
     */
    public boolean expire(String key, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, timeUnit);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * 根据 key 获取过期时间
     *
     * @param key 键
     * @return 时间(秒)
     */
    public long getExpire(Object key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * key是否存在
     *
     * @param key key
     * @return T/F
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param keys 可变参数
     */
    public void delete(String... keys) {
        if (keys != null && keys.length > 0) {
            if (keys.length == 1) {
                Boolean result = redisTemplate.delete(keys[0]);
                log.debug("删除缓存:" + keys[0] + result);
            } else {
                Set<Object> keySet = new HashSet<>();
                for (String key : keys) {
                    keySet.add(redisTemplate.keys(key));
                }
                long count = redisTemplate.delete(keySet);
                log.debug("删除缓存数量:" + count);
            }
        }
    }

    /**
     * 字符串缓存获取
     *
     * @param key key
     * @return value
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }


    /**
     * 字符串缓存存入
     *
     * @param key   key
     * @param value value
     * @return T/F
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 字符串缓存存入并设置时间,默认单位为 second
     *
     * @param key   key
     * @param value value
     * @param time  time
     * @return T/F
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key  键
     * @param item 项
     * @return 值
     */
    public Object hmget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * key对应的整个hashmap
     *
     * @param key 键
     * @return 所有值
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * hash放入值
     *
     * @param key 键
     * @param map hashmap
     * @return T/F
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * hash放入值并设置时间
     *
     * @param key  键
     * @param map  hashmap
     * @param time 过期时间
     * @return T/F
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 对应key中放置item-value
     *
     * @param key   键
     * @param item  项
     * @param value 项值
     * @return T/F
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 对应key中放置item-value, 不存在创建对应的key
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  过期时间
     * @return T/F
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 删除对应hashKey
     *
     * @param key  hashKey
     * @param item 项
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * hash表中是否存在该项值
     *
     * @param key  key
     * @param item item
     * @return T/F
     */
    public boolean hHashKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * 获取list缓存内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束0 到 -1 代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取list缓存内容
     *
     * @param key   键
     * @return
     */
    public List<Object> lGet(String key) {
        return lGet(key,0,-1);
    }

    /**
     * 尾插法
     * @param key 键
     * @param value 值
     * @return 操作结果
     */
    public boolean lSetRight(String key, Object value){
        try {
            redisTemplate.opsForList().rightPush(key,value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return false;
        }
    }

    /**
     * 头插法
     * @param key 键
     * @param value 值
     * @return 操作结果
     */
    public boolean lSetLeft(String key,Object value){
        try {
            redisTemplate.opsForList().leftPush(key,value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return false;
        }
    }

    /**
     * 存入list缓存
     *
     * @param key   键
     * @param value 值
     * @return T/F
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

}
