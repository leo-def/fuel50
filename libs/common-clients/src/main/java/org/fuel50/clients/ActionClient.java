package org.fuel50.clients;

import org.fuel50.dtos.ActionDto;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ActionClient {
    private final WebClient webClient;

    public ActionClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<ActionDto> createForCompanyAndActivity(Long companyId, Long activityId, ActionDto dto) {
        return webClient.post()
                .uri("/company/{companyId}/activity/{activityId}/action", companyId, activityId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(ActionDto.class);
    }
}