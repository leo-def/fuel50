package org.fuel50.datagenerator.factories;

import net.datafaker.Faker;
import org.fuel50.dtos.UserActivityDto;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class UserActivityFactory {
    private static final Faker faker = new Faker();

    public static UserActivityDto fakeLink(Long userId, Long activityId) {
        UserActivityDto dto = new UserActivityDto();
        dto.setUserId(userId);
        dto.setActivityId(activityId);
        dto.setJoinedAt(LocalDateTime.now().minusDays(ThreadLocalRandom.current().nextInt(1, 90)));
        // Optionally set a role
        dto.setRole(faker.job().title());
        return dto;
    }
}