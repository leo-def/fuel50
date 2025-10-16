package org.fuel50.activity.controllers;

import org.fuel50.dtos.ActivityDto;
import org.fuel50.activity.services.ActivityService;
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
@RequestMapping(path = "/company/{companyId}/user/{userId}/activity", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Activity", description = "Activities filtered by user within a company")
public class ActivityByUserController {
    private final ActivityService service;
    public ActivityByUserController(ActivityService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "List activities by company and user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ActivityDto.class))))
    })
    public Flux<ActivityDto> list(@Parameter(description = "Company id", example = "1") @PathVariable Long companyId,
                                  @Parameter(description = "User id", example = "5") @PathVariable Long userId) {
        return service.listByCompanyAndUser(companyId, userId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get activity by id within company and user")
    public Mono<ActivityDto> get(@Parameter(description = "Company id", example = "1") @PathVariable Long companyId,
                                 @Parameter(description = "User id", example = "5") @PathVariable Long userId,
                                 @Parameter(description = "Activity id", example = "100") @PathVariable Long id) {
        return service.getWithinCompanyAndUser(companyId, userId, id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create activity for company and user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Activity created",
            content = @Content(schema = @Schema(implementation = ActivityDto.class)))
    })
    public Mono<ActivityDto> create(@Parameter(description = "Company id", example = "1") @PathVariable Long companyId,
                                    @Parameter(description = "User id", example = "5") @PathVariable Long userId,
                                    @RequestBody ActivityDto dto) {
        return service.createForCompanyAndUser(companyId, userId, dto);
    }

    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Patch activity within company and user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Activity updated",
            content = @Content(schema = @Schema(implementation = ActivityDto.class)))
    })
    public Mono<ActivityDto> patch(@Parameter(description = "Company id", example = "1") @PathVariable Long companyId,
                                   @Parameter(description = "User id", example = "5") @PathVariable Long userId,
                                   @Parameter(description = "Activity id", example = "100") @PathVariable Long id,
                                   @RequestBody ActivityDto dto) {
        return service.patchWithinCompanyAndUser(companyId, userId, id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete activity within company and user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Activity deleted")
    })
    public Mono<Void> delete(@Parameter(description = "Company id", example = "1") @PathVariable Long companyId,
                             @Parameter(description = "User id", example = "5") @PathVariable Long userId,
                             @Parameter(description = "Activity id", example = "100") @PathVariable Long id) {
        return service.deleteWithinCompanyAndUser(companyId, userId, id);
    }
}