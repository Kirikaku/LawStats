package com.unihh.lawstats.backend;

import com.unihh.lawstats.backend.repositories.VerdictRepository;
import com.unihh.lawstats.core.config.SolrProperties;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

import java.text.ParseException;

@Configuration
@PropertySource("classpath:config/solr.properties")
@EnableSolrRepositories
@ComponentScan
public class AppConfig {

    @Autowired
    Environment environment;

    @Bean
    public SolrClient solrClient() {
        return new HttpSolrClient("http://localhost:8983/solr/verdict");
    }

    @Bean
    public SolrOperations solrTemplate() {
        return new SolrTemplate(solrClient());
    }

    @Bean
    public ImportTestData importTestData(VerdictRepository verdictRepository) throws ParseException {
        return new ImportTestData(verdictRepository);
    }
}
