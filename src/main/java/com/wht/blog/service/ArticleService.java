package com.wht.blog.service;

import com.wht.blog.dao.ArticleMapper;
import com.wht.blog.entity.Article;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wht
 * @since 2019-09-19 15:04
 */

@Service
public class ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    public Article getOneById(int id) {
        return articleMapper.selectByPrimaryKey(id);
    }

    public List getAll() {
        return articleMapper.getAll();
    }
    public List search(String title, String status,String type,Integer authorId) {
        return articleMapper.search(title, status, type, authorId);
    }

    public int insertSelective (Article article) {
        return articleMapper.insertSelective(article);
    }
    public void updateByPrimaryKeySelective (Article article) {
        articleMapper.updateByPrimaryKeySelective(article);
    }
    public void del (Map ids) {
        articleMapper.deleteByPrimaryKeyBatch(ids);
    }
}
