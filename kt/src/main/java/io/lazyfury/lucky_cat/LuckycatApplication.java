package io.lazyfury.lucky_cat;

import io.lazyfury.lucky_cat.admin.ui.table.UserTable;
import io.lazyfury.lucky_cat.app.auth.repo.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@SpringBootApplication
@RestController
@ComponentScan
@EnableJpaRepositories
@EntityScan
public class LuckycatApplication {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${server.port}")
    private int port;

    public static void main(String[] args) {
        SpringApplication.run(LuckycatApplication.class, args);
    }

    @PostConstruct
    public void init() {
        System.out.println("Application name: " + appName);
        System.out.println("Server port: " + port);
        System.out.println("Server is running on http://localhost:" + port);
    }


    @GetMapping("/")
    public HashMap<String, Object> home() {
        var table = new UserTable();
        return new HashMap<String, Object>() {{
            put("message", "Hello World");
            put("table", table);
        }};
    }

}
