package org.fuel50.datagenerator.ai.model;

import lombok.Data;

@Data
public class UserSpec {
    private String firstName;
    private String lastName;
    private String email;
    private Boolean isAdmin;
}
