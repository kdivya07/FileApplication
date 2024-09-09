package com.filemanagement.utils;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class UrlUtil {

    public static final String VALIDATE_API_URL = "http://localhost:8081/auth-service/validate?token=";

    public static String downloadUrl(String attachmentId){
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(attachmentId)
                .toUriString();
    }

}
