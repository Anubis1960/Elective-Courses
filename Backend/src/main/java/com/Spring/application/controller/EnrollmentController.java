package com.Spring.application.controller;


import com.Spring.application.entity.Enrollment;
import com.Spring.application.enums.Status;
import com.Spring.application.exceptions.ObjectNotFound;
import com.Spring.application.repository.EnrollmentRepository;
import com.Spring.application.service.impl.EnrollmentServiceImpl;
import com.Spring.application.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
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
    @JsonView(Views.Public.class)
    public ResponseEntity<Enrollment> enroll(@RequestParam Long studentId,
                                             @RequestParam Long courseId,
                                             @RequestParam Integer priority) throws ObjectNotFound {
        Enrollment enrollment = enrollmentService.addEnrollment(studentId, courseId, priority);
        return new ResponseEntity<>(enrollment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<Enrollment> updateEnrollment(@PathVariable("id") Long enrollmentId,
                                                       @RequestParam Long studentId,
                                                       @RequestParam Long courseId,
                                                       @RequestParam Integer priority,
                                                       @RequestParam String status) throws ObjectNotFound {
        Enrollment enrollment = enrollmentService.updateEnrollment(enrollmentId, studentId, courseId, priority, status);
        return new ResponseEntity<>(enrollment, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<Enrollment> deleteEnrollment(@PathVariable("id") Long enrollmentId) throws ObjectNotFound {
        Enrollment enrollment = enrollmentService.deleteEnrollment(enrollmentId);
        return new ResponseEntity<>(enrollment, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable("id") Long enrollmentId) throws ObjectNotFound {
        Enrollment enrollment = enrollmentService.getEnrollmentById(enrollmentId);
        return new ResponseEntity<>(enrollment, HttpStatus.OK);
    }

    @GetMapping("/")
    @JsonView(Views.Public.class)
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        if (enrollments.isEmpty()) {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(enrollments, HttpStatus.OK);
    }

    @GetMapping("/test")
    @JsonView(Views.Public.class)
    public ResponseEntity<List<Enrollment>> getEnrollmentsSortedByStudentGrade() {
        List<Enrollment> enrollments = enrollmentRepository.findAllByOrderByStudentGradeAsc();
        if (enrollments.isEmpty()) {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(enrollments, HttpStatus.OK);
    }
}
