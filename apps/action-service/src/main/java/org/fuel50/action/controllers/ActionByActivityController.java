package org.fuel50.action.controllers;

import org.fuel50.dtos.ActionDto;
import org.fuel50.action.services.ActionService;
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
@RequestMapping(path = "/company/{companyId}/activity/{activityId}/action", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Action", description = "Actions filtered by activity")
public class ActionByActivityController {
    private final ActionService service;
    public ActionByActivityController(ActionService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "List actions by company and activity")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ActionDto.class))))
    })
    public Flux<ActionDto> list(@Parameter(description = "Company id", example = "1") @PathVariable Long companyId,
                                @Parameter(description = "Activity id", example = "100") @PathVariable Long activityId) {
        return service.listByCompanyAndActivity(companyId, activityId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create action under activity")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Action created",
            content = @Content(schema = @Schema(implementation = ActionDto.class)))
    })
    public Mono<ActionDto> create(@Parameter(description = "Company id", example = "1") @PathVariable Long companyId,
                                  @Parameter(description = "Activity id", example = "100") @PathVariable Long activityId,
                                  @RequestBody ActionDto dto) {
        return service.createForCompanyAndActivity(companyId, activityId, dto);
    }
}