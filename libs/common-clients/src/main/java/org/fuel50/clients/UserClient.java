package org.fuel50.clients;

import org.fuel50.dtos.UserDto;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class UserClient {
    private final WebClient webClient;

    public UserClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<UserDto> createForCompany(Long companyId, UserDto dto) {
        return webClient.post()
                .uri("/company/{companyId}/user", companyId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(UserDto.class);
    }
}