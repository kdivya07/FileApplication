package com.filemanagement.services;

import com.filemanagement.entities.Attachment;
import com.filemanagement.constants.ErrorConstants;
import com.filemanagement.exceptions.UnauthorizedException;
import com.filemanagement.repositories.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ValidationServiceImpl validationServiceImpl;

    @Override
    public Attachment saveAttachment(MultipartFile file, String token) throws Exception {
        logger.debug("Entering saveAttachment with fileName: {} and token: {}", file.getOriginalFilename(), token);
        if (!validationServiceImpl.isTokenValid(token)) {
            logger.warn("Token validation failed for token: {}", token);
            throw new UnauthorizedException(ErrorConstants.ERROR_UNAUTHORIZED);
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        if (fileName.contains("..")) {
            logger.error("Filename contains invalid path name: {}", fileName);
            throw new IllegalArgumentException(ErrorConstants.ERROR_INVALID_FILENAME + fileName);
        }
        try {
            Attachment attachment = new Attachment(fileName, file.getContentType(), file.getBytes());
            Attachment savedAttachment = fileRepository.save(attachment);
            logger.info("Successfully saved file: {}", fileName);
            return savedAttachment;

        } catch (IOException e) {
            logger.error("Could not read file bytes for file {}: {}", fileName, e.getMessage(), e);
            throw new IOException(ErrorConstants.ERROR_FILE_READ + fileName, e);
        } catch (Exception e) {
            logger.error("Could not save file {}", fileName, e.getMessage(), e);
            throw new RuntimeException(ErrorConstants.ERROR_FILE_SAVE + fileName, e);
        } finally {
            logger.debug("Exiting saveAttachment");
        }
    }

    @Override
    public Attachment getAttachment(String fileId, String token) throws Exception {
        logger.debug("Entering getAttachment for fileId: {} and token: {}", fileId, token);
        if (!validationServiceImpl.isTokenValid(token)) {
            logger.warn("Token validation failed for token:{}", token);
            throw new UnauthorizedException(ErrorConstants.ERROR_UNAUTHORIZED);
        }
        try {
            Attachment attachment = fileRepository.findById(fileId).orElseThrow(() -> new Exception(ErrorConstants.ERROR_FILE_NOT_FOUND + fileId));
            logger.info("Successfully saved file with id:{}", fileId);
            return attachment;
        } catch (Exception e) {
            logger.error("Error downloading file with id: {}", fileId);
            throw new RuntimeException(ErrorConstants.ERROR_FILE_NOT_FOUND + fileId, e);
        } finally {
            logger.debug("Exiting getAttachment.");
        }
    }
}
