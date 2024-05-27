package com.Spring.application.service.impl;

import com.Spring.application.service.ApplicationPeriodService;
import org.springframework.stereotype.Service;

@Service
public class ApplicationPeriodServiceImpl implements ApplicationPeriodService {
    private boolean isOpen = true;
    @Override
    public boolean getApplicationPeriod() {
        return isOpen;
    }

    @Override
    public boolean reverseApplicationPeriod() {
        System.out.println("Application period is now: " + isOpen);
        isOpen = !isOpen;
        System.out.println("Application period is now: " + isOpen);
        return isOpen;
    }
}
