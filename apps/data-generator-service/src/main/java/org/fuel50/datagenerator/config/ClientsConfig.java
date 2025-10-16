package org.fuel50.datagenerator.config;

import org.fuel50.clients.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientsConfig {

    @Bean
    public CompanyClient companyClient(@Value("${clients.company.base-url}") String baseUrl) {
        return new CompanyClient(WebClient.builder().baseUrl(baseUrl).build());
    }

    @Bean
    public UserClient userClient(@Value("${clients.user.base-url}") String baseUrl) {
        return new UserClient(WebClient.builder().baseUrl(baseUrl).build());
    }

    @Bean
    public ActivityClient activityClient(@Value("${clients.activity.base-url}") String baseUrl) {
        return new ActivityClient(WebClient.builder().baseUrl(baseUrl).build());
    }

    @Bean
    public ActionClient actionClient(@Value("${clients.action.base-url}") String baseUrl) {
        return new ActionClient(WebClient.builder().baseUrl(baseUrl).build());
    }

    @Bean
    public UserActivityClient userActivityClient(@Value("${clients.user-activity.base-url}") String baseUrl) {
        return new UserActivityClient(WebClient.builder().baseUrl(baseUrl).build());
    }
}