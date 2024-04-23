package com.Spring.Main.service;

import com.Spring.Main.entity.Enrollment;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EnrollmentService {
    ResponseEntity<String> addEnrollment(Long studentId, Long courseId, Integer priority, String status);
    ResponseEntity<String> updateEnrollment(Long enrollmentId, Long studentId, Long courseId, Integer priority, String status);
    ResponseEntity<String> deleteEnrollment(Long enrollmentId);
    ResponseEntity<Enrollment> getEnrollmentById(Long enrollmentId);
    ResponseEntity<List<Enrollment>> getAllEnrollments();
}
