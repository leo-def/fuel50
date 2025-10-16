package org.fuel50.activity.controllers;

import org.fuel50.dtos.UserActivityDto;
import org.fuel50.activity.services.UserActivityService;
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
@RequestMapping(path = "/user-activity", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "UserActivity", description = "User-Activity link CRUD operations")
public class UserActivityController {
    private final UserActivityService service;
    public UserActivityController(UserActivityService service) { this.service = service; }

    @GetMapping("/{id}")
    @Operation(summary = "Get user-activity link by id")
    public Mono<UserActivityDto> get(@Parameter(description = "UserActivity id", example = "200") @PathVariable Long id) { return service.get(id); }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create user-activity link")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Link created",
            content = @Content(schema = @Schema(implementation = UserActivityDto.class)))
    })
    public Mono<UserActivityDto> create(@RequestBody UserActivityDto dto) { return service.createLink(dto); }

    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Patch user-activity link")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Link updated",
            content = @Content(schema = @Schema(implementation = UserActivityDto.class)))
    })
    public Mono<UserActivityDto> patch(@Parameter(description = "UserActivity id", example = "200") @PathVariable Long id,
                                       @RequestBody UserActivityDto dto) { return service.patch(id, dto); }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user-activity link")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Link deleted")
    })
    public Mono<Void> delete(@Parameter(description = "UserActivity id", example = "200") @PathVariable Long id) { return service.delete(id); }

    @GetMapping(path = "/by-user/{userId}")
    @Operation(summary = "List user-activity links by user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserActivityDto.class))))
    })
    public Flux<UserActivityDto> listByUser(@Parameter(description = "User id", example = "5") @PathVariable Long userId) { return service.listByUser(userId); }

    @GetMapping(path = "/by-activity/{activityId}")
    @Operation(summary = "List user-activity links by activity")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserActivityDto.class))))
    })
    public Flux<UserActivityDto> listByActivity(@Parameter(description = "Activity id", example = "100") @PathVariable Long activityId) { return service.listByActivity(activityId); }
}