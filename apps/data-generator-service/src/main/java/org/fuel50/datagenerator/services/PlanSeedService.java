package org.fuel50.datagenerator.services;

import org.fuel50.datagenerator.ai.model.Plan;
import org.fuel50.datagenerator.ai.model.PlanResult;
import org.fuel50.datagenerator.ai.model.CompanySpec;
import org.fuel50.dtos.*;
import org.fuel50.datagenerator.factories.*;
import org.fuel50.clients.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class PlanSeedService {
    private final CompanyClient companyClient;
    private final UserClient userClient;
    private final ActivityClient activityClient;
    private final ActionClient actionClient;
    private final UserActivityClient userActivityClient;

    public PlanSeedService(CompanyClient companyClient,
                           UserClient userClient,
                           ActivityClient activityClient,
                           ActionClient actionClient,
                           UserActivityClient userActivityClient) {
        this.companyClient = companyClient;
        this.userClient = userClient;
        this.activityClient = activityClient;
        this.actionClient = actionClient;
        this.userActivityClient = userActivityClient;
    }

    public Mono<PlanResult> seed(Plan plan) {
        PlanResult result = new PlanResult();

        List<CompanySpec> companies = Optional.ofNullable(plan.getCompanies()).orElse(List.of());

        return Flux.fromIterable(companies)
                .flatMap(companySpec -> {
                    CompanyDto companyDto = CompanyFactory.fromSpec(companySpec);
                    return companyClient.create(companyDto)
                            .flatMap(createdCompany -> {
                                Long companyId = createdCompany.getId();
                                result.getCompanyIds().add(companyId);

                                // Users
                                Flux<UserDto> usersFlux = Flux.fromIterable(Optional.ofNullable(companySpec.getUsers()).orElse(List.of()))
                                        .flatMap(userSpec -> userClient.createForCompany(companyId, UserFactory.fromSpec(companyId, userSpec)));

                                // Activities and nested actions
                                Flux<ActivityDto> activitiesFlux = Flux.fromIterable(Optional.ofNullable(companySpec.getActivities()).orElse(List.of()))
                                        .flatMap(activitySpec -> activityClient.createForCompany(companyId, ActivityFactory.fromSpec(companyId, activitySpec))
                                                .flatMap(activityDto -> {
                                                    Long activityId = activityDto.getId();
                                                    Flux<ActionDto> actionsFlux = Flux.fromIterable(Optional.ofNullable(activitySpec.getActions()).orElse(List.of()))
                                                            .flatMap(actionSpec -> actionClient.createForCompanyAndActivity(companyId, activityId, ActionFactory.fromSpec(companyId, activityId, actionSpec)));
                                                    return actionsFlux.collectList().map(list -> activityDto);
                                                })
                                        );

                                return Mono.zip(usersFlux.collectList(), activitiesFlux.collectList())
                                        .flatMap(tuple -> {
                                            List<UserDto> users = tuple.getT1();
                                            List<ActivityDto> activities = tuple.getT2();
                                            result.setUsersCreated(result.getUsersCreated() + users.size());
                                            result.setActivitiesCreated(result.getActivitiesCreated() + activities.size());

                                            // Count actions by querying per activity? We already created actions above but not counted.
                                            int actionsCount = Optional.ofNullable(companySpec.getActivities()).orElse(List.of())
                                                    .stream().mapToInt(a -> Optional.ofNullable(a.getActions()).orElse(List.of()).size()).sum();
                                            result.setActionsCreated(result.getActionsCreated() + actionsCount);

                                            // Link each user to each activity
                                            Flux<UserActivityDto> links = Flux.fromIterable(users)
                                                    .flatMap(user -> Flux.fromIterable(activities)
                                                            .flatMap(act -> userActivityClient.createLink(UserActivityFactory.fakeLink(user.getId(), act.getId()))));

                                            return links.collectList()
                                                    .doOnNext(list -> result.setUserActivityLinksCreated(result.getUserActivityLinksCreated() + list.size()))
                                                    .thenReturn(createdCompany);
                                        });
                            });
                })
                .collectList()
                .map(list -> result);
    }
}