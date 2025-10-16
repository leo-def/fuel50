package org.fuel50.datagenerator.ai.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class PlanResult {
    private List<Long> companyIds = new ArrayList<>();
    private int usersCreated;
    private int activitiesCreated;
    private int actionsCreated;
    private int userActivityLinksCreated;
}