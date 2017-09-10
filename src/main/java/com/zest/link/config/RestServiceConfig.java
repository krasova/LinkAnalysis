package com.zest.link.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestServiceConfig {

    @Bean
    public HttpClient httpClient() {
        HttpClientBuilder builder = HttpClientBuilder.create();
        HttpClient httpClient  = builder.build();
        return httpClient;
    }

    @Bean
    public ClientHttpRequestFactory httpClientFactory() {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(httpClientFactory());
    }
}
