package org.fuel50.clients;

import org.fuel50.dtos.CompanyDto;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class CompanyClient {
    private final WebClient webClient;

    public CompanyClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<CompanyDto> create(CompanyDto dto) {
        return webClient.post()
                .uri("/company")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(CompanyDto.class);
    }
}