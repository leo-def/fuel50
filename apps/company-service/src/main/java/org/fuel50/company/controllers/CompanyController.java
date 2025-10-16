package org.fuel50.company.controllers;

import org.fuel50.dtos.CompanyDto;
import org.fuel50.company.services.CompanyService;
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
// no explicit OpenAPI @RequestBody to avoid name collision with Spring's annotation

@RestController
@RequestMapping(path = "/company", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Company", description = "Company CRUD operations")
public class CompanyController {
    private final CompanyService service;
    public CompanyController(CompanyService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "List companies")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CompanyDto.class))))
    })
    public Flux<CompanyDto> list() { return service.list(); }

    @GetMapping("/{id}")
    @Operation(summary = "Get company by id")
    public Mono<CompanyDto> get(@Parameter(description = "Company id", example = "1") @PathVariable Long id) { return service.get(id); }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create company")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Company created",
            content = @Content(schema = @Schema(implementation = CompanyDto.class)))
    })
    public Mono<CompanyDto> create(@RequestBody CompanyDto dto) { return service.create(dto); }

    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Patch company")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Company updated",
            content = @Content(schema = @Schema(implementation = CompanyDto.class)))
    })
    public Mono<CompanyDto> patch(@Parameter(description = "Company id", example = "1") @PathVariable Long id,
                                  @RequestBody CompanyDto dto) { return service.patch(id, dto); }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete company")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Company deleted")
    })
    public Mono<Void> delete(@Parameter(description = "Company id", example = "1") @PathVariable Long id) { return service.delete(id); }
}