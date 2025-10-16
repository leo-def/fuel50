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
@RequestMapping(path = "/company/{companyId}/activity", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Activity", description = "Activity CRUD operations within a company")
public class ActivityController {
    private final ActivityService service;
    public ActivityController(ActivityService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "List activities by company")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ActivityDto.class))))
    })
    public Flux<ActivityDto> list(@Parameter(description = "Company id", example = "1") @PathVariable Long companyId) { return service.listByCompany(companyId); }

    @GetMapping("/{id}")
    @Operation(summary = "Get activity by id within company")
    public Mono<ActivityDto> get(@Parameter(description = "Company id", example = "1") @PathVariable Long companyId,
                                 @Parameter(description = "Activity id", example = "100") @PathVariable Long id) { return service.getWithinCompany(companyId, id); }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create activity for company")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Activity created",
            content = @Content(schema = @Schema(implementation = ActivityDto.class)))
    })
    public Mono<ActivityDto> create(@Parameter(description = "Company id", example = "1") @PathVariable Long companyId,
                                    @RequestBody ActivityDto dto) { return service.createForCompany(companyId, dto); }

    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Patch activity within company")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Activity updated",
            content = @Content(schema = @Schema(implementation = ActivityDto.class)))
    })
    public Mono<ActivityDto> patch(@Parameter(description = "Company id", example = "1") @PathVariable Long companyId,
                                   @Parameter(description = "Activity id", example = "100") @PathVariable Long id,
                                   @RequestBody ActivityDto dto) { return service.patchWithinCompany(companyId, id, dto); }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete activity within company")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Activity deleted")
    })
    public Mono<Void> delete(@Parameter(description = "Company id", example = "1") @PathVariable Long companyId,
                             @Parameter(description = "Activity id", example = "100") @PathVariable Long id) { return service.deleteWithinCompany(companyId, id); }
}