package com.beizeng.admin.dao;

import com.beizeng.admin.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description: <h1>UserDao dao</h1>
 * @author:
 **/
@Repository
public interface UserDao {

    User selectByPrimaryKey(Integer id);

    List<User> selAllUser();

}
