package com.wht.blog.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Log {
    private Integer id;

    private String action;

    private String message;

    private String type;

    private String ip;

    private Integer userId;

    private Date created;

    private String data;

}