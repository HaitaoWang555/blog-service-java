package com.wht.blog.dto;

import com.wht.blog.entity.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wht
 * @since 2019-09-24 8:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentDto extends Comment{
    private UserDto userInfo;
    private UserDto replyUserInfo;
}
