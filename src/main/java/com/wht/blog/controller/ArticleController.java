package com.wht.blog.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wht.blog.dto.Pagination;
import com.wht.blog.entity.Article;
import com.wht.blog.service.ArticleService;
import com.wht.blog.util.Consts;
import com.wht.blog.util.RestResponse;
import com.wht.blog.util.Types;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wht
 * @since 2019-09-19 15:03
 */
@RestController
@RequestMapping("/manage/article")
public class ArticleController extends BaseController{
    @Resource
    private ArticleService articleService;

    @GetMapping("/list")
    public RestResponse searchList(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "sortBy", required = false, defaultValue = "updated_at desc") String sortBy,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = Consts.PAGE_SIZE) Integer limit
    ) {
        if (!StringUtils.isEmpty(id)) {
            return RestResponse.ok(articleService.getOneById(id));
        } else if (!StringUtils.isEmpty(title)) {
            Page<Article> article = PageHelper.startPage(page, limit, sortBy).doSelectPage(() ->
                    articleService.search(title)
            );
            return RestResponse.ok(new Pagination<Article>(article));
        } else {
            Page<Article> article = PageHelper.startPage(page, limit, sortBy).doSelectPage(() ->
                    articleService.getAll()
            );
            return RestResponse.ok(new Pagination<Article>(article));
        }
    }

    @PostMapping("/add")
    public RestResponse add(
            @RequestParam(value = "title") String title,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "tags") String tags,
            @RequestParam(value = "category") String category,
            @RequestParam(value = "status", defaultValue = Types.DRAFT) String status,
            @RequestParam(value = "allowComment", defaultValue = "false") Boolean allowComment
    ) {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setTags(tags);
        article.setCategory(category);
        article.setStatus(status);
        article.setAllowComment(allowComment);

        articleService.insertSelective(article);
        return RestResponse.ok(article, 0, "添加成功");
    }

    @PostMapping("/update")
    public RestResponse update(
            @RequestParam(value = "id") Integer id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "tags", required = false) String tags,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "allowComment", required = false) Boolean allowComment
    ) {
        Article article = new Article();
        article.setId(id);
        article.setTitle(title);
        article.setContent(content);
        article.setTags(tags);
        article.setCategory(category);
        article.setStatus(status);
        article.setAllowComment(allowComment);
        article.setUpdatedAt(new Date());

        articleService.updateByPrimaryKeySelective(article);
        return RestResponse.ok(article, 0, "更新成功");
    }

    @DeleteMapping("/delete")
    public RestResponse delUser(
            @RequestParam(value = "ids") String ids
    ) {
        Map<String, String> map = new HashMap<>();
        map.put("ids", ids);
        articleService.del(map);
        return RestResponse.ok("删除成功");
    }
}