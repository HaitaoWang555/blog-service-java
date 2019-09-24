package com.wht.blog.dao;

import com.wht.blog.entity.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CommentMapper {
    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Comment record);
    void deleteByPrimaryKeyBatch(Map ids);
    List search(@Param("article_id") Integer article_id, @Param("parent_id") Integer parent_id);
}