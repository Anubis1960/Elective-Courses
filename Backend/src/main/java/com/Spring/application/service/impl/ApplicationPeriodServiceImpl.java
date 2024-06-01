package com.Spring.application.service.impl;

import com.Spring.application.repository.ApplicationPeriodRepository;
import com.Spring.application.service.ApplicationPeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationPeriodServiceImpl implements ApplicationPeriodService {
    @Autowired
    private ApplicationPeriodRepository applicationPeriodRepository;
    @Override
    public boolean getApplicationPeriod() {
        return applicationPeriodRepository.findAll().get(0).getIsOpen();
    }

    @Override
    public boolean reverseApplicationPeriod() {
        boolean isOpen = applicationPeriodRepository.findAll().get(0).getIsOpen();
        applicationPeriodRepository.findAll().get(0).setIsOpen(!isOpen);
        applicationPeriodRepository.save(applicationPeriodRepository.findAll().get(0));
        return applicationPeriodRepository.findAll().get(0).getIsOpen();
    }
}
