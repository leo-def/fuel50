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
@RequestMapping(path = "/company/{companyId}/action", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Action", description = "Action CRUD operations within a company")
public class ActionController {
    private final ActionService service;
    public ActionController(ActionService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "List actions by company")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ActionDto.class))))
    })
    public Flux<ActionDto> list(@Parameter(description = "Company id", example = "1") @PathVariable Long companyId) { return service.listByCompany(companyId); }

    @GetMapping("/{id}")
    @Operation(summary = "Get action by id within company")
    public Mono<ActionDto> get(@Parameter(description = "Company id", example = "1") @PathVariable Long companyId,
                               @Parameter(description = "Action id", example = "10") @PathVariable Long id) { return service.getWithinCompany(companyId, id); }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create action for company")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Action created",
            content = @Content(schema = @Schema(implementation = ActionDto.class)))
    })
    public Mono<ActionDto> create(@Parameter(description = "Company id", example = "1") @PathVariable Long companyId,
                                  @RequestBody ActionDto dto) { return service.createForCompany(companyId, dto); }

    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Patch action within company")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Action updated",
            content = @Content(schema = @Schema(implementation = ActionDto.class)))
    })
    public Mono<ActionDto> patch(@Parameter(description = "Company id", example = "1") @PathVariable Long companyId,
                                 @Parameter(description = "Action id", example = "10") @PathVariable Long id,
                                 @RequestBody ActionDto dto) { return service.patchWithinCompany(companyId, id, dto); }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete action within company")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Action deleted")
    })
    public Mono<Void> delete(@Parameter(description = "Company id", example = "1") @PathVariable Long companyId,
                             @Parameter(description = "Action id", example = "10") @PathVariable Long id) { return service.deleteWithinCompany(companyId, id); }
}