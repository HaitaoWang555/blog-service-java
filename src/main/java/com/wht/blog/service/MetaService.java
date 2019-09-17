package com.wht.blog.service;

import com.wht.blog.dao.MetaMapper;
import com.wht.blog.entity.Meta;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wht
 * @since 2019-09-16 16:42
 */
@Service
public class MetaService {
    @Resource
    private MetaMapper metaMapper;


    public List getAll () {
        return metaMapper.getAll();
    }
    public List search(String name, String type) {
        return metaMapper.search(name, type);
    }
    public Meta getOneById (int id) {
        return metaMapper.selectByPrimaryKey(id);
    }
    public void add (Meta meta) {
        metaMapper.insert(meta);
    }
    public void insertSelective (Meta meta) {
        metaMapper.insertSelective(meta);
    }

    public void updateByPrimaryKeySelective (Meta meta) {
        metaMapper.updateByPrimaryKeySelective(meta);
    }

    public void del (Integer id) {
        metaMapper.deleteByPrimaryKey(id);
    }
}
