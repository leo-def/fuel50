package org.fuel50.datagenerator.factories;

import net.datafaker.Faker;
import org.fuel50.dtos.RatingDto;

import java.util.concurrent.ThreadLocalRandom;

public class RatingFactory {
    private static final Faker faker = new Faker();

    public static RatingDto fakeRating(Long fromUserId, Long toUserActivityId) {
        RatingDto dto = new RatingDto();
        dto.setFromUserId(fromUserId);
        dto.setToUserActivityId(toUserActivityId);
        dto.setScore((byte) ThreadLocalRandom.current().nextInt(1, 6)); // 1-5
        dto.setComment(faker.lorem().sentence());
        return dto;
    }
}