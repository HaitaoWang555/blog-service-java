package com.wht.blog.service;

import com.wht.blog.dao.ArticleMapper;
import com.wht.blog.dto.Archives;
import com.wht.blog.entity.Article;
import com.wht.blog.util.Const;
import com.wht.blog.util.Method;
import com.wht.blog.util.Types;
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
    @Resource
    private LogService logService;

    public Article getOneById(int id) {
        return articleMapper.selectByPrimaryKey(id);
    }
    public Article getOneByIdNoContent(int id) {
        return articleMapper.selectByPrimaryKeyNoContent(id);
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

    public int updateByPrimaryKeySelective(Article article) {
        return articleMapper.updateByPrimaryKeySelective(article);
    }

    public void del(Map<String, String> ids) {
        int delLen =  articleMapper.deleteByPrimaryKeyBatch(ids);
        if (delLen != 0) {
            this.saveLog(ids.get("ids"));
        }
    }

    private void transformPreView(Article article) {
        String content = article.getContent();
        content =Method.delHTMLTag(content);
        Integer maxLen = Const.MAX_PREVIEW_COUNT;
        Integer newLen = content.length() > maxLen ? maxLen : content.length();
        content =content.substring(0, newLen).concat(" ......");
        article.setContent(content);
    }

    public List<Archives> archive(List<Article> articleList, List<Archives> archives) {
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

    private void saveLog(String ids) {
        logService.save(
                Types.LOG_ACTION_DELETE, "id:" + ids,
                Types.LOG_MESSAGE_DELETE_ARTICLE,
                Types.LOG_TYPE_OPERATE,
                Method.getIp()
        );
    }
}