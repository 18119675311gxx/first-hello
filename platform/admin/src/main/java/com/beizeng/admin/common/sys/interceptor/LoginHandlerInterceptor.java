package com.beizeng.admin.common.sys.interceptor;

import com.alibaba.fastjson.JSON;
import com.beizeng.admin.common.sys.annotion.IgnToken;
import com.beizeng.admin.common.utils.redis.RedisUtil;
import com.beizeng.admin.common.config.TokenConfig;
import com.beizeng.admin.entity.User;
import com.liren.basic.common.response.JsonReturn;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @description: <h1>AuthorizationInterceptor 用户权限拦截器</h1>
 * @author:
 **/
@Slf4j
@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenConfig tokenConfig;

    public static final String LOGIN_ID = "loginId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = tokenConfig.getHeader();

        IgnToken annotation;
        if (handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(IgnToken.class);
            if (annotation != null) {
                return true;
            }
        } else {
            return true;
        }

        // 获取用户凭证
        String token = request.getHeader(tokenConfig.getHeader());
        if (StringUtils.isBlank(token)) {
            token = request.getParameter(tokenConfig.getHeader());
        }
        // 凭证为空
        if (StringUtils.isBlank(token)) {
            return this.resp(response, HttpStatus.UNAUTHORIZED.value(), header + "不能为空！");
//            throw new RRException(JsonReturn.CODE_NO_LOGIN, HttpStatus.UNAUTHORIZED.value(), tokenConfig.getHeader() + "不能为空！");
        }
        // token不为空，开始获取用户数据
        Map<String, Object> map = (Map<String, Object>) RedisUtil.getObjectValue(token);
        if (MapUtils.isEmpty(map)) {
            System.out.println("~~~~~~~~~~~token是无效的~~~~~~~~~~~");
            return this.resp(response, HttpStatus.UNAUTHORIZED.value(), header + "失效，请重新登录！");
        }
        User user = (User) map.get("user");
        if (user == null) {
            return this.resp(response, HttpStatus.UNAUTHORIZED.value(), header + "失效，请重新登录！");
        }

        //设置userId到request里，后续根据userId，获取用户信息
//        request.setAttribute(LOGIN_ID, user);

        return true;
    }

    private boolean resp(HttpServletResponse response, int value, String msg) throws IOException {
        response.reset();
        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        JsonReturn r = new JsonReturn();
        r.put("code", r.CODE_NO_LOGIN);
        r.put("status", value);
        r.put("msg", msg);
        response.getWriter().print(JSON.toJSON(r));
        return false;
    }

}
