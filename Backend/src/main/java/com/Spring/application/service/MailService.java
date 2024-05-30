package com.Spring.application.service;

public interface MailService {
    void sendMail(String to, String subject, String text);
    void sendAllAssignedCoursesMail();
    void sendReassignment(Long studentId, Long courseId, Long newCourseId);
}
