package com.unihh.lawstats.backend.controller;

import com.unihh.lawstats.backend.repository.VerdictRepoService;
import com.unihh.lawstats.stats.CourtStatistic;
import com.unihh.lawstats.stats.Greeting;
import com.unihh.lawstats.stats.StatisticsCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class StatsController {

    @Autowired
    private VerdictRepoService verdictRepoService;





    @RequestMapping("/stats/aggrcourts")
    public CourtStatistic requestStats() {


        CourtStatistic courtStatistic = new StatisticsCreator(verdictRepoService).createCourtStatistic();

        return courtStatistic;
    }


    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/stats/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }



}
