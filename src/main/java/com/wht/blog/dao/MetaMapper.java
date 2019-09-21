package com.wht.blog.dao;

import com.wht.blog.entity.Meta;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


public interface MetaMapper {
    int deleteByPrimaryKeyBatch(Map ids);

    int insert(Meta record);

    int insertSelective(Meta record);

    Meta selectByPrimaryKey(Integer id);
    Meta selectByName(String name);

    int updateByPrimaryKeySelective(Meta record);

    int updateByPrimaryKey(Meta record);

    List getAll();
    List search(@Param("name")String name, @Param("type")String type);

    List<Meta> selectByArticles(@Param("articleId") Integer articleId, @Param("type") String type);
}