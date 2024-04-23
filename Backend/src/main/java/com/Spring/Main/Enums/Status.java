package com.Spring.Main.enums;

public enum Status {
    ACCEPTED("ACCEPTED"),
    PENDING("PENDING");

    private final String status;

    Status(String status) {
        this.status = status;
    }
}
