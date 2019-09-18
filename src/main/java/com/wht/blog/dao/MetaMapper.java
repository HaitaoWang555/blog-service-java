package com.wht.blog.dao;

import com.github.pagehelper.Page;
import com.wht.blog.entity.Meta;

import java.util.List;

public interface MetaMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Meta record);

    int insertSelective(Meta record);

    Meta selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Meta record);

    int updateByPrimaryKey(Meta record);

    Page<Meta> getAll(Integer page,Integer limit);
    List search(String name, String type);
}