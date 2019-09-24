package com.wht.blog.controller;

import com.wht.blog.util.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wht
 * @since 2019-09-22 2:45
 */

@RestController
@Slf4j
@RequestMapping("/manage/upload")
public class UploadController extends BaseController{

    @Resource
    private Environment environment;

    private final static String fileTempPath = "/upload/";

    @PostMapping("/local")
    public RestResponse local(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return RestResponse.fail("文件为空");
        }
        String realPath = "src/main/resources" + fileTempPath;
        File dir = new File(realPath);
        boolean filePathHave = false;
        if(!dir.isDirectory()){
            filePathHave = dir.mkdirs();
        }
        if (!filePathHave) {
            log.error("【创建文件目录】失败, 绝对路径：{}", realPath);
            return RestResponse.fail("文件上传失败");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String format = sdf.format(new Date());

        String fileName = file.getOriginalFilename();
        String newName = format + "-" + fileName;
        String localFilePath = dir.getAbsolutePath() + File.separator + newName;

        try {
            file.transferTo(new File(localFilePath));
        } catch (IOException e) {
            log.error("【文件上传至本地】失败，绝对路径：{}", localFilePath);
            return RestResponse.fail("文件上传失败");
        }
        String url = this.getServiceIp() + "/" + newName;
        log.info("【文件上传至本地】绝对路径：{}", localFilePath);
        return RestResponse.ok(url);
    }

    private String getServiceIp() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        assert address != null;
        return address.getHostAddress() + ":" + environment.getProperty("local.server.port");
    }

}