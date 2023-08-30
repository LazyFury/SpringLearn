package io.lazyfury.mall.code.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
class CorsConfig {
    private CorsConfiguration buildConfig() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*"); //允许任何域名
        config.addAllowedHeader("*"); //允许任何头
        config.addAllowedMethod("*"); //允许任何方法
        return config;
    }


    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig()); //注册
        return new CorsFilter(source);
    }
}
