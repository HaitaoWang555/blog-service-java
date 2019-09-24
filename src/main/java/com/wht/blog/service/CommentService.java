package com.wht.blog.service;

import com.wht.blog.dao.CommentMapper;
import com.wht.blog.entity.Comment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wht
 * @since 2019-09-23 16:02
 */
@Service
public class CommentService {
    @Resource
    private CommentMapper commentMapper;

    public void getAll(Integer article_id, Integer parent_id) {
        commentMapper.search(article_id, parent_id);
    }

    public void add(Comment comment) {
        commentMapper.insertSelective(comment);
    }
    public void update(Comment comment) {
        commentMapper.updateByPrimaryKeySelective(comment);
    }
}
