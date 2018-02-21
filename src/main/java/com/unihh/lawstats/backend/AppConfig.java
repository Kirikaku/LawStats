package com.unihh.lawstats.backend;

import com.unihh.lawstats.core.config.SolrProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:config/solr.properties")
public class AppConfig {

    @Autowired
    Environment environment;

    @Bean
    SolrProperties appProperties() {
        SolrProperties bean = new SolrProperties();
        bean.setSolrHost(environment.getProperty("solr.host"));
        bean.setSolrPort(environment.getProperty("solr.port"));
        return bean;
    }
}
