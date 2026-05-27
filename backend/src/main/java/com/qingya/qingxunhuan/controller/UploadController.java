package com.qingya.qingxunhuan.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.qingya.qingxunhuan.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Value("${upload.path:./uploads}")
    private String uploadPath;

    @PostMapping("/image")
    public Result<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error(400, "文件不能为空");
        }
        String ext = FileUtil.extName(file.getOriginalFilename());
        if (!StrUtil.containsAny(ext, "jpg", "jpeg", "png", "gif", "webp")) {
            return Result.error(400, "不支持的图片格式");
        }
        String filename = UUID.randomUUID() + "." + ext;
        File dest = new File(uploadPath, filename);
        try {
            FileUtil.mkParentDirs(dest);
            file.transferTo(dest);
            String url = "/uploads/" + filename;
            return Result.success(Map.of("url", url));
        } catch (Exception e) {
            log.error("上传失败", e);
            return Result.error("上传失败");
        }
    }
}
