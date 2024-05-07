package com.Spring.application.service;

import com.Spring.application.entity.Enrollment;
import com.Spring.application.exceptions.ObjectNotFound;

import java.util.List;

public interface EnrollmentService {
    Enrollment addEnrollment(Long studentId, Long courseId, Integer priority) throws ObjectNotFound;
    Enrollment updateEnrollment(Long enrollmentId, Long studentId, Long courseId, Integer priority, String status) throws ObjectNotFound;
    Enrollment deleteEnrollment(Long enrollmentId) throws ObjectNotFound;
    Enrollment getEnrollmentById(Long enrollmentId) throws ObjectNotFound;
    List<Enrollment> getAllEnrollments();
    List<Enrollment> getAllEnrollmentsWhereStatusIsAccepted();
}
