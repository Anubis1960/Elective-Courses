package com.Spring.Main.Controller;

import com.Spring.Main.Entity.Enrollment;
import com.Spring.Main.Enums.Status;
import com.Spring.Main.Service.Impl.CourseScheduleServiceImpl;
import com.Spring.Main.Service.Impl.CourseServiceImpl;
import com.Spring.Main.Service.Impl.EnrollmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

public class StudentController {
    @Autowired
    EnrollmentServiceImpl enrollmentService;
    @Autowired
    CourseScheduleServiceImpl courseScheduleService;
    @Autowired
    CourseServiceImpl courseService;

    @PostMapping("/enroll")
    public ResponseEntity<String> enroll(Long studentId, Long courseId, Integer priority) {
        return enrollmentService.addEnrollment(studentId, courseId, priority, Status.PENDING.toString());
    }

    @PostMapping("/updateEnrollment")
    public ResponseEntity<String> updateEnrollment(Long enrollmentId,Long studentId, Long courseId, Integer priority) {
        return enrollmentService.updateEnrollment(enrollmentId,studentId, courseId, priority, Status.PENDING.toString());
    }

    @PostMapping("/deleteEnrollment")
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
