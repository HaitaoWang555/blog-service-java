package com.wht.blog.service;

import com.github.pagehelper.Page;
import com.wht.blog.dao.ArticleMapper;
import com.wht.blog.dto.Archives;
import com.wht.blog.entity.Article;
import com.wht.blog.util.Const;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public List<Article> getAll() {
        return articleMapper.getAll();
    }

    public void getAllWithContent() {
        List<Article> articleList = articleMapper.getAllWithContent();
        for (Article item : articleList) {
            this.transformPreView(item);
        }
    }

    public List search(String title, String status, String type, Integer authorId, String meta) {
        List<Integer> metaArr = new ArrayList<>();
        if (meta != null) {
            for (String ele : meta.split(",")) {
                Integer item = Integer.valueOf(ele);
                metaArr.add(item);
            }
        }
        return articleMapper.search(title, status, type, authorId, metaArr);
    }

    public int insertSelective(Article article) {
        return articleMapper.insertSelective(article);
    }

    public void updateByPrimaryKeySelective(Article article) {
        articleMapper.updateByPrimaryKeySelective(article);
    }

    public void del(Map<String, String> ids) {
        articleMapper.deleteByPrimaryKeyBatch(ids);
    }
    public int getCommentCount (int id) {
        Article article = articleMapper.getCommentCount(id);
        return article.getCommentCount();
    }
    private void transformPreView(Article article) {
        String content = article.getContent();
        Integer maxLen = Const.MAX_PREVIEW_COUNT;
        Integer contentLen = content.length();
        if (contentLen > maxLen) {
            int index = content.substring(0, maxLen).lastIndexOf("</p>");
            int len = index > -1 ? index : maxLen;
            content = content.substring(0, len).concat("......</p>");
        }
        article.setContent(content);
    }

    public Page<Archives> archive(Page<Article> articleList, Page<Archives> archives) {
        String current = "";
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM" );
        for (Article article:articleList) {
            String dateStr = sdf.format(article.getUpdatedAt());
            if (dateStr.equals(current)) {
                Archives arc = archives.get(archives.size() - 1);
                arc.getArticle().add(article);
                arc.setCount(arc.getArticle().size());
            } else {
                current = dateStr;
                Archives arc = new Archives();
                arc.setDateStr(dateStr);
                arc.setCount(1);
                List<Article> arts = new ArrayList<>();
                arts.add(article);
                arc.setArticle(arts);
                archives.add(arc);
            }
        }
        return archives;
    }
}