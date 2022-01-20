package com.ncuz.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * Created by rajeevkumarsingh on 02/08/17.
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final long MAX_AGE_SECS = 3600;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS", "PATCH")
                .allowedHeaders("*","Access-Control-Allow-Origin", "Access-Control-Allow-Headers","Origin", "Content-Type", "Accept", "Content-type", "x-requested-with", "x-requested-by") //What is this for?
                .allowCredentials(true)
                .maxAge(MAX_AGE_SECS);
    }

//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("forward:/index.html");
//    }
//    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
//            "classpath:/META-INF/resources/","classpath:/resources/",
//            "classpath:/BOOT-INF/classes/public/",
//             "classpath:/resources/public/",
//              "classpath:/public/",
//            "classpath:/BOOT-INF/classes/static/",
//            "classpath:/resources/static/",
//            "classpath:/static/"};
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//
//        registry.addResourceHandler("**/**")
//                .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
//
//    }


    
    
}
