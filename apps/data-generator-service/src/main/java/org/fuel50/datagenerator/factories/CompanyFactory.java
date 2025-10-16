package org.fuel50.datagenerator.factories;

import net.datafaker.Faker;
import org.fuel50.dtos.CompanyDto;

public class CompanyFactory {
    private static final Faker faker = new Faker();

    public static CompanyDto fakeCompanyDto() {
        CompanyDto dto = new CompanyDto();
        dto.setName(faker.company().name());
        dto.setDomain(faker.internet().domainName());
        return dto;
    }

    public static CompanyDto fromSpec(org.fuel50.datagenerator.ai.model.CompanySpec spec) {
        CompanyDto dto = new CompanyDto();
        dto.setName(spec.getName());
        dto.setDomain(spec.getDomain());
        return dto;
    }
}