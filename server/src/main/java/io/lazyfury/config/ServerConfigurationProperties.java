package io.lazyfury.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "server")
public class ServerConfigurationProperties {
    private int port;
}
