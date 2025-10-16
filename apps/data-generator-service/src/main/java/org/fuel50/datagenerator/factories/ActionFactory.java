package org.fuel50.datagenerator.factories;

import net.datafaker.Faker;
import org.fuel50.dtos.ActionDto;

public class ActionFactory {
    private static final Faker faker = new Faker();

    public static ActionDto fakeForCompany(Long companyId) {
        ActionDto dto = new ActionDto();
        dto.setCompanyId(companyId);
        dto.setName(faker.hacker().verb() + " " + faker.hacker().noun());
        dto.setDescription(faker.lorem().sentence());
        return dto;
    }

    public static ActionDto fakeForCompanyAndActivity(Long companyId, Long activityId) {
        ActionDto dto = fakeForCompany(companyId);
        dto.setActivityId(activityId);
        return dto;
    }

    public static ActionDto fromSpec(Long companyId, Long activityId, org.fuel50.datagenerator.ai.model.ActionSpec spec) {
        ActionDto dto = new ActionDto();
        dto.setCompanyId(companyId);
        dto.setActivityId(activityId);
        dto.setName(spec.getName());
        dto.setDescription(spec.getDescription());
        return dto;
    }
}