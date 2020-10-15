package com.beizeng.admin.controller;


import com.beizeng.admin.common.config.JedisConfig;
import com.beizeng.admin.common.sys.annotion.IgnToken;
import com.beizeng.admin.common.utils.token.TokenUtil;
import com.beizeng.admin.entity.User;
import com.beizeng.admin.service.MyUserService;
import com.liren.basic.common.response.JsonReturn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @description: <h1>LoginController 登录控制类</h1>
 * @author:
 **/
@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

    private static JedisConfig jedisConfig;


    private final MyUserService myUserService;

    public LoginController(MyUserService myUserService, JedisConfig jedisConfig) {
        this.myUserService = myUserService;
        this.jedisConfig = jedisConfig;
    }

    @GetMapping("/login/{userId}")
    public String hello(@PathVariable String userId) {
        User user = myUserService.queryuserbyid(userId);
        String token = TokenUtil.createToken(user);
        log.info("token:{}", token);
        return token;
    }

    /**
     * 忽略token验证
     *
     * @return
     * @author:
     */
    @IgnToken
    @GetMapping("/testLogin/{userId}")
    public JsonReturn testLogin(@PathVariable String userId) {
        User user = myUserService.queryuserbyid(userId);
        return JsonReturn.send(JsonReturn.CODE_SUCCESS, "请求成功", user);
    }

    /**
     * 忽略token验证
     *
     * @author:
     */
    @IgnToken
    @GetMapping("/userLogin")
    public JsonReturn adminlogin(String userId) {
        User user = myUserService.queryuserbyid(userId);
        String token = TokenUtil.createToken(user);
        log.info("token:{}", token);
        return JsonReturn.send(JsonReturn.CODE_SUCCESS, "请求成功", token);
    }

}
