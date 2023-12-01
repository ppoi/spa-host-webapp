package org.tsukuba_bunko.lab.spa;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@SpringBootApplication
@Configuration
public class Application implements WebMvcConfigurer {
    @Autowired
    private WebProperties properties;

    @Value("${spa.stage}")
    private String stage;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/env.json")
            .addResourceLocations(properties.getResources().getStaticLocations())
            .resourceChain(false)
            .addResolver(new EnvResolver());
        registry.addResourceHandler("/**")
            .addResourceLocations(properties.getResources().getStaticLocations())
            .resourceChain(false)
            .addResolver(new IndexResolver());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }

    private static class IndexResolver extends PathResourceResolver {
        @Override
        @Nullable
        protected Resource getResource(String resourcePath, Resource location) throws IOException {
            Resource resource = super.getResource(resourcePath, location);
            return resource != null ? resource : super.getResource("index.html", location);
        }
    }

    private class EnvResolver extends PathResourceResolver {
        @Override
        @Nullable
        protected Resource getResource(String resourcePath, Resource location) throws IOException {
            if(StringUtils.hasText(stage)) {
                resourcePath = "env_" + stage + ".json";
            }
            return super.getResource(resourcePath, location);
        }
    }

    public static void main(String...args) {
        new SpringApplicationBuilder()
            .bannerMode(Mode.OFF)
            .web(WebApplicationType.SERVLET)
            .sources(Application.class)
            .run(args);
    }
}
