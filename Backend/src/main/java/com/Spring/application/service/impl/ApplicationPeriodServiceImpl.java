package com.Spring.application.service.impl;

import com.Spring.application.repository.ApplicationPeriodRepository;
import com.Spring.application.service.ApplicationPeriodService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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

    @Override
    public boolean updateApplicationPeriod(String endDate) {
        if (applicationPeriodRepository.findAll().isEmpty()) {
            return false;
        }
        applicationPeriodRepository.findAll().get(0).setEndDate(LocalDate.parse(endDate));
        return true;
    }

    @Override
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void checkApplicationPeriod() {
        if (applicationPeriodRepository.findAll().isEmpty()) {
            return;
        }
        if (!getApplicationPeriod()) {
            return;
        }
        LocalDate endDate = applicationPeriodRepository.findAll().get(0).getEndDate();
        if (LocalDate.now().isAfter(endDate)) {
            reverseApplicationPeriod();
        }
    }
}
