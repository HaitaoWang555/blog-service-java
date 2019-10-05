package com.wht.blog.dao;

import com.wht.blog.entity.Meta;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface MetaMapper {
    int deleteByPrimaryKeyBatch(Map ids);

    int insert(Meta record);

    int insertSelective(Meta record);
    void insertSelectiveBatch(@Param("metas") List<Meta> metas);

    Meta selectByPrimaryKey(Integer id);
    Meta selectByName(String name);

    int updateByPrimaryKeySelective(Meta record);

    int updateByPrimaryKey(Meta record);

    List<Meta> getAll();
    List search(@Param("name")String name, @Param("type")String type);
    List<Meta> searchByIds(@Param("ids")String[] ids);
    List selectMetasDtoPublish(@Param("type")String type);

    List<Meta> selectByArticles(@Param("articleId") Integer articleId, @Param("type") String type);
}