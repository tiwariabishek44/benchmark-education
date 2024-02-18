package com.benchmark.education.security;

import org.springframework.context.annotation.Configuration;
        import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
        import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/files/**") // Define the URL path to access the files
                .addResourceLocations("file:/home/master-x/testing_benchmark/"); // Specify the local filesystem path
    }
}
