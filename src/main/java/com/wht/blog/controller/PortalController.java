package com.wht.blog.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wht.blog.dto.Archives;
import com.wht.blog.dto.Pagination;
import com.wht.blog.entity.Article;
import com.wht.blog.entity.Comment;
import com.wht.blog.entity.User;
import com.wht.blog.service.ArticleService;
import com.wht.blog.service.CommentService;
import com.wht.blog.service.MetaService;
import com.wht.blog.util.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 博客前台 controller
 *
 * @author wht
 * @since 2019-09-22 22:21
 */
@RestController
@RequestMapping("/portal")
public class PortalController extends BaseController{
    @Resource
    private ArticleService articleService;
    @Resource
    private MetaService metaService;
    @Resource
    private CommentService commentService;
    @GetMapping("/article/list")
    public RestResponse home(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = Const.PAGE_SIZE) Integer limit
    ) {
        String sortBy = "updated_at desc";
        Page<Article> article = PageHelper.startPage(page, limit, sortBy).doSelectPage(() ->
                articleService.getAllWithContent()
        );
        return RestResponse.ok(new Pagination<Article>(article));
    }
    @GetMapping("/article/getOne")
    public RestResponse getOne(
            @RequestParam(value = "id", required = false) Integer id
    ) {
        Article article = articleService.getOneById(id);
        Integer hits = article.getHits() + 1;
        article.setHits(hits);
        articleService.updateByPrimaryKeySelective(article);
        return RestResponse.ok(article);
    }

    @GetMapping("/archive/list")
    public RestResponse getArchiveList(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = Const.PAGE_SIZE) Integer limit
    ) {
        String sortBy = "updated_at desc";
        Page<Article> article = PageHelper.startPage(page, limit, sortBy).doSelectPage(() ->
                articleService.getAll()
        );
        Page<Archives> archives = new Page<>();
        archives = articleService.archive(article, archives);
        return RestResponse.ok(new Pagination<Archives>(archives));
    }
    @GetMapping("/tag/list")
    public RestResponse getTagList() {
        return RestResponse.ok(metaService.getMetaDto(Types.TAG));
    }

    @GetMapping("/category/list")
    public RestResponse getCategoryList() {
        return RestResponse.ok(metaService.getMetaDto(Types.CATEGORY));
    }
    @GetMapping("/comment/list")
    public RestResponse getCommentList(
            @RequestParam(value = "articleId") Integer article_id,
            @RequestParam(value = "parentId", required = false) Integer parent_id,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = Const.PAGE_SIZE) Integer limit
    ) {
        Page<Article> comment = PageHelper.startPage(page, limit).doSelectPage(() ->
                commentService.getAll(article_id, parent_id)
        );
        return RestResponse.ok(new Pagination<Archives>(comment));
    }
    @PostMapping("/comment/add")
    public RestResponse addComment(
            @RequestParam(value = "articleId") Integer article_id,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "parentId", required = false) Integer parent_id,
            @RequestParam(value = "replyUserId", required = false) Integer reply_user_id
    ) {
        User user = this.user();
        if (null == user) {
            return RestResponse.fail(ErrorCode.NOT_LOGIN.getCode(),"请登陆");
        } else {
            this.insertComment(article_id, content, parent_id, reply_user_id, user);
            if (parent_id != null) {
                this.upDateParentComment(parent_id);
            }
            this.upDateArticle(article_id);
            return RestResponse.ok("添加成功");
        }
    }

    private void insertComment(Integer article_id, String content, Integer parent_id, Integer reply_user_id, User user) {
        Comment comment = new Comment();
        comment.setArticleId(article_id);
        comment.setContent(content);
        if (parent_id != null) comment.setParentId(parent_id);
        if (reply_user_id != null) comment.setParentId(reply_user_id);
        String Ip = Method.getIp();
        comment.setIp(Ip);
        String agent = Method.getAgent();
        comment.setAgent(agent);
        comment.setUserId(user.getId());
        comment.setCreated(new Date());
        commentService.add(comment);
    }
    private void upDateParentComment(Integer parent_id) {
        Comment commentParent = new Comment();
        commentParent.setId(parent_id);
        commentParent.setIsHaveLeaf(true);
        commentService.update(commentParent);
    }
    private void upDateArticle(Integer article_id) {
        Article article = new Article();
        article.setId(article_id);
        Article article1 = articleService.getOneById(article_id);
        int count = article1.getCommentCount() != null ? article1.getCommentCount() : 0;
        article.setCommentCount(count + 1);
        articleService.updateByPrimaryKeySelective(article);
    }
}
