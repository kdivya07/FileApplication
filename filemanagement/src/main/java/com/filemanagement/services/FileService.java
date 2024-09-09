package com.filemanagement.services;

import com.filemanagement.entities.Attachment;
import org.springframework.web.multipart.MultipartFile;


public interface FileService {

    Attachment saveAttachment(MultipartFile file, String token) throws Exception;

    Attachment getAttachment(String fileId, String token) throws Exception;
}
