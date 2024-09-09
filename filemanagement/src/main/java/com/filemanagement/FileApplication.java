package com.filemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class FileApplication {

    public static final Logger logger = LoggerFactory.getLogger(FileApplication.class);

    public static void main(String[] args) {
        logger.info("Application started.");
        SpringApplication.run(FileApplication.class, args);
        logger.info("Application started.");
    }

}
