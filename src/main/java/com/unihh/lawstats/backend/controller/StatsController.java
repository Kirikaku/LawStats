package com.unihh.lawstats.backend.controller;

import com.unihh.lawstats.backend.repository.VerdictRepoService;
import com.unihh.lawstats.core.model.Verdict;
import com.unihh.lawstats.stats.AttributeStatistic;
import com.unihh.lawstats.stats.Greeting;
import com.unihh.lawstats.stats.StatisticsCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.query.result.SolrResultPage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class StatsController {

    @Autowired
    private VerdictRepoService verdictRepoService;

    private StatisticsCreator statisticsCreator;




    @RequestMapping("/stats/aggrcourts")
    public AttributeStatistic requestStats(@RequestParam(value="attribute", defaultValue="hallo") String attribute) {
        if(statisticsCreator == null){
           SolrResultPage resultsPage = (SolrResultPage) verdictRepoService.findAll().iterator().next();
           List<Verdict> allVerdicts = resultsPage.getContent();

           statisticsCreator = new StatisticsCreator(allVerdicts);
        }

        AttributeStatistic attributeStatistic = statisticsCreator.createAttributeStatistic(attribute);

        return attributeStatistic;
    }




    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/stats/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

}
