package com.wht.blog.dao;

import com.wht.blog.entity.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ArticleMapper {
    void deleteByPrimaryKeyBatch(Map ids);

    int insert(Article record);

    int insertSelective(Article record);

    Article selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKey(Article record);
    List getAll();
    List search(@Param("title")String title);
}