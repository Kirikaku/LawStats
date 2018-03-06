package com.unihh.lawstats.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

    @GetMapping("/")
    public String home(Model model) {
        return "index";
    }

    @GetMapping("/upload")
    public String upload(Model model) {
        return "upload";
    }

    @GetMapping("/stats")
    public String stats(Model model) {
        return "stats";
    }
}
