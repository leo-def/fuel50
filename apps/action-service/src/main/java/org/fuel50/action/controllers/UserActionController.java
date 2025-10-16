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
@RequestMapping(path = "/user-action", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "UserAction", description = "User-Action CRUD operations and queries")
public class UserActionController {
    private final UserActionService service;
    public UserActionController(UserActionService service) { this.service = service; }

    @GetMapping("/{id}")
    @Operation(summary = "Get user-action link by id")
    public Mono<UserActionDto> get(@Parameter(description = "UserAction id", example = "10") @PathVariable Long id) { return service.get(id); }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create user-action link")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User-action link created",
            content = @Content(schema = @Schema(implementation = UserActionDto.class)))
    })
    public Mono<UserActionDto> create(@RequestBody UserActionDto dto) { return service.create(dto); }

    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Patch user-action link")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User-action link updated",
            content = @Content(schema = @Schema(implementation = UserActionDto.class)))
    })
    public Mono<UserActionDto> patch(@Parameter(description = "UserAction id", example = "10") @PathVariable Long id, @RequestBody UserActionDto dto) { return service.patch(id, dto); }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user-action link")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User-action link deleted")
    })
    public Mono<Void> delete(@Parameter(description = "UserAction id", example = "10") @PathVariable Long id) { return service.delete(id); }

    @GetMapping(path = "/by-user/{userId}")
    @Operation(summary = "List user-action links by user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserActionDto.class))))
    })
    public Flux<UserActionDto> listByUser(@Parameter(description = "User id", example = "5") @PathVariable Long userId) { return service.listByUser(userId); }

    @GetMapping(path = "/by-action/{actionId}")
    @Operation(summary = "List user-action links by action")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserActionDto.class))))
    })
    public Flux<UserActionDto> listByAction(@Parameter(description = "Action id", example = "100") @PathVariable Long actionId) { return service.listByAction(actionId); }
}