package org.fuel50.datagenerator.ai.model;

import java.util.List;

import lombok.Data;

@Data
public class CompanySpec {
    private String name;
    private String domain;
    private List<UserSpec> users;
    private List<ActivitySpec> activities;
}