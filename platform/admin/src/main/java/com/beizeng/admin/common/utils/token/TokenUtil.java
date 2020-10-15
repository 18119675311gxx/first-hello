package com.beizeng.admin.common.utils.token;

import com.beizeng.admin.common.utils.redis.RedisUtil;
import com.beizeng.admin.common.config.TokenConfig;
import com.beizeng.admin.entity.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @description: <h1>TokenUtil Token工具类</h1>
 * @author:
 **/
@Component
public class TokenUtil {

    private static TokenConfig tokenConfig;

    public TokenUtil(TokenConfig tokenConfig) {
        this.tokenConfig = tokenConfig;
    }

    /**
     * 生成token
     *
     * @author:
     */
    public static String createToken(User user) {

        String token = UUID.randomUUID().toString().replace("-", "") + "_" + user.getUserName();
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("token", token);
        //  map.put("权限，路径",);
        RedisUtil.setExpireObject(token, tokenConfig.getExpire(), map);
        return token;
    }

    /**
     * 退出token
     *
     * @author:
     */
    public static void loginOff(String token) {
        RedisUtil.removeObject(token);
    }

    /**
     * 获取用户信息
     *
     * @author:
     */
    public void getUserInfoByToken(String token) {

    }

}
