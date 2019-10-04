package com.wht.blog.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wht.blog.dto.Pagination;
import com.wht.blog.entity.Meta;
import com.wht.blog.service.MetaService;
import com.wht.blog.util.Const;
import com.wht.blog.util.RestResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 属性(标签和分类)管理 Controller
 *
 * @author wht
 * @since 2019-09-16 16:32
 */
@RestController
@RequestMapping("/manage/metas")
public class MetaController extends BaseController {

    @Resource
    private MetaService metaService;

    @GetMapping("/list")
    public RestResponse searchList(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "list", required = false) String list,
            @RequestParam(value = "sortBy", required = false, defaultValue = "updated_at desc") String sortBy,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = Const.PAGE_SIZE) Integer limit
    ) {
        if (!StringUtils.isEmpty(id)) {
            return RestResponse.ok(metaService.getOneById(id));
        } else if (!StringUtils.isEmpty(name) || !StringUtils.isEmpty(type)) {
            Page<Meta> meta = PageHelper.startPage(page, limit, sortBy).doSelectPage(() ->
                    metaService.search(name, type)
            );
            return RestResponse.ok(new Pagination<Meta>(meta));
        } else if (!StringUtils.isEmpty(list) && list.equals("all")) {
            List<Meta> meta = metaService.getAll();
            return RestResponse.ok(new Pagination<Meta>(meta));
        } else {
            Page<Meta> meta = PageHelper.startPage(page, limit, sortBy).doSelectPage(() ->
                    metaService.getAll()
            );
            return RestResponse.ok(new Pagination<Meta>(meta));
        }
    }

    @PostMapping("/add")
    public RestResponse add(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "type") String type,
            @RequestParam(value = "color", required = false) String color,
            @RequestParam(value = "textColor", required = false) String text_color
    ) {
        Meta meta = new Meta();
        meta.setName(name);
        meta.setType(type);
        meta.setColor(color);
        meta.setTextColor(text_color);
        meta.setUpdatedAt(new Date());
        meta.setCreatedAt(new Date());

        metaService.insertSelective(meta);
        return RestResponse.ok(meta, 0, "添加成功");
    }

    @PostMapping("/update")
    public RestResponse update(
            @RequestParam(value = "id") Integer id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "color", required = false) String color,
            @RequestParam(value = "textColor", required = false) String text_color
    ) {
        Meta meta = new Meta();
        meta.setId(id);
        meta.setName(name);
        meta.setType(type);
        meta.setColor(color);
        meta.setTextColor(text_color);

        meta.setUpdatedAt(new Date());

        metaService.updateByPrimaryKeySelective(meta);
        return RestResponse.ok("更新成功");
    }

    @DeleteMapping("/delete")
    public RestResponse del(
            @RequestParam(value = "ids") String ids
    ) {
        Map<String, String> map = new HashMap<>();
        map.put("ids", ids);
        metaService.del(map);
        return RestResponse.ok("删除成功");
    }

}
