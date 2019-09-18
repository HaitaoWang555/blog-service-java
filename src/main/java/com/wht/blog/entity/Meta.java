package com.wht.blog.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Meta {
    private Integer id;

    private String name;

    private String type;
    private String color;
    private String textColor;

    private Date createdAt;
    private Date updatedAt;
}