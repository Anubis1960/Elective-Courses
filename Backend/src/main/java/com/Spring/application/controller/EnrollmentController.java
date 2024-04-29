package com.Spring.application.controller;


import com.Spring.application.entity.Enrollment;
import com.Spring.application.enums.Status;
import com.Spring.application.exceptions.ObjectNotFound;
import com.Spring.application.repository.EnrollmentRepository;
import com.Spring.application.service.impl.EnrollmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollment")
public class EnrollmentController {
    @Autowired
    private EnrollmentServiceImpl enrollmentService;
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @PostMapping("/")
    public ResponseEntity<Enrollment> enroll(Long studentId, Long courseId, Integer priority) throws ObjectNotFound {
        Enrollment enrollment = enrollmentService.addEnrollment(studentId, courseId, priority);
        return new ResponseEntity<>(enrollment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Enrollment> updateEnrollment(@PathVariable("id") Long enrollmentId,Long studentId, Long courseId, Integer priority, String status) throws ObjectNotFound {
        Enrollment enrollment = enrollmentService.updateEnrollment(enrollmentId, studentId, courseId, priority, status);
        return new ResponseEntity<>(enrollment, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Enrollment> deleteEnrollment(@PathVariable("id") Long enrollmentId) throws ObjectNotFound {
        Enrollment enrollment = enrollmentService.deleteEnrollment(enrollmentId);
        return new ResponseEntity<>(enrollment, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable("id") Long enrollmentId) throws ObjectNotFound {
        Enrollment enrollment = enrollmentService.getEnrollmentById(enrollmentId);
        return new ResponseEntity<>(enrollment, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        if (enrollments.isEmpty()) {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(enrollments, HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<List<Enrollment>> getEnrollmentsSortedByStudentGrade() {
        List<Enrollment> enrollments = enrollmentRepository.findAllByOrderByStudentGradeAsc();
        if (enrollments.isEmpty()) {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(enrollments, HttpStatus.OK);
    }
}
