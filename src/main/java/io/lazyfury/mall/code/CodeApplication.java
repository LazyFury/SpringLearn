package io.lazyfury.mall.code;

import io.lazyfury.mall.code.config.SEOConfig;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author suke
 * @name Application
 */
@EnableCaching
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Learn Project",description = "这份文档故意留下的，你可以做任何你喜欢的，在不侵犯其他用户权益的前提下"))
public class CodeApplication {

	@Value("${server.port}")
	protected Long serverPort;

	@Autowired
	ServerConfigurationProperties configuration;

	public static void main(String[] args) {
		SpringApplication.run(CodeApplication.class, args);
	}

	@PostConstruct
	public void init(){
		System.out.printf("server on http://localhost:%d\n", serverPort);
	}

	@Bean
	public SEOConfig seo(ThymeleafViewResolver viewResolver){
		var seo =  new SEOConfig("app name","app description" , List.of(new String[]{"test","seo","Spring boot"}),"author");
		if(viewResolver!=null){
			var map = new HashMap<String,Object>();
			map.put("site",seo);
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

