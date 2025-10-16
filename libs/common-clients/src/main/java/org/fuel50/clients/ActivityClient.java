package org.fuel50.clients;

import org.fuel50.dtos.ActivityDto;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ActivityClient {
    private final WebClient webClient;

    public ActivityClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<ActivityDto> createForCompany(Long companyId, ActivityDto dto) {
        return webClient.post()
                .uri("/company/{companyId}/activity", companyId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(ActivityDto.class);
    }
}