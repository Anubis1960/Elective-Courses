package com.Spring.Main.enums;

public enum Role {
    STUDENT("STUDENT"),
    ADMIN("ADMIN"),
    STUDENTADMIN("STUDENTADMIN");

    private final String role;

    Role(String role) {
        this.role = role;
    }
}
