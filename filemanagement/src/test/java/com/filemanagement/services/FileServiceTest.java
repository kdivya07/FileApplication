package com.filemanagement.services;

import com.filemanagement.constants.ErrorConstants;
import com.filemanagement.entities.Attachment;
import com.filemanagement.exceptions.UnauthorizedException;
import com.filemanagement.repositories.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class FileServiceTest {

    @InjectMocks
    private FileServiceImpl fileService;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private ValidationServiceImpl validationServiceImpl;

    @Mock
    private MultipartFile file;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveAttachment_ValidToken_Success() throws Exception {
        String fileId = "fileId";
        String fileName ="file.txt";
        String token ="valid-token";
        byte[] fileBytes = "file-content".getBytes();
        when(file.getOriginalFilename()).thenReturn(fileName);
        when(file.getBytes()).thenReturn(fileBytes);
        when(validationServiceImpl.isTokenValid(token)).thenReturn(true);
        when(fileRepository.save(any(Attachment.class))).thenReturn(new Attachment(fileId, fileName, "text/plain", fileBytes));

        Attachment savedAttachment = fileService.saveAttachment(file, token);

        assertNotNull(savedAttachment);
        assertEquals(fileName, savedAttachment.getFileName());
        verify(fileRepository).save(any(Attachment.class));
    }

    @Test
    void saveAttachment_InvalidToken_ThrowsUnauthorizedException() throws Exception {
        String token = "invalid-token";
        when(validationServiceImpl.isTokenValid(token)).thenReturn(false);
        UnauthorizedException thrown = assertThrows(UnauthorizedException.class, () -> {
            fileService.saveAttachment(file, token);
        });
        assertEquals(ErrorConstants.UNAUTHORIZED, thrown.getMessage());
    }

    @Test
    void saveAttachment_InvalidFilename_ThrowsIllegalArgumentException() throws Exception {
        String token = "valid-token";
        String fileName = "../file.txt";
        when(file.getOriginalFilename()).thenReturn(fileName);
        when(validationServiceImpl.isTokenValid(token)).thenReturn(true);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            fileService.saveAttachment(file, token);
        });
        assertEquals(ErrorConstants.INVALID_FILENAME + fileName, thrown.getMessage());
    }

    @Test
    void saveAttachment_FileReadException_ThrowsIOException() throws Exception {
        String token = "valid-token";
        String fileName = "file.txt";
        when(file.getOriginalFilename()).thenReturn(fileName);
        when(validationServiceImpl.isTokenValid(token)).thenReturn(true);
        when(file.getBytes()).thenThrow(new IOException("Read error"));
        IOException thrown = assertThrows(IOException.class, () -> {
            fileService.saveAttachment(file, token);
        });
        assertEquals(ErrorConstants.ERROR_FILE_READ + fileName, thrown.getMessage());
    }

    @Test
    void getAttachment_ValidToken_Success() throws Exception {
        String token = "valid-token";
        String fileId = "file-id";
        Attachment attachment = new Attachment("file-id", "file.txt", "text/plain", "file content".getBytes());
        when(validationServiceImpl.isTokenValid(token)).thenReturn(true);
        when(fileRepository.findById(fileId)).thenReturn(Optional.of(attachment));
        Attachment retrievedAttachment = fileService.getAttachment(fileId, token);
        System.out.println("Retrieved Attachment: " + retrievedAttachment);
        assertNotNull(retrievedAttachment);
        assertEquals(fileId, retrievedAttachment.getFileId());
        verify(fileRepository).findById(fileId);
    }


    @Test
    void getAttachment_InvalidToken_ThrowsUnauthorizedException() throws Exception {
        String token = "invalid-token";
        String fileId = "file-id";
        when(validationServiceImpl.isTokenValid(token)).thenReturn(false);
        UnauthorizedException thrown = assertThrows(UnauthorizedException.class, () -> {
            fileService.getAttachment(fileId, token);
        });
        assertEquals(ErrorConstants.UNAUTHORIZED, thrown.getMessage());
    }

    @Test
    void getAttachment_FileNotFound_ThrowsRuntimeException() throws Exception {
        String token = "valid-token";
        String fileId = "file-id";
        when(validationServiceImpl.isTokenValid(token)).thenReturn(true);
        when(fileRepository.findById(fileId)).thenReturn(Optional.empty());
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            fileService.getAttachment(fileId, token);
        });
        assertEquals(ErrorConstants.FILE_NOT_FOUND + fileId, thrown.getMessage());
    }
}
