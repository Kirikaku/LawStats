package com.unihh.lawstats.core.config;

public class SolrProperties {

    private String solrHost;

    private String solrPort;

    public String getSolrHost() {
        return solrHost;
    }

    public String getSolrPort() {
        return solrPort;
    }

    public void setSolrHost(String solrHost) {
        this.solrHost = solrHost;
    }

    public void setSolrPort(String solrPort) {
        this.solrPort = solrPort;
    }
}
