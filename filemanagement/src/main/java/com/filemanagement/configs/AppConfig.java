package com.filemanagement.configs;

import com.filemanagement.entities.Attachment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Attachment attachment() {
        return new Attachment();
    }
}
