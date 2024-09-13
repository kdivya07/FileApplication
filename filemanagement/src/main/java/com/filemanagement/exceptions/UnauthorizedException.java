package com.filemanagement.exceptions;

import lombok.Data;

@Data
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}