package org.fuel50.datagenerator.ai.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fuel50.datagenerator.ai.model.Plan;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PromptAiService {
    private final ChatClient.Builder chatClientBuilder;
    private final ObjectMapper objectMapper;

    public PromptAiService(ChatClient.Builder chatClientBuilder, ObjectMapper objectMapper) {
        this.chatClientBuilder = chatClientBuilder;
        this.objectMapper = objectMapper;
    }

    public Mono<Plan> generatePlan(String userPrompt, String prevPrompt) {
        String system = """
            You are a planning assistant. 
            Return ONLY a JSON object with keys: companies (array). 
            Each company has: name, domain, users (array of firstName,lastName,email,isAdmin), 
            activities (array of name,description, actions array of name,description). 
            No extra text.
        """;

        ChatClient chatClient = chatClientBuilder.build();

        var request = chatClient.prompt().system(system);
        if (prevPrompt != null && !prevPrompt.isBlank()) {
            request = request.user(prevPrompt);
        }

        return request.user(userPrompt)
                .stream()
                .content() // Flux<String> chunks
                .doOnNext(System.out::print) // optional: print streaming output
                .reduce(new StringBuilder(), (sb, chunk) -> sb.append(chunk))
                .map(StringBuilder::toString)
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, Plan.class);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse AI JSON", e);
                    }
                });
    }
}
