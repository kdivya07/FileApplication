package com.filemanagement.services;

import org.springframework.web.client.RestClientException;

public interface ValidationService {

    boolean isTokenValid(String token) throws RestClientException;

}
