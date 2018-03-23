package com.unihh.lawstats.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

    /**
     * Links to the home page
     */
    @GetMapping("/")
    public String home(Model model) {
        return "index";
    }

    /**
     * Links to the upload page
     */
    @GetMapping("/upload")
    public String upload(Model model) {
        return "upload";
    }

    /**
     * Links to the stats page
     */
    @GetMapping("/stats")
    public String stats(Model model) {
        return "stats";
    }
}
