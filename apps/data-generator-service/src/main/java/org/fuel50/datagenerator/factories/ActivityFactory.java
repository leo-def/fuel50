package org.fuel50.datagenerator.factories;

import net.datafaker.Faker;
import org.fuel50.dtos.ActivityDto;

public class ActivityFactory {
    private static final Faker faker = new Faker();

    public static ActivityDto fakeForCompany(Long companyId) {
        ActivityDto dto = new ActivityDto();
        dto.setCompanyId(companyId);
        dto.setName(faker.book().title());
        dto.setDescription(faker.lorem().sentence());
        return dto;
    }

    public static ActivityDto fakeForCompanyAndUser(Long companyId, Long userId) {
        ActivityDto dto = fakeForCompany(companyId);
        dto.setCreatedBy(userId);
        return dto;
    }

    public static ActivityDto fromSpec(Long companyId, org.fuel50.datagenerator.ai.model.ActivitySpec spec) {
        ActivityDto dto = new ActivityDto();
        dto.setCompanyId(companyId);
        dto.setName(spec.getName());
        dto.setDescription(spec.getDescription());
        return dto;
    }
}