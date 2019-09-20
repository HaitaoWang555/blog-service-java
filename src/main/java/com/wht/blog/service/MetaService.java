package com.wht.blog.service;

import com.wht.blog.dao.MetaMapper;
import com.wht.blog.dao.MiddleMapper;
import com.wht.blog.entity.Meta;
import com.wht.blog.exception.TipException;
import com.wht.blog.util.Types;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author wht
 * @since 2019-09-16 16:42
 */
@Service
public class MetaService {
    @Resource
    private MetaMapper metaMapper;
    @Resource
    private MiddleMapper middleMapper;


    public List getAll () {
        return metaMapper.getAll();
    }
    public List search(String name, String type) {
        return metaMapper.search(name, type);
    }
    public Meta getOneById (int id) {
        return metaMapper.selectByPrimaryKey(id);
    }
    public void insertSelective (Meta meta) {
        metaMapper.insertSelective(meta);
    }

    public void updateByPrimaryKeySelective (Meta meta) {
        metaMapper.updateByPrimaryKeySelective(meta);
    }

    public void del (Map ids) {
        metaMapper.deleteByPrimaryKeyBatch(ids);
    }

    public void saveOrRemoveMetas(String names, String type, Integer articleId) {
        type = verifyType(type);
        if (null == articleId) {
            throw new TipException("关联文章id不能为空");
        }

        if (StringUtils.isEmpty(names)) {
            middleMapper.deleteByArticleId(articleId);
        }

        removeMetas(names, type, articleId);
        saveMetas(names, type, articleId);
    }

    /**
     * 添加names新加的属性到数据库
     *
     * @param names
     * @param type
     * @param articleId
     */
    private void saveMetas(String names, String type, Integer articleId) {
        List<Meta> metas = metaMapper.selectByArticles(articleId, type);
        Set<String> metaSet = new HashSet<>();
        for (Meta meta : metas) {
            metaSet.add(meta.getName());
        }
    }

    /**
     * 从数据库中删除names属性中没有的
     *
     * @param names
     * @param type
     * @param articleId
     */
    private void removeMetas(String names, String type, Integer articleId) {
        String[] nameArr = names.split(",");
        Set<String> nameSet = new HashSet<>(Arrays.asList(nameArr));
        List<Meta> metas = metaMapper.selectByArticles(articleId, type);

    }
    /**
     * 验证Type是否为定义的
     *
     * @return
     */
    private String verifyType(String type) {
        switch (type) {
            case Types.CATEGORY:
                return type;
            case Types.TAG:
                return type;
            default:
                throw new TipException("传输的属性类型不合法");
        }
    }
}
