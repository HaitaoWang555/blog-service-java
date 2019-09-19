package com.wht.blog.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Article {
    private Integer id;

    private String title;

    private Date createdAt;

    private Date updatedAt;

    private Integer authorId;

    private Integer hits;

    private String tags;

    private String category;

    private String status;

    private String type;

    private Boolean allowComment;

    private String content;

}