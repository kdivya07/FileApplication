package com.filemanagement.services;

import com.filemanagement.constants.AppConstants;
import com.filemanagement.constants.ErrorConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class ValidationServiceImpl implements ValidationService {

    private static final Logger logger = LoggerFactory.getLogger(ValidationServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    public boolean isTokenValid(String token) throws RestClientException {
        logger.debug("Validating token: {}", token);
        String url = AppConstants.VALIDATE_API_URL + token;
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            logger.info("Token is valid.");
            return true;
        } else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            logger.warn("Token is unauthorised.");
            return false;
        } else {
            logger.error("Invalid token response status: {}", response.getStatusCode());
            throw new RestClientException(ErrorConstants.UNKNOWN_ERROR + response.getStatusCode());
        }
    }

}
