package com.Spring.application.controller;


import com.Spring.application.entity.Enrollment;
import com.Spring.application.enums.Status;
import com.Spring.application.service.impl.EnrollmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollment")
public class EnrollmentController {
    @Autowired
    private EnrollmentServiceImpl enrollmentService;

    @PostMapping("/enroll")
    public ResponseEntity<String> enroll(Long studentId, Long courseId, Integer priority) {
        return enrollmentService.addEnrollment(studentId, courseId, priority, Status.PENDING.toString());
    }

    @PutMapping("/updatenrollment")
    public ResponseEntity<String> updateEnrollment(Long enrollmentId,Long studentId, Long courseId, Integer priority) {
        return enrollmentService.updateEnrollment(enrollmentId,studentId, courseId, priority, Status.PENDING.toString());
    }

    @PostMapping("/deletenrollment")
    public ResponseEntity<String> deleteEnrollment(Long enrollmentId) {
        return enrollmentService.deleteEnrollment(enrollmentId);
    }

    @GetMapping("/getEnrollmentById")
    public ResponseEntity<Enrollment> getEnrollmentById(Long enrollmentId) {
        return enrollmentService.getEnrollmentById(enrollmentId);
    }

    @GetMapping("/getAllEnrollments")
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }
}
