package com.unihh.lawstats.backend.controller;

import com.unihh.lawstats.core.config.SolrProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Service
public class HelloWorldController {

    @Autowired
    private final SolrProperties solrProperties;

    public HelloWorldController(SolrProperties solrProperties) {

        this.solrProperties = solrProperties;
    }

    @RequestMapping("/greeting")
    public String greeting() {
        return "Hello World";
    }
}
