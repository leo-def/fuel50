package org.fuel50.datagenerator.factories;

import org.fuel50.dtos.UserActionDto;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class UserActionFactory {

    public static UserActionDto fakeLink(Long userId, Long actionId) {
        UserActionDto dto = new UserActionDto();
        dto.setUserId(userId);
        dto.setActionId(actionId);
        dto.setPerformedAt(LocalDateTime.now().minusDays(ThreadLocalRandom.current().nextInt(0, 30)));
        return dto;
    }
}