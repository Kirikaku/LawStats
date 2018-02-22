package com.unihh.lawstats.backend.controller;

import com.unihh.lawstats.backend.repositories.VerdictRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Service
public class HelloWorldController {

    public HelloWorldController() {}

    @RequestMapping("/greeting")
    public String greeting() {
        final StringBuilder builder = new StringBuilder("Test");
        return builder.toString();
    }
}
