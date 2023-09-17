package io.lazyfury.utils.qiniu;

import com.alibaba.fastjson.JSON;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nonnull;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

@Component
@RestController
@RequestMapping("/qiniu")
@Tag(name = "七牛云", description = "分片上传最小实例")
public class QiniuUploadApi {

    String accessKey = "miH88jbix_duzqqzLYlkJwOXDmKJosupodV2IV5N";
    String secretKey = "k9OhdxRmVDLJ9kTQhTQohjf3kEyeSpQZFilZ0-Yt";
    String bucketName = "test-suke";

    String baseUrl = "https://upload.qiniup.com";

    Configuration cfg;
    UploadManager uploadManager;

    OkHttpClient client = new OkHttpClient();
    String token;
    LocalDateTime tokenExpress = LocalDateTime.now().minusHours(1);
    Auth auth;


    @Autowired
    QiniuUploadApi() {
        System.out.println("initializing");
        cfg = new Configuration(Region.region0());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        uploadManager = new UploadManager(cfg);
    }

    public String getAuth() {
        if (LocalDateTime.now().isBefore(tokenExpress)) {
            System.out.println("old token");
            return token;
        }
        System.out.println("new token");

        var policy = new StringMap();
        policy.put("returnBody", "{\"key\":\"$(key)\",\"etag\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
        long expireSeconds = 3600 + 60;

        auth = Auth.create(accessKey, secretKey);
        token = auth.uploadToken(bucketName, null, expireSeconds, policy);
        tokenExpress = LocalDateTime.now().plusHours(1);
        return token;
    }

    @PostMapping("/part")
    public String uploadPart(@Nonnull InputStream inputStream, @RequestParam String uploadId, @RequestParam long partNumber, @RequestParam String objName) throws IOException {
        var bytes = inputStream.readAllBytes();
        System.out.println(bytes.length);
        var body = RequestBody.create(bytes);
        System.out.println(body);
        var req = new Request.Builder()
                .url("%s/buckets/%s/objects/%s/uploads/%s/%d".formatted(baseUrl, bucketName, objName, uploadId, partNumber))
                .addHeader("Content-Type", "application/octet-stream")
                .addHeader("Authorization", "UpToken " + token)
                .put(body).build();
        System.out.println(req);
        var res = client.newCall(req).execute();
        assert (res.body() != null);
        return res.body().string();
    }

    @GetMapping("/list")
    public String list(@RequestParam String uploadId, @RequestParam String objName) throws IOException {
        var req = new Request.Builder()
                .url("%s/buckets/%s/objects/%s/uploads/%s?part-number-marker=%s".formatted(baseUrl, bucketName, objName, uploadId, 99))
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "UpToken " + token)
                .get()
                .build();
        System.out.println(req);
        var res = client.newCall(req).execute();
        assert (res.body() != null);
        return res.body().string();
    }


    @PostMapping("/complete")
    public String complete(@org.springframework.web.bind.annotation.RequestBody ArrayList<Part> parts, @RequestParam String uploadId, @RequestParam String objName, @RequestParam String fName, @RequestParam String fType) throws IOException {
        var map = new HashMap<>();
        System.out.println(objName);
        map.put("parts", parts);
        map.put("token", token);
        /*map.put("mimeType",fType);*/
        /*map.put("fname",fName);*/

        var body = JSON.toJSONString(map);
        System.out.println(body);
        var req = new Request.Builder()
                .url("%s/buckets/%s/objects/%s/uploads/%s".formatted(baseUrl, bucketName, objName, uploadId))
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "UpToken " + token)
                .post(RequestBody.create(body.getBytes())).build();

        var res = client.newCall(req).execute();
        assert (res.body() != null);
        return res.body().string();
    }

    @GetMapping("/init")
    public HashMap<String, Object> initialize(@RequestParam String fName, @RequestParam String fType) throws IOException {
        var token = getAuth();
        /*var objName = UUID.randomUUID().toString();*/
        var objNameBase64 = Base64.getEncoder().encodeToString(fName.getBytes());
        var req = new Request.Builder()
                .url("%s/buckets/%s/objects/%s/uploads".formatted(baseUrl, bucketName, objNameBase64))
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "UpToken " + token)
                .post(RequestBody.create(JSON.toJSONBytes(new HashMap<>()))).build();

        System.out.println(req);
        var res = client.newCall(req).execute();
        assert res.body() != null;
        var map = new HashMap<String, Object>();
        map.put("name", objNameBase64);
        map.put("data", JSON.parse(res.body().string()));
        return map;
    }
}

