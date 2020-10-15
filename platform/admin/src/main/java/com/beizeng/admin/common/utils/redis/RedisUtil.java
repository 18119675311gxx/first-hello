package com.beizeng.admin.common.utils.redis;

import com.liren.basic.utils.SerializeUtil;
import redis.clients.jedis.Jedis;

/**
 * @description: <h1>RedisUtil Redis 工具类</h1>
 * @author:
 **/

public class RedisUtil {

    /**
     * 常用过期时间 一天
     **/
    public static final int NORMAL_EXPIRE_TIME = 24 * 60 * 60;

    /**
     * @param key
     * @param value
     * @Description: 把一个对象存入redis
     */
    public static void setObjectValue(String key, Object value) {
        Jedis jedis = JedisUtil.getJedis();
        try {
            SerializeUtil su = new SerializeUtil();
            jedis.set(key.getBytes(), su.serialize(value));
        } finally {
            JedisUtil.returnResource(jedis);
        }
    }

    /**
     * @param key
     * @param value
     * @Description: 把一个字符串存入redis
     */
    public static void setStringValue(String key, String value) {

        Jedis jedis = JedisUtil.getJedis();
        try {
            jedis.set(key, value);
        } finally {
            JedisUtil.returnResource(jedis);
        }
    }

    /**
     * @param key
     * @param time  过期时间(秒)
     * @param value
     * @Description: 把一个对象存入redis
     */
    public static void setExpireObject(String key, int time, Object value) {

        Jedis jedis = JedisUtil.getJedis();
        try {
            SerializeUtil su = new SerializeUtil();
            jedis.setex(key.getBytes(), time, su.serialize(value));
        } finally {
            JedisUtil.returnResource(jedis);
        }
    }

    /**
     * @param key
     * @param time  过期时间(秒)
     * @param value
     * @Description: 把一个字符串存入redis
     */
    public static void setExpireString(String key, int time, String value) {
        Jedis jedis = JedisUtil.getJedis();
        try {
            jedis.setex(key, time, value);
        } finally {
            JedisUtil.returnResource(jedis);
        }
    }

    /**
     * @param key
     * @Description: 查询一个key从redis
     */
    public static Object getObjectValue(String key) {
        Jedis jedis = JedisUtil.getJedis();
        Object o = new Object();
        try {
            SerializeUtil su = new SerializeUtil();
            o = su.unserialize(jedis.get(key.getBytes()));
        } finally {
            JedisUtil.returnResource(jedis);
        }
        return o;
    }

    /**
     * @param key
     * @Description: 查询一个key从redis
     */
    public static String getStringValue(String key) {
        Jedis jedis = JedisUtil.getJedis();
        String value = "";
        try {
            value = jedis.get(key);
        } finally {
            JedisUtil.returnResource(jedis);
        }
        return value;
    }

    /**
     * @param key
     * @Description: 删除对象
     */
    public static void removeString(String key) {
        Jedis jedis = JedisUtil.getJedis();
        try {
            jedis.del(key);
        } finally {
            JedisUtil.returnResource(jedis);
        }
    }

    /**
     * @param key
     * @Description 删除一个key
     */
    public static void removeObject(String key) {
        Jedis jedis = JedisUtil.getJedis();
        try {
            jedis.del(key.getBytes());
        } finally {
            JedisUtil.returnResource(jedis);
        }
    }

    /**
     * @param key
     * @param value
     * @Description: 把一个对象存入队列
     */
    public static void setQueueValue(String key, Object value) {
        Jedis jedis = JedisUtil.getJedis();
        try {
            SerializeUtil su = new SerializeUtil();
            jedis.rpush(key.getBytes(), su.serialize(value));
            //	写这个代码的有问题哦， 如果数据不停，就会一直不过期
            jedis.expire(key.getBytes(), 24 * 60 * 60);
        } finally {
            JedisUtil.returnResource(jedis);
        }
    }

    /**
     * @param key
     * @param value
     * @Description: 把一个对象存入队列
     */
    public static void setQueueValueNoExpire(String key, Object value) {
        Jedis jedis = JedisUtil.getJedis();
        try {
            SerializeUtil su = new SerializeUtil();
            jedis.rpush(key.getBytes(), su.serialize(value));
        } finally {
            JedisUtil.returnResource(jedis);
        }
    }

    /**
     * @param key
     * @Description: 把一个对象取出队列
     */
    public static Object getQueueValue(String key) {
        Jedis jedis = JedisUtil.getJedis();
        Object o = new Object();
        try {
            SerializeUtil su = new SerializeUtil();
            o = su.unserialize(jedis.lpop(key.getBytes()));
        } finally {
            JedisUtil.returnResource(jedis);
        }
        return o;
    }

    /**
     * @param key
     * @Description: 设置key的过期时间
     */
    public static void setExpireKey(String key, int seconds) {
        Jedis jedis = JedisUtil.getJedis();
        try {
            jedis.expire(key, seconds);
        } finally {
            JedisUtil.returnResource(jedis);
        }
    }
}
