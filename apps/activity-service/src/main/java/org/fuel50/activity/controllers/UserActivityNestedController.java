package org.fuel50.activity.controllers;

import org.fuel50.dtos.UserActivityDto;
import org.fuel50.activity.services.UserActivityService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping(path = "/company/{companyId}/user/{userId}/activity/{activityId}/user-activity", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "UserActivity", description = "Nested user-activity endpoints")
public class UserActivityNestedController {
    private final UserActivityService service;
    public UserActivityNestedController(UserActivityService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Get link between user and activity")
    public Mono<UserActivityDto> getLink(@Parameter(description = "User id", example = "5") @PathVariable Long userId,
                                         @Parameter(description = "Activity id", example = "100") @PathVariable Long activityId) {
        return service.findLink(userId, activityId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create link between user and activity")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Link created",
            content = @Content(schema = @Schema(implementation = UserActivityDto.class)))
    })
    public Mono<UserActivityDto> createLink(@Parameter(description = "User id", example = "5") @PathVariable Long userId,
                                            @Parameter(description = "Activity id", example = "100") @PathVariable Long activityId,
                                            @RequestBody UserActivityDto dto) {
        dto.setUserId(userId);
        dto.setActivityId(activityId);
        return service.createLink(dto);
    }
}