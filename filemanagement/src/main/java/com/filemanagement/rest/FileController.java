package com.filemanagement.rest;

import com.filemanagement.utils.UrlUtil;
import com.filemanagement.entities.Attachment;
import com.filemanagement.model.ResponseData;
import com.filemanagement.services.FileService;
import com.filemanagement.utils.FileDownloadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
public class FileController {

    public static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @Autowired
    private Attachment attachment;

    @PostMapping("/upload")
    public ResponseData uploadFile(@RequestParam("file") MultipartFile file, @RequestParam String token) throws Exception {
        logger.debug("Received upload request for file: {} with token: {}", file.getOriginalFilename(), token);
        if(file.isEmpty()){
            logger.warn("Attempted to upload an empty file.");
            throw new IllegalArgumentException("File is empty");
        }
        attachment = fileService.saveAttachment(file, token);
        logger.info("File uploaded successfully: {}", file.getOriginalFilename());
        String downloadUrl = UrlUtil.getFileDownloadUrl(attachment.getFileId());
        logger.debug("Generated download URL: {}", downloadUrl);
        return new ResponseData(attachment.getFileName(), downloadUrl, file.getContentType(), file.getSize());
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId, @RequestParam String token) throws Exception {
        logger.debug("Received download request for fileID: {} with token: {}", fileId, token);
        try {
            Attachment attachment = fileService.getAttachment(fileId, token);
            logger.info("File downloaded successfully: {}", attachment.getFileName());
            return FileDownloadUtil.createFileDownloadResponse(attachment);
        } catch (Exception e) {
            logger.error("Error downloading the file with fileId: {}", fileId, e);
            return FileDownloadUtil.handleDownloadError(fileId, e);
        }
    }
}

