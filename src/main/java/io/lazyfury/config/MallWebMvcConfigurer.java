package io.lazyfury.config;

import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MallWebMvcConfigurer implements WebMvcConfigurer {

    @Override
    public void addFormatters(@Nonnull FormatterRegistry registry) {
        System.out.println("MallWebMvcConfigurer.addFormatters 时间格式化");
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setUseIsoFormat(true);
        registrar.registerFormatters(registry);
        WebMvcConfigurer.super.addFormatters(registry);
    }
}
