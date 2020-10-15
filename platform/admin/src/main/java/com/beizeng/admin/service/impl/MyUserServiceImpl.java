package com.beizeng.admin.service.impl;

import com.beizeng.admin.dao.UserDao;
import com.beizeng.admin.entity.User;
import com.beizeng.admin.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: <h1>MyUserServiceImpl MyUser实现层</h1>
 * @author:
 **/
@Service
public class MyUserServiceImpl implements MyUserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User queryuserbyid(String userId) {
        User user = userDao.selectByPrimaryKey(Integer.parseInt(userId));
        return user;
    }

    @Override
    public List<User> getUserByPage() {
        List<User> users = userDao.selAllUser();
        return users;
    }
}
