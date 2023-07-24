package org.judeukkim.springbootKDJ.config;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class FileConfigClass implements WebMvcConfigurer {

    //File 업로드 로직
    String saveFiles="file:///C:/saveFiles/"; //외부에서 접근 허용할 폴더를 설정


    //addResourceHandlers 메서드는 웹 애플리케이션에서 정적 리소스의 위치와 URL 매핑을 설정하기 위해 사용됩니다.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 요청 URL
        registry.addResourceHandler("/upload/**")
                .addResourceLocations(saveFiles); // 폴더 접근권한 허용

    }
}
