package com.Spring.Main.controller;


import com.Spring.Main.entity.Enrollment;
import com.Spring.Main.enums.Status;
import com.Spring.Main.service.EnrollmentService;
import com.Spring.Main.service.impl.EnrollmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/enrollment")
public class EnrollmentController {
    @Autowired
    private EnrollmentServiceImpl enrollmentService;

    @PostMapping("/enroll")
    public ResponseEntity<String> enroll(Long studentId, Long courseId, Integer priority) {
        return enrollmentService.addEnrollment(studentId, courseId, priority, Status.PENDING.toString());
    }

    @PostMapping("/updatenrollment")
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
