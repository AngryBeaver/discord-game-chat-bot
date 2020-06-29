package net.verplanmich.bot.website;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/alienencounter/**")
                .addResourceLocations("/alienencounter/");
        registry
                .addResourceHandler("/clank/**")
                .addResourceLocations("/clank/");
        registry
                .addResourceHandler("/zombicide/**")
                .addResourceLocations("/zombicide/");
        registry
                .addResourceHandler("/waterdeep/**")
                .addResourceLocations("/waterdeep/");
        registry
                .addResourceHandler("/spiritisland/**")
                .addResourceLocations("/spiritisland/");
    }

    @Bean
    public WebMvcConfigurerAdapter forwardToIndex() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/zombicide/").setViewName(
                        "forward:/zombicide/index.html");
                registry.addViewController("/alienencounter/").setViewName(
                        "forward:/alienencounter/index.html");
                registry.addViewController("/clank/").setViewName(
                        "forward:/clank/index.html");
                registry.addViewController("/waterdeep/").setViewName(
                        "forward:/waterdeep/index.html");
                registry.addViewController("/spiritisland/").setViewName(
                        "forward:/spiritisland/index.html");
            }
        };
    }
}