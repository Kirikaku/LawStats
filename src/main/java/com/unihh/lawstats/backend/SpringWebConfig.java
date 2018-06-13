package com.unihh.lawstats.backend;

import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
@ComponentScan
@PropertySource("classpath:config/lawstats.properties")
@EnableSolrRepositories
@Slf4j

public class SpringWebConfig implements ApplicationContextAware {

    private final Environment environment;
    private ApplicationContext applicationContext;

    @Autowired
    public SpringWebConfig(Environment environment) {
        this.environment = environment;
    }

    @Override

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public SolrClient solrClient() {
        log.info("Create HttpSolrClient with following URL {}", environment.getProperty("solr.address"));
        return new HttpSolrClient.Builder()
                .withBaseSolrUrl(environment.getProperty("solr.address"))
                .build();
    }

    @Bean
    public SolrOperations solrTemplate() {
        log.info("Put SolrClient into the SolrTemplte Object");
        return new SolrTemplate(solrClient());
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("Messages");
        return messageSource;
    }

    // View Resolver
    @Bean
    public ThymeleafViewResolver htmlViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine(htmlTemplateResolver()));
        resolver.setContentType("text/html");
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }

    @Bean
    public ThymeleafViewResolver javascriptViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine(javascriptTemplateResolver()));
        resolver.setContentType("application/javascript");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setViewNames(new String[]{"*.js"});
        return resolver;
    }

    @Bean
    public ThymeleafViewResolver cssViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine(cssTemplateResolver()));
        resolver.setContentType("text/css");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setViewNames(new String[]{"*.css"});
        return resolver;
    }


    //Template Engine
    private SpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver);
        engine.setEnableSpringELCompiler(true);
        return engine;
    }

    //Template Resolver
    private SpringResourceTemplateResolver htmlTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(this.applicationContext);
        resolver.setSuffix(".html");
        resolver.setCacheable(false);
        resolver.setTemplateMode(TemplateMode.HTML);
        return resolver;
    }

    private SpringResourceTemplateResolver javascriptTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix("/static/js/");
        resolver.setCacheable(false);
        resolver.setTemplateMode(TemplateMode.JAVASCRIPT);
        return resolver;
    }

    private SpringResourceTemplateResolver cssTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix("/static/css/");
        resolver.setSuffix(".css");
        resolver.setCacheable(false);
        resolver.setTemplateMode(TemplateMode.CSS);
        return resolver;
    }
}

