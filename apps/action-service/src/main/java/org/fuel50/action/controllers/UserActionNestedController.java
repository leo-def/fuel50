package org.fuel50.action.controllers;

import org.fuel50.dtos.UserActionDto;
import org.fuel50.action.services.UserActionService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping(path = "/company/{companyId}/user/{userId}/action/{actionId}/user-action", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "UserAction", description = "User-Action nested under company/user/action")
public class UserActionNestedController {
    private final UserActionService service;
    public UserActionNestedController(UserActionService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "List user-action links for user and action")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserActionDto.class))))
    })
    public Flux<UserActionDto> list(@Parameter(description = "User id", example = "5") @PathVariable Long userId,
                                    @Parameter(description = "Action id", example = "100") @PathVariable Long actionId) {
        return service.listByUser(userId).filter(ua -> ua.getActionId() != null && ua.getActionId().equals(actionId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create user-action link for user and action")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User-action link created",
            content = @Content(schema = @Schema(implementation = UserActionDto.class)))
    })
    public Mono<UserActionDto> create(@Parameter(description = "User id", example = "5") @PathVariable Long userId,
                                      @Parameter(description = "Action id", example = "100") @PathVariable Long actionId,
                                      @RequestBody UserActionDto dto) {
        dto.setUserId(userId);
        dto.setActionId(actionId);
        return service.create(dto);
    }
}