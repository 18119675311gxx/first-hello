package com.beizeng.admin.controller;

import com.beizeng.admin.entity.User;
import com.github.pagehelper.PageHelper;
import com.beizeng.admin.common.utils.rocket.RocketMessageProducer;
import com.beizeng.admin.service.MyUserService;
import com.liren.basic.common.exception.RRException;
import com.liren.basic.common.page.Page;
import com.liren.basic.common.response.JsonReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @description: <h1>MyuserController MyUser 控制类</h1>
 * @author:
 **/
@RestController
@RequestMapping("/user")
public class MyUserController {

    @Autowired
    private MyUserService myUserService;

    @RequestMapping(value = {"/queryuserbyid/{userId}"}, method = RequestMethod.GET)
    public User queryuserbyid(@PathVariable String userId) throws RRException {
        User user = myUserService.queryuserbyid(userId);
        return user;
    }

    /**
     * 分页查询
     * @return
     */
    @RequestMapping(value = {"/getUserByPage"}, method = RequestMethod.GET)
    public Map getUserByPage(Page p) {
        //  在查询分页的sql之前，调用开始分页语句！！！
        PageHelper.startPage(p.getPage(), p.getRows());
        List<User> user = myUserService.getUserByPage();
        return p.pageInfo(1, "请求成功！", user);
    }

    /**
     * rocketmq demo
     */
    @RequestMapping(value = {"/useRocketMQ"}, method = RequestMethod.GET)
    public JsonReturn useRocketMQ() {
        RocketMessageProducer.producerDelay("RocketProdTagTest",System.currentTimeMillis(),"RocketProdKeyTest","RocketProdBodyTest");
        return JsonReturn.ok("请求成功！");
    }

}
