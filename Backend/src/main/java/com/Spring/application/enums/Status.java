package com.Spring.application.enums;

public enum Status {
    ACCEPTED("ACCEPTED"),
    PENDING("PENDING"),
    REJECTED("REJECTED");

    private final String status;

    Status(String status) {
        this.status = status;
    }
}
