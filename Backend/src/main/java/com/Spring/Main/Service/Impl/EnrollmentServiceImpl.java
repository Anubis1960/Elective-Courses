package com.Spring.Main.service.impl;

import com.Spring.Main.entity.Course;
import com.Spring.Main.entity.Enrollment;
import com.Spring.Main.entity.Student;
import com.Spring.Main.enums.Status;
import com.Spring.Main.repository.CourseRepository;
import com.Spring.Main.repository.EnrollmentRepository;
import com.Spring.Main.repository.StudentRepository;
import com.Spring.Main.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService{

    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Override
    public ResponseEntity<String> addEnrollment(Long studentId, Long courseId, Integer priority, String status) {
        try{
            Student student = studentRepository.findById(studentId).orElse(null);
            if (student == null) {
                return ResponseEntity.badRequest().body("Student not found");
            }

            Course course = courseRepository.findById(courseId).orElse(null);
            if (course == null) {
                return ResponseEntity.badRequest().body("Course not found");
            }

            Enrollment enrollment = new Enrollment(student, course, priority, Status.valueOf(status));
            enrollmentRepository.save(enrollment);

            return ResponseEntity.ok("Enrollment added successfully");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid status");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Error adding enrollment");
    }

    @Override
    public ResponseEntity<String> updateEnrollment(Long enrollmentId, Long studentId, Long courseId, Integer priority, String status) {
        try {
            Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElse(null);
            if (enrollment == null) {
                return ResponseEntity.badRequest().body("Enrollment not found");
            }

            Student student = studentRepository.findById(studentId).orElse(null);
            if (student == null) {
                return ResponseEntity.badRequest().body("Student not found");
            }

            Course course = courseRepository.findById(courseId).orElse(null);
            if (course == null) {
                return ResponseEntity.badRequest().body("Course not found");
            }

            enrollment.setStudent(student);
            enrollment.setCourse(course);
            enrollment.setPriority(priority);
            enrollment.setStatus(Status.valueOf(status));
            enrollmentRepository.save(enrollment);

            return ResponseEntity.ok("Enrollment updated successfully");
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid status");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Error updating enrollment");
    }

    @Override
    public ResponseEntity<String> deleteEnrollment(Long enrollmentId) {
        try {
            enrollmentRepository.deleteById(enrollmentId);
            return ResponseEntity.ok("Enrollment deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Error deleting enrollment");
    }

    @Override
    public ResponseEntity<Enrollment> getEnrollmentById(Long enrollmentId) {
        try {
            Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElse(null);
            if (enrollment == null) {
                return ResponseEntity.badRequest().body(null);
            }

            return ResponseEntity.ok(enrollment);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        try {
            List<Enrollment> enrollments = enrollmentRepository.findAll();
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(null);
    }
}
