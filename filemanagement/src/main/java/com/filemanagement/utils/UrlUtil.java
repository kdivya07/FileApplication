package com.filemanagement.utils;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class UrlUtil {

    public static String getFileDownloadUrl(String attachmentId){
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(attachmentId)
                .toUriString();
    }

}
