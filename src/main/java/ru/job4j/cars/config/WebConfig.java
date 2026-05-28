package ru.job4j.cars.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private static final String UPLOAD_DIR = "uploads";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadLocation = Paths.get(UPLOAD_DIR)
                .toAbsolutePath()
                .normalize()
                .toUri()
                .toString();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(withTrailingSlash(uploadLocation));
    }

    private String withTrailingSlash(String value) {
        return value.endsWith("/") ? value : value + "/";
    }
}
