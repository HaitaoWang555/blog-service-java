package com.wht.blog.controller;

import com.wht.blog.util.Method;
import com.wht.blog.util.RestResponse;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

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

        String realPath =  Method.createFilePath("article", this.getLoginUserId().toString());
        String thumbnail = "thumbnail/";
        File dir =  new File(realPath);

        if(!dir.isDirectory()){
            dir.mkdirs();
        }

        String fileName = file.getOriginalFilename();
        String localFilePath = dir.getAbsolutePath() + File.separator + fileName;
        String thumbnailPath = dir.getAbsolutePath() + File.separator + thumbnail;
        File thumbnailDir =new File(thumbnailPath);
        if(!thumbnailDir.isDirectory()){
            thumbnailDir.mkdirs();
        }
        String thumbnailFilePath =thumbnailPath + fileName;
        try {
            file.transferTo(new File(localFilePath));
            imgThumbnail(localFilePath, thumbnailFilePath);
        } catch (IOException e) {
            return RestResponse.fail("文件上传失败");
        }
        String url = file.getContentType().equals("image/jpeg")
                ? "/" + realPath.concat(thumbnail) + fileName
                : "/" + realPath.concat(thumbnail) + fileName + ".jpg";
        log.info("【文件上传至本地】绝对路径：{}", localFilePath);
        return RestResponse.ok(url, "上传成功");
    }

    private void imgThumbnail(String localFilePath, String thumbnailFilePath) throws IOException {
        Thumbnails.of(localFilePath)
                .scale(1)
                .outputQuality(0.5)
                .outputFormat("jpg")
                .toFile(thumbnailFilePath);
    }

}
