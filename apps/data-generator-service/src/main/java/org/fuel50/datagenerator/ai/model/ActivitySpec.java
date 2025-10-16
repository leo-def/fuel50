package org.fuel50.datagenerator.ai.model;

import java.util.List;

import lombok.Data;

@Data
public class ActivitySpec {
    private String name;
    private String description;
    private List<ActionSpec> actions;
}