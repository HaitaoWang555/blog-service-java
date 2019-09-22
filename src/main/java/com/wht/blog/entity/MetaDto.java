package com.wht.blog.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author wht
 * @since 2019-09-23 5:48
 */
@Data
public class MetaDto {
    private Integer id;

    private String name;

    private String type;
    private String color;
    private String textColor;

    private Date createdAt;
    private Date updatedAt;
    List<Middle> middles;
}
