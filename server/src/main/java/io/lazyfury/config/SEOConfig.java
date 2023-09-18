package io.lazyfury.config;

import io.lazyfury.utils.SEOInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import java.util.HashMap;
import java.util.List;

@Component
public class SEOConfig {
    @Bean
    public SEOInfo seo(ThymeleafViewResolver viewResolver) {
        var seo = new SEOInfo("Arc Mall", "app description", List.of(new String[]{"test", "seo", "Spring boot"}), "author");
        if (viewResolver != null) {
            var map = new HashMap<String, Object>();
            map.put("site", seo);
            viewResolver.setStaticVariables(map);
        }
        return seo;
    }
}
