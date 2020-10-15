package com.beizeng.admin.common.utils.redis;

import com.beizeng.admin.common.config.JedisConfig;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @description: <h1>JedisUtil jedis工具类</h1>
 * @author:
 **/

public class JedisUtil {
    private static JedisConfig jedisConfig;

    public JedisUtil(JedisConfig jedisConfig) {
        this.jedisConfig = jedisConfig;
    }

    /**
     * @Description: <h2>构建jedis 连接池</h2>
     * 为了方便，与加快构建性能，yml文件中的 配置 仅仅做注释使用。
     * @return: {@link JedisPool}
     * @author:
     */
    public static JedisPool getPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setTestOnBorrow(false);
        config.setTestWhileIdle(true);

        config.setMaxTotal(1000);
        config.setMaxIdle(10);
        config.setMaxWaitMillis(10000L);
        config.setMinIdle(5);
        config.setTimeBetweenEvictionRunsMillis(100L);
        return new JedisPool(config, jedisConfig.getHost(), jedisConfig.getPort());
    }

    /**
     * @Description: <h2>获取jedis 实例</h2>
     * @return: {@link Jedis}
     * @author:
     */
    public static Jedis getJedis() {
        Jedis jedis = getPool().getResource();
        if (StringUtils.isNotEmpty(jedisConfig.getPassword())) {
            jedis.auth(jedisConfig.getPassword());
        }

        if (jedisConfig.getHost().length() < 20) {
            jedis.select(2);
        }
        return jedis;
    }

    /**
     * @Description: <h2>返还到连接池</h2>
     * @author:
     */
    public static void returnResource(Jedis redis) {
        if (redis != null) {
            redis.close();
        }
    }
}
