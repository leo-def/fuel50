package org.fuel50.datagenerator.controllers;

import lombok.Data;
import org.fuel50.datagenerator.ai.model.PlanResult;
import org.fuel50.datagenerator.services.PlanSeedService;
import org.fuel50.datagenerator.ai.services.PromptAiService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@Tag(name = "Prompt", description = "AI prompt endpoints for generating and seeding plans")
public class PromptController {

    private final PromptAiService promptAiService;
    private final PlanSeedService planSeedService;

    public PromptController(PromptAiService promptAiService, PlanSeedService planSeedService) {
        this.promptAiService = promptAiService;
        this.planSeedService = planSeedService;
    }

    @PostMapping(path = "/prompt/plan-seed", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Generate a plan from prompt and seed data")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Plan generated and seeded",
            content = @Content(schema = @Schema(implementation = PlanResult.class)))
    })
    public Mono<PlanResult> planAndSeed(@Valid @RequestBody PromptRequest request) {
        return promptAiService.generatePlan(request.getPrompt(), request.getPrevPrompt())
                .flatMap(planSeedService::seed);
    }

    @Data
    public static class PromptRequest {
        @NotBlank
        @Schema(description = "Primary user prompt", example = "Generate sample companies with users and activities")
        private String prompt;

        @Schema(description = "Optional previous context prompt", example = "Earlier we created Acme with 2 activities")
        private String prevPrompt;
    }
}