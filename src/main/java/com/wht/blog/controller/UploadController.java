package com.wht.blog.controller;

import com.wht.blog.util.Method;
import com.wht.blog.util.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wht
 * @since 2019-09-22 2:45
 */

@RestController
@Slf4j
@RequestMapping("/api/manage/upload")
public class UploadController extends BaseController{

    @PostMapping("/local")
    public RestResponse local(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return RestResponse.fail("文件为空");
        }

        String realPath =  Method.createFilePath("article");
        File dir =  new File(realPath);

        boolean filePathHave = true;
        if(!dir.isDirectory()){
            filePathHave = dir.mkdirs();
        }
        if (!filePathHave) {
            log.error("【创建文件目录】失败, 绝对路径：{}", realPath);
            return RestResponse.fail("文件上传失败");
        }

        String fileName = file.getOriginalFilename();
        String localFilePath = dir.getAbsolutePath() + File.separator + fileName;

        try {
            file.transferTo(new File(localFilePath));
        } catch (IOException e) {
            log.error("【文件上传至本地】失败，绝对路径：{}", localFilePath);
            return RestResponse.fail("文件上传失败");
        }
        String url = "/" + realPath + fileName;
        log.info("【文件上传至本地】绝对路径：{}", localFilePath);
        return RestResponse.ok(url, "上传成功");
    }

}
