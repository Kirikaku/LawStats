package com.unihh.lawstats.backend.controller;

import com.unihh.lawstats.backend.repositories.VerdictRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Service
public class HelloWorldController {

    private final VerdictRepository verdictRepository;

    public HelloWorldController(VerdictRepository verdictRepository) {

        this.verdictRepository = verdictRepository;
    }

    @RequestMapping("/greeting")
    public String greeting() {
        final StringBuilder builder = new StringBuilder("Test");
        verdictRepository.findAllByRevisionSuccess(0).forEach(verdict -> builder.append(verdict.getDocketNumber() + "\n"));
        return builder.toString();
    }
}
