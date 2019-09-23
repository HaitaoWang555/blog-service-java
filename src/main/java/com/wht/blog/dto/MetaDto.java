package com.wht.blog.dto;

import com.wht.blog.entity.Meta;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author wht
 * @since 2019-09-23 11:05
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class MetaDto extends Meta{
    List<ArticleInfoDto> articles;
}
