package io.lazyfury;

import io.lazyfury.config.ServerConfigurationProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author suke
 * @apiNote Application
 */
//@EnableCaching
@EnableWebMvc
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Learn Project", description = "", extensions = {
        @io.swagger.v3.oas.annotations.extensions.Extension(name = "x-logo", properties = {
                @io.swagger.v3.oas.annotations.extensions.ExtensionProperty(name = "url", value = "https://avatars.githubusercontent.com/u/25139337?s=200&v=4")
        })
}))
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@SecurityRequirement(name = "bearerAuth")

@ComponentScan
public class Application {

    @Value("${server.port}")
    protected Long serverPort;

    @Autowired
    ServerConfigurationProperties configuration;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void after() {

        System.out.printf("server on http://localhost:%d\n", serverPort);
//        swager url
        System.out.println("swagger url: http://localhost:" + serverPort + "/swagger-ui/index.html");
    }


}

