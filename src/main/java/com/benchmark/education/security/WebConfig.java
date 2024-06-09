package com.benchmark.education.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${protected-file-location}")
    private String protectedFileLocation;

    @Value("${manual-file-location}")
    private String manualFileLocation;

    @Value("${public-file-location}")
    private String publicFIleLocation;

    @Value("${react-file-location}")
    private String reactFileLocation;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/files/**") // Define the URL path to access the files
//                .addResourceLocations("file:/home/master-x/testing_benchmark/"); // Specify the local filesystem path

        registry.addResourceHandler("/files/public/**") // Define the URL path to access the files
                .addResourceLocations("file:"+publicFIleLocation); // Specify the local filesystem path

        registry.addResourceHandler("/files/protected/**") // Define the URL path to access the files
                .addResourceLocations("file:"+protectedFileLocation); // Specify the local filesystem path

        registry.addResourceHandler("/files/manual/**") // Define the URL path to access the files
                .addResourceLocations("file:"+manualFileLocation); // Specify the local filesystem path



    }



    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Forward root path to index.html
        registry.addViewController("/")
                .setViewName("forward:/index.html");
        // Forward /admin-panel path to index.html
        registry.addViewController("/admin-panel/**")
                .setViewName("forward:/index.html");
    }



}
