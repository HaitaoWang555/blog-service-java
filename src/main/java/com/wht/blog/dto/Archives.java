package com.wht.blog.dto;

import com.wht.blog.entity.Article;
import lombok.Data;

import java.util.List;

/**
 * 归档 Dto
 * @author wht
 * @since 2019-09-22 23:48
 */
@Data
public class Archives {

    private String dateStr;

    private Integer count;

    private List<Article> article;
}
