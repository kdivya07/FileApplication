package com.filemanagement.constants;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class UrlConstants {

    public static final String VALIDATE_API_URL = "http://localhost:8081/auth-service/validate?token=";

    public static String DOWNLOAD_URL(String attachmentId){
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(attachmentId)
                .toUriString();
    }

}
