package io.lazyfury;

import io.lazyfury.config.SEOConfig;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import java.util.HashMap;
import java.util.List;

/**
 * @author suke
 * @apiNote Application
 */
//@EnableCaching
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Learn Project", description = ""))
@ComponentScan(basePackages = "io.lazyfury")
public class Application {

    @Value("${server.port}")
    protected Long serverPort;

    @Autowired
    ServerConfigurationProperties configuration;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void init() {

        System.out.printf("server on http://localhost:%d\n", serverPort);
//        swager url
        System.out.println("swagger url: http://localhost:" + serverPort + "/swagger-ui/index.html");
    }

    @Bean
    public SEOConfig seo(ThymeleafViewResolver viewResolver) {
        var seo = new SEOConfig("app name", "app description", List.of(new String[]{"test", "seo", "Spring boot"}), "author");
        if (viewResolver != null) {
            var map = new HashMap<String, Object>();
            map.put("site", seo);
            viewResolver.setStaticVariables(map);
        }
        return seo;
    }
}

@Component
@Data
@ConfigurationProperties(prefix = "server")
class ServerConfigurationProperties {
    private int port;
}

