package com.wht.blog.controller;

import com.alibaba.excel.EasyExcel;
import com.wht.blog.entity.Meta;
import com.wht.blog.service.MetaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wht
 * @since 2019-10-05 5:32
 */
@RestController
@RequestMapping("/manage/download")
public class DownloadController {
    @Resource
    private MetaService metaService;

    @GetMapping("/meta")
    public void download(
            @RequestParam(value = "ids", required = false) String ids,
            HttpServletResponse response
    ) throws IOException {
        // 这里注意 有同学反应下载的文件名不对。这个时候 请别使用swagger 他会有影响
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String fileName = URLEncoder.encode("标签与分类", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), Meta.class).sheet("标签与分类").doWrite(downloadMeta(ids));
    }
    @GetMapping("/metaTemplate")
    public void downloadTemplate(
            HttpServletResponse response
    ) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("标签与分类", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        List<Meta> metas = new ArrayList<>();
        EasyExcel.write(response.getOutputStream(), Meta.class).sheet("标签与分类").doWrite(metas);
    }
    private List<Meta> downloadMeta(String ids) {
        if (StringUtils.isNotEmpty(ids)) {
            String[] idArr = ids.split(",");
            return metaService.search(idArr);
        } else {
            return metaService.getAll();
        }
    }
}
