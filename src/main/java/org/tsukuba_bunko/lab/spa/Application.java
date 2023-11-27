package org.tsukuba_bunko.lab.spa;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@SpringBootApplication
@EnableWebMvc
public class Application implements WebMvcConfigurer {
    @Autowired
    private WebProperties properties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
            .addResourceLocations(properties.getResources().getStaticLocations())
            .resourceChain(false)
            .addResolver(new Resolver());
    }

    private static class Resolver extends PathResourceResolver {
        @Override
        @Nullable
        protected Resource getResource(String resourcePath, Resource location) throws IOException {
            Resource resource = super.getResource(resourcePath, location);
            return resource != null ? resource : super.getResource("index.html", location);
        }
    }
   
    public static void main(String...args) {
        SpringApplication.run(Application.class, args);
    }
}
