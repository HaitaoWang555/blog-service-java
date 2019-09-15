package com.wht.blog.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Integer id;

    private String username;

    private String passwordMd5;

    private String email;

    private String screenName;

    private Date created;

    private Date logged;

}