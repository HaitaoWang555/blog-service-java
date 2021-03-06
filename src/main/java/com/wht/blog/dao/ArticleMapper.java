package com.wht.blog.dao;

import com.wht.blog.entity.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ArticleMapper {
    int deleteByPrimaryKeyBatch(Map ids);

    int insert(Article record);

    int insertSelective(Article record);
    Article selectByPrimaryKey(Integer id);
    Article selectByPrimaryKeyNoContent(Integer id);
    Article getCommentCount(Integer id);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKey(Article record);
    List<Article> getAll();
    List<Article> getAllWithContent();
    List search(
            @Param("title")String title,
            @Param("status")String status,
            @Param("type")String type,
            @Param("authorId")Integer authorId,
            @Param("meta")List meta
    );
}