package org.fuel50.clients;

import org.fuel50.dtos.UserActivityDto;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class UserActivityClient {
    private final WebClient webClient;

    public UserActivityClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<UserActivityDto> createLink(UserActivityDto dto) {
        return webClient.post()
                .uri("/user-activity")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(UserActivityDto.class);
    }
}