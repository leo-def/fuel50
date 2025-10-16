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
@RequestMapping(path = "/company/{companyId}/user/{userId}/activity/{userActivityId}/rating", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Rating", description = "Ratings nested under company/user/activity")
public class RatingNestedController {
    private final RatingService service;
    public RatingNestedController(RatingService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "List ratings for a user-activity within company")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RatingDto.class))))
    })
    public Flux<RatingDto> listToUserActivity(@Parameter(description = "UserActivity id (target)", example = "200") @PathVariable Long userActivityId) {
        return service.listByToUserActivity(userActivityId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create rating for a user-activity within company")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Rating created",
            content = @Content(schema = @Schema(implementation = RatingDto.class)))
    })
    public Mono<RatingDto> createForUserActivity(
            @Parameter(description = "User id (source)", example = "5") @PathVariable Long userId,
            @Parameter(description = "UserActivity id (target)", example = "200") @PathVariable Long userActivityId,
            @RequestBody RatingDto dto) {
        dto.setFromUserId(userId);
        dto.setToUserActivityId(userActivityId);
        return service.create(dto);
    }
}