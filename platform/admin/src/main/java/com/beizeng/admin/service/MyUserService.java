package com.beizeng.admin.service;

import com.beizeng.admin.entity.User;

import java.util.List;

/**
 * @description: MyUserService myuser接口层
 * @author:
 **/

public interface MyUserService {

    User queryuserbyid(String userId);

    List<User> getUserByPage();
}
