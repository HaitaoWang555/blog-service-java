package com.wht.blog.controller;

import com.google.common.base.Joiner;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wht.blog.dto.Pagination;
import com.wht.blog.entity.Article;
import com.wht.blog.service.ArticleService;
import com.wht.blog.service.MetaService;
import com.wht.blog.util.Const;
import com.wht.blog.util.RestResponse;
import com.wht.blog.util.Types;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wht
 * @since 2019-09-19 15:03
 */
@RestController
@RequestMapping("/api/manage/article")
public class ArticleController extends BaseController{
    @Resource
    private ArticleService articleService;
    @Resource
    private MetaService metasService;

    @GetMapping("/list")
    public RestResponse searchList(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "authorId", required = false) Integer authorId,
            @RequestParam(value = "meta", required = false) String meta,
            @RequestParam(value = "sortBy", required = false, defaultValue = "updated_at desc") String sortBy,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = Const.PAGE_SIZE) Integer limit
    ) {

        if (id!=null) {
            return RestResponse.ok(articleService.getOneById(id));
        } else if (!StringUtils.isAllBlank(title, status, type, meta) || authorId!=null || sortBy.equals("updated_at desc")) {
            Page<Article> article = PageHelper.startPage(page, limit, sortBy).doSelectPage(() ->
                    articleService.search(title, status, type, authorId, meta)
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
            @RequestParam(value = "type", defaultValue = Types.POST) String type,
            @RequestParam(value = "allowComment", defaultValue = "false") Boolean allowComment
    ) {
        this.insert(title, content, tags, category, status, type, allowComment);
        return RestResponse.ok("添加成功");
    }

    @PostMapping("/update")
    public RestResponse update(
            @RequestParam(value = "id") Integer id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "tags", required = false) String tags,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "allowComment", required = false) Boolean allowComment
    ) {
        Article article = new Article();
        article.setId(id);
        article.setTitle(title);
        article.setContent(content);
        article.setTags(tags);
        article.setCategory(category);
        article.setStatus(status);
        article.setType(type);
        article.setAllowComment(allowComment);
        article.setUpdatedAt(new Date());

        int update = articleService.updateByPrimaryKeySelective(article);
        if (update != 0) {
            if (StringUtils.isNotBlank(tags)) metasService.saveOrRemoveMeta(tags, Types.TAG, article.getId());
            if (StringUtils.isNotBlank(category)) metasService.saveOrRemoveMeta(category, Types.CATEGORY, article.getId());
        }
        return RestResponse.ok("更新成功");
    }

    @DeleteMapping("/delete")
    public RestResponse del(
            @RequestParam(value = "ids") String ids
    ) {
        Map<String, String> map = new HashMap<>();
        map.put("ids", ids);
        articleService.del(map);
        return RestResponse.ok("删除成功");
    }

    @PostMapping("/upload")
    public RestResponse upload(
            @RequestParam(value = "file") MultipartFile file
    ) throws IOException {
        InputStream inputStream = file.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        List<String> list = reader.lines().collect(Collectors.toList());
        String content = Joiner.on("\n").join(list);
        return RestResponse.ok(content,"导入成功");
    }

    private void insert(String title, String content, String tags, String category, String status, String type, Boolean allowComment) {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setTags(tags);
        article.setCategory(category);
        article.setStatus(status);
        article.setType(type);
        article.setAllowComment(allowComment);
        articleService.insertSelective(article);
        //存储分类和标签
        if (StringUtils.isNotBlank(tags)) metasService.saveOrRemoveMeta(tags, Types.TAG, article.getId());
        if (StringUtils.isNotBlank(category)) metasService.saveOrRemoveMeta(category, Types.CATEGORY, article.getId());
    }

}
