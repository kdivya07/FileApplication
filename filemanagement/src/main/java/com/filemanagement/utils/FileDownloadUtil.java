package com.filemanagement.utils;

import com.filemanagement.entities.Attachment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileDownloadUtil {

    public static final Logger logger = LoggerFactory.getLogger(FileDownloadUtil.class);

    public static ResponseEntity<Resource> createFileDownloadResponse(Attachment attachment) {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFileName() + "\"")
                .body(new ByteArrayResource(attachment.getData()));
    }

    public static ResponseEntity<Resource> handleDownloadError(String fileId, Exception e) {
        logger.error("Error downloading the file with fileId: {}", fileId, e);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
