package com.wht.blog.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    private Integer id;

    private Integer articleId;

    private Integer parentId;

    private Integer userId;

    private Integer replyUserId;

    private Integer agree;

    private Integer disagree;

    private String ip;

    private String agent;

    private Date created;

    private String content;

    private Boolean isHaveLeaf;

}