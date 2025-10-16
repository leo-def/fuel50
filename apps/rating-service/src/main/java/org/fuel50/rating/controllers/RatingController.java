package org.fuel50.rating.controllers;

import org.fuel50.dtos.RatingDto;
import org.fuel50.rating.services.RatingService;
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
@RequestMapping(path = "/rating", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Rating", description = "Rating CRUD operations and queries")
public class RatingController {
    private final RatingService service;
    public RatingController(RatingService service) { this.service = service; }

    @GetMapping("/{id}")
    @Operation(summary = "Get rating by id")
    public Mono<RatingDto> get(@Parameter(description = "Rating id", example = "10") @PathVariable Long id) { return service.get(id); }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create rating")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Rating created",
            content = @Content(schema = @Schema(implementation = RatingDto.class)))
    })
    public Mono<RatingDto> create(@RequestBody RatingDto dto) { return service.create(dto); }

    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Patch rating")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Rating updated",
            content = @Content(schema = @Schema(implementation = RatingDto.class)))
    })
    public Mono<RatingDto> patch(@Parameter(description = "Rating id", example = "10") @PathVariable Long id, @RequestBody RatingDto dto) { return service.patch(id, dto); }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete rating")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Rating deleted")
    })
    public Mono<Void> delete(@Parameter(description = "Rating id", example = "10") @PathVariable Long id) { return service.delete(id); }

    @GetMapping(path = "/by-to-user-activity/{userActivityId}")
    @Operation(summary = "List ratings by target user-activity")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RatingDto.class))))
    })
    public Flux<RatingDto> listByToUserActivity(@Parameter(description = "UserActivity id (target)", example = "200") @PathVariable Long userActivityId) { return service.listByToUserActivity(userActivityId); }

    @GetMapping(path = "/by-from-user/{userId}")
    @Operation(summary = "List ratings by source user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RatingDto.class))))
    })
    public Flux<RatingDto> listByFromUser(@Parameter(description = "User id (source)", example = "5") @PathVariable Long userId) { return service.listByFromUser(userId); }
}