package com.wht.blog.dao;

import com.wht.blog.entity.Meta;

import java.util.List;
import java.util.Map;


public interface MetaMapper {
    int deleteByPrimaryKeyBatch(Map ids);

    int insert(Meta record);

    int insertSelective(Meta record);

    Meta selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Meta record);

    int updateByPrimaryKey(Meta record);

    List getAll();
    List search(String name, String type);
}