package org.fuel50.user.controllers;

import org.fuel50.dtos.UserDto;
import org.fuel50.user.services.UserService;
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
@RequestMapping(path = "/company/{companyId}/user", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User", description = "User CRUD operations within a company")
public class UserController {
    private final UserService service;
    public UserController(UserService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "List users by company")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class))))
    })
    public Flux<UserDto> list(@Parameter(description = "Company id", example = "1") @PathVariable Long companyId) { return service.listByCompany(companyId); }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id within company")
    public Mono<UserDto> get(@Parameter(description = "Company id", example = "1") @PathVariable Long companyId,
                             @Parameter(description = "User id", example = "5") @PathVariable Long id) { return service.getWithinCompany(companyId, id); }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create user for company")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User created",
            content = @Content(schema = @Schema(implementation = UserDto.class)))
    })
    public Mono<UserDto> create(@Parameter(description = "Company id", example = "1") @PathVariable Long companyId,
                                @RequestBody UserDto dto) { return service.createForCompany(companyId, dto); }

    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Patch user within company")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User updated",
            content = @Content(schema = @Schema(implementation = UserDto.class)))
    })
    public Mono<UserDto> patch(@Parameter(description = "Company id", example = "1") @PathVariable Long companyId,
                               @Parameter(description = "User id", example = "5") @PathVariable Long id,
                               @RequestBody UserDto dto) { return service.patchWithinCompany(companyId, id, dto); }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user within company")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User deleted")
    })
    public Mono<Void> delete(@Parameter(description = "Company id", example = "1") @PathVariable Long companyId,
                             @Parameter(description = "User id", example = "5") @PathVariable Long id) { return service.deleteWithinCompany(companyId, id); }
}