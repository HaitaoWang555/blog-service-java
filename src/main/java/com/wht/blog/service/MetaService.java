package com.wht.blog.service;

import com.wht.blog.dao.MetaMapper;
import com.wht.blog.dao.MiddleMapper;
import com.wht.blog.entity.Meta;
import com.wht.blog.entity.Middle;
import com.wht.blog.exception.TipException;
import com.wht.blog.util.Method;
import com.wht.blog.util.Types;
import org.springframework.stereotype.Service;

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
    @Resource
    private LogService logService;

    public List<Meta> getAll () {
        return metaMapper.getAll();
    }
    public List search(String name, String type) {
        return metaMapper.search(name, type);
    }
    public List<Meta> search(String[] ids) {
        return metaMapper.searchByIds(ids);
    }
    public Meta getOneById (int id) {
        return metaMapper.selectByPrimaryKey(id);
    }
    public void insertSelective (Meta meta) {
        metaMapper.insertSelective(meta);
    }
    public void insertSelective (List<Meta> metas) {
        metaMapper.insertSelectiveBatch(metas);
    }
    public void updateByPrimaryKeySelective (Meta meta) {
        metaMapper.updateByPrimaryKeySelective(meta);
    }

    public void del (Map<String, String> ids) {
        int delLen = metaMapper.deleteByPrimaryKeyBatch(ids);
        if (delLen != 0) {
            this.saveLog(ids.get("ids"));
        }
    }

    public void saveOrRemoveMeta(String names, String type, Integer articleId) {
        type = verifyType(type);
        if (null == articleId) {
            throw new TipException("关联文章id不能为空");
        }

        removeMetas(names, type, articleId);
        saveMetas(names, type, articleId);
    }

    private void saveMetas(String names, String type, Integer articleId) {
        List<Meta> metas = metaMapper.selectByArticles(articleId, type);
        Set<String> metaSet = new HashSet<>();
        for (Meta meta : metas) {
            metaSet.add(meta.getName());
        }
        String[] nameArr = names.split(",");
        for (String name : nameArr) {
            if (!metaSet.contains(name)) {
                Meta meta = metaMapper.selectByName(name);
                if (meta != null) {
                    Middle middle = new Middle();
                    middle.setAId(articleId);
                    middle.setMId(meta.getId());
                    middleMapper.insert(middle);
                }
            }
        }
    }

    private void removeMetas(String names, String type, Integer articleId) {
        String[] nameArr = names.split(",");
        Set<String> nameSet = new HashSet<>(Arrays.asList(nameArr));
        List<Meta> metas = metaMapper.selectByArticles(articleId, type);
        for (Meta meta : metas) {
            if (!nameSet.contains(meta.getName())) {
                middleMapper.deleteByMiddle(articleId, meta.getId());
                System.out.println(meta.getId());
            }
        }

    }

    private String verifyType(String type) {
        if (type.equals(Types.CATEGORY) || type.equals(Types.TAG)) {
            return type;
        } else {
            throw new TipException("传输的属性类型不合法");
        }
    }

    public List getMetaDto(String type){
        return metaMapper.selectMetasDtoPublish(type);
    }

    private void saveLog(String ids) {
        logService.save(
                Types.LOG_ACTION_DELETE, "id:" + ids,
                Types.LOG_MESSAGE_DELETE_META,
                Types.LOG_TYPE_OPERATE,
                Method.getIp()
        );
    }
}
