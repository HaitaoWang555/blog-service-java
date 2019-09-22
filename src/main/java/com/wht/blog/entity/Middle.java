package com.wht.blog.entity;

import lombok.Data;

@Data
public class Middle {
    private Integer id;

    private Integer aId;

    private Integer mId;
    private Article article;
    private Meta meta;
}