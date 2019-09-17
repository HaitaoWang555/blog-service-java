package com.wht.blog.controller;

import com.wht.blog.entity.Meta;
import com.wht.blog.service.MetaService;
import com.wht.blog.util.RestResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
    public RestResponse list() {
        return RestResponse.ok(metaService.getAll());
    }

    @GetMapping("getOne")
    public RestResponse getOne(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "type", required = false) String type
    ) {
        if (!StringUtils.isEmpty(id)) {
            return RestResponse.ok(metaService.getOneById(id));
        } else if (!StringUtils.isEmpty(name) || !StringUtils.isEmpty(type)) {
            return RestResponse.ok(metaService.search(name, type));
        } else {
            return RestResponse.fail(1,"无效参数");
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

        metaService.updateByPrimaryKeySelective(meta);
        return RestResponse.ok("更新成功");
    }

    @DeleteMapping("/delete")
    public RestResponse delUser(@RequestParam(value = "id") int id) {
        metaService.del(id);
        return RestResponse.ok("删除成功");
    }

}
