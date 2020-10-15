package com.beizeng.admin.entity;

import java.io.Serializable;

/**
 * @description: <h1>User 实体类</h1>
 * @author:
 **/

public class User implements Serializable {

    private static final long serialVersionUID = -3667158102115095511L;
    /**
     * id
     */
    private Integer id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    public Integer getId() {
        return this.id;

    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return this.userName;

    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;

    }

    public void setPassword(String password) {
        this.password = password;
    }
}
