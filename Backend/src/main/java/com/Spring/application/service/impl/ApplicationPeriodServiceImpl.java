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
        isOpen = !isOpen;
        return isOpen;
    }
}
