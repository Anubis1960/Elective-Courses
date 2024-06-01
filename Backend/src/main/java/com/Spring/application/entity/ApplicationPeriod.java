package com.Spring.application.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "ApplicationPeriod")
public class ApplicationPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_period_id", nullable = false)
    private Long applicationPeriodId;
    @Column(name= "is_open", nullable = false)
    private boolean isOpen;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;

    public ApplicationPeriod() {
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ApplicationPeriod(boolean isOpen, LocalDate startDate, LocalDate endDate) {
        this.isOpen = isOpen;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getApplicationPeriodId() {
        return applicationPeriodId;
    }

    public void setApplicationPeriodId(Long applicationPeriodId) {
        this.applicationPeriodId = applicationPeriodId;
    }

    public boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
}
