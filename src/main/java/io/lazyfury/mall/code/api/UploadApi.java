package io.lazyfury.mall.code.api;


import jakarta.servlet.http.HttpServletRequest;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UploadApi {
    @Autowired
    private HttpServletRequest request;

    File path = new File(ResourceUtils.getURL("file:").getPath());
    File upload;

    public UploadApi() throws FileNotFoundException {
        upload = new File(path.getAbsolutePath(), "static/upload");
        if (!upload.exists()) {
            var _ok = upload.mkdirs();
        }
    }

    @PostMapping("/upload")
    public HashMap<String, Object> upload(@RequestPart MultipartFile file) throws IOException {
        val f = new File(upload.getAbsolutePath(), Objects.requireNonNull(file.getOriginalFilename()));
        System.out.println(f.getAbsoluteFile());
        file.transferTo(f.getAbsoluteFile());
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "上传成功");
        map.put("data", "http://127.0.0.1:8080/static/upload/" + file.getOriginalFilename());
        return map;
    }
}
