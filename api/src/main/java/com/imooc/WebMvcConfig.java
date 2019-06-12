package com.imooc;

import com.imooc.controller.interceptor.MiniInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author daile.sun
 * @date 2018/10/8
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources")
                .addResourceLocations("file:C:/imooc_video_dev/")
                .addResourceLocations("classpath:/static/");

        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

    }

    @Bean
    public MiniInterceptor miniInterceptor(){
        return new MiniInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(miniInterceptor()).addPathPatterns("/user/**")
            .addPathPatterns("/bgm/**")
            .addPathPatterns("/video/upload","/video/uploadCover","/video/userLike","/video/userUnLike","/video/saveComment")
            .excludePathPatterns("/user/queryPublisher");
        super.addInterceptors(registry);
    }
}
