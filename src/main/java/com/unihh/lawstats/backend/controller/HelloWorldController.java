package com.unihh.lawstats.backend.controller;

import com.unihh.lawstats.backend.repositories.VerdictRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Service
public class HelloWorldController {

    @Resource
    private final VerdictRepository productRepository;

    public HelloWorldController(VerdictRepository productRepository) {
        this.productRepository = productRepository;
    }

    @RequestMapping("/greeting")
    public String greeting() {
        StringBuilder builder = new StringBuilder();
        productRepository.findByRevisionSuccess(0).forEach(verdict -> builder.append(verdict.getDocketNumber()));
        return builder.toString();
    }
}
