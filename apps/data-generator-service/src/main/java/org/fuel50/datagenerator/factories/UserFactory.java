package org.fuel50.datagenerator.factories;

import net.datafaker.Faker;
import org.fuel50.dtos.UserDto;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class UserFactory {
    private static final Faker faker = new Faker();

    public static UserDto fakeForCompany(Long companyId) {
        UserDto dto = new UserDto();
        dto.setCompanyId(companyId);
        dto.setEmail(faker.internet().emailAddress());
        dto.setFirstName(faker.name().firstName());
        dto.setLastName(faker.name().lastName());
        dto.setJoinedAt(LocalDateTime.now().minusDays(ThreadLocalRandom.current().nextInt(1, 365)));
        dto.setIsAdmin(faker.bool().bool());
        return dto;
    }

    public static UserDto fromSpec(Long companyId, org.fuel50.datagenerator.ai.model.UserSpec spec) {
        UserDto dto = new UserDto();
        dto.setCompanyId(companyId);
        dto.setEmail(spec.getEmail());
        dto.setFirstName(spec.getFirstName());
        dto.setLastName(spec.getLastName());
        dto.setIsAdmin(Boolean.TRUE.equals(spec.getIsAdmin()));
        return dto;
    }
}