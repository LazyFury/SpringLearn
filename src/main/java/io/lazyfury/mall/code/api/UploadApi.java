package io.lazyfury.mall.code.api;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nonnull;
import lombok.val;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

@RestController
@Tag(name = "上传Api")
public class UploadApi {
    File path = new File(ResourceUtils.getURL("file:").getPath());
    File upload;

    public UploadApi() throws FileNotFoundException {
        upload = new File(path.getAbsolutePath(), "static/upload");
        if (!upload.exists()) {
            if (!upload.mkdirs()) {
                throw new Error("创建文件夹失败");
            }
        }
    }

    @PostMapping("/upload")
    public HashMap<String, Object> upload(@Nonnull @RequestPart MultipartFile file) throws IOException {
        //排除未知文件后缀和可执行文件
        val ext = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        if (ext.equals("exe") || ext.equals("bat") || ext.equals("sh") || ext.equals("cmd")) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("code", 400);
            map.put("msg", "上传失败");
            map.put("data", "不支持的文件类型");
            return map;
        }
        //仅支持视频 图片 音频类型 使用content type判断
        val contentType = file.getContentType();
        if (contentType == null || (!contentType.startsWith("image") && !contentType.startsWith("video") && !contentType.startsWith("audio"))) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("code", 400);
            map.put("msg", "上传失败");
            map.put("data", "不支持的文件类型");
            return map;
        }


        val f = new File(upload.getAbsolutePath(), Objects.requireNonNull(file.getOriginalFilename()));
        System.out.println(f.getAbsoluteFile());
        file.transferTo(f.getAbsoluteFile());
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "上传成功");
        map.put("data", "http://127.0.0.1:13587/static/upload/" + file.getOriginalFilename());
        return map;
    }
}
