package com.wht.blog.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wht.blog.dto.Archives;
import com.wht.blog.dto.CommentDto;
import com.wht.blog.dto.Pagination;
import com.wht.blog.entity.Article;
import com.wht.blog.entity.Comment;
import com.wht.blog.entity.User;
import com.wht.blog.service.ArticleService;
import com.wht.blog.service.CommentService;
import com.wht.blog.service.MetaService;
import com.wht.blog.service.UsersService;
import com.wht.blog.util.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 博客前台 controller
 *
 * @author wht
 * @since 2019-09-22 22:21
 */
@RestController
@RequestMapping("/api/portal")
public class PortalController extends BaseController{
    @Resource
    private ArticleService articleService;
    @Resource
    private MetaService metaService;
    @Resource
    private CommentService commentService;
    @Resource
    private UsersService usersService;

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
    public RestResponse getArchiveList() {
        List<Article> article = articleService.getAll();
        List<Archives> archives = new ArrayList<>();
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
        Page<CommentDto> comment = PageHelper.startPage(page, limit).doSelectPage(() ->
                commentService.getAll(article_id, parent_id)
        );
        return RestResponse.ok(new Pagination<Archives>(comment));
    }
    @PostMapping("/comment/add")
    public RestResponse addComment(
            @RequestParam(value = "article_id") Integer article_id,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "parent_id", required = false) Integer parent_id,
            @RequestParam(value = "reply_user_id", required = false) Integer reply_user_id
    ) {
        User user = this.user();
        if (null == user) {
            return RestResponse.fail(ErrorCode.NOT_LOGIN.getCode(),"请登陆");
        } else {
            Comment comment = this.insertComment(article_id, content, parent_id, reply_user_id, user);
            if (parent_id != null) {
                this.upDateParentComment(parent_id);
            }
            this.upDateArticle(article_id);
            return RestResponse.ok(comment, "评论成功");
        }
    }
    @PostMapping("/user/login")
    public RestResponse login(@RequestParam String username, @RequestParam String password) {
        User user = usersService.login(username, password);
        request.getSession().setAttribute(Const.USER_SESSION_KEY, user);
        return RestResponse.ok(user,"登录成功" );
    }
    @PostMapping("/user/logout")
    public RestResponse logout() {
        request.getSession().setAttribute(Const.USER_SESSION_KEY, null);
        return RestResponse.ok("退出成功" );
    }
    @PostMapping("/user/register")
    public RestResponse addUser(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "screenName", required = false) String screen_name
    ) {
        User user = Method.addUser(username, email, screen_name, password);
        usersService.addUser(user);
        this.login(username, password);
        return RestResponse.ok(user,"注册成功并登录");
    }
    private Comment insertComment(Integer article_id, String content, Integer parent_id, Integer reply_user_id, User user) {
        Comment comment = new Comment();
        comment.setArticleId(article_id);
        comment.setContent(content);
        if (parent_id != null) comment.setParentId(parent_id);
        if (reply_user_id != null) comment.setReplyUserId(reply_user_id);
        String Ip = Method.getIp();
        comment.setIp(Ip);
        String agent = Method.getAgent();
        comment.setAgent(agent);
        comment.setUserId(user.getId());
        comment.setCreated(new Date());
        commentService.add(comment);
        return comment;
    }
    private void upDateParentComment(Integer parent_id) {
        Comment commentParent = new Comment();
        commentParent.setId(parent_id);
        commentParent.setIsHaveLeaf(true);
        commentService.update(commentParent);
    }
    private void upDateArticle(Integer article_id) {
        Article article = articleService.getOneByIdNoContent(article_id);
        int count = article.getCommentCount() != null ? article.getCommentCount() : 0;
        article.setCommentCount(count + 1);
        articleService.updateByPrimaryKeySelective(article);
    }
}
