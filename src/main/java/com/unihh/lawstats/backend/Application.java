package com.unihh.lawstats.backend;

import com.unihh.lawstats.backend.service.storage.StorageProperties;
import com.unihh.lawstats.backend.service.storage.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
@Slf4j
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        log.info("Start application, version 1");
        SpringApplication.run(Application.class, args);
    }


    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();

        };
    }
}