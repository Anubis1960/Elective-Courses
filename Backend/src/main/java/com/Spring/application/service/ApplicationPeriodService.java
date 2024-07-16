package com.Spring.application.service;

import com.Spring.application.entity.ApplicationPeriod;

import java.time.LocalDate;

public interface ApplicationPeriodService {
    boolean getApplicationPeriod();
    boolean reverseApplicationPeriod();
    void checkApplicationPeriod();
    boolean updateApplicationPeriod(LocalDate endDate);
}
