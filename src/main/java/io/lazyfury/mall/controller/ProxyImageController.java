package io.lazyfury.mall.controller;


import jakarta.servlet.http.HttpServletResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ProxyImageController {

    OkHttpClient client = new OkHttpClient();

    @Cacheable(value = "image", key = "#url")
    @ResponseBody
    @GetMapping(value = "/proxy", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage(String url, HttpServletResponse response) throws IOException {
        var resp = client.newCall(new Request.Builder().url(url).build()).execute();
        if (resp.isSuccessful()) {
            System.out.println(resp.body());
            if (resp.body() != null) {
                response.setContentType("image/png");
                return resp.body().byteStream().readAllBytes();
            }
        }
        return new byte[0];
    }
}
