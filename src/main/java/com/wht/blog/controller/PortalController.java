package com.wht.blog.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wht.blog.dto.Archives;
import com.wht.blog.dto.Pagination;
import com.wht.blog.entity.Article;
import com.wht.blog.service.ArticleService;
import com.wht.blog.service.MetaService;
import com.wht.blog.util.Const;
import com.wht.blog.util.RestResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 博客前台 controller
 *
 * @author wht
 * @since 2019-09-22 22:21
 */
@RestController
@RequestMapping("/portal")
public class PortalController {
    @Resource
    private ArticleService articleService;
    @Resource
    private MetaService metaService;

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
        return RestResponse.ok(metaService.getMetaDto("tag"));
    }

    @GetMapping("/category/list")
    public RestResponse getCategoryList() {
        return RestResponse.ok();
    }

}
