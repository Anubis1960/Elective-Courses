package com.Spring.application.service;

import com.Spring.application.entity.Enrollment;
import com.Spring.application.exceptions.ObjectNotFound;

import java.util.List;

public interface EnrollmentService {
    Enrollment addEnrollment(Long studentId, Long courseId, Integer priority) throws ObjectNotFound;
    void updateEnrollment(Long enrollmentId, Long studentId, Long courseId, Integer priority, String status) throws ObjectNotFound;
    Enrollment deleteEnrollment(Long enrollmentId) throws ObjectNotFound;
    Enrollment getEnrollmentById(Long enrollmentId) throws ObjectNotFound;
    List<Enrollment> getAllEnrollments();
    Integer countByCourseId(Long courseId);
    Integer countByStudentId(Long studentId);
    List<Enrollment> getEnrollmentsByStudentId(Long studentId);
    List<Enrollment> assignStudents() throws ObjectNotFound;
    List<Enrollment> getEnrollmentsByYearAndStatusIsAccepted(Integer year);
    List<Enrollment> getEnrollmentsByFacultySectionAndStatusIsAccepted(String facultySection);
    Enrollment reassingStudent(Long studentId, Long courseId, Long newCourseId);
    Integer countByCourseIdAndStatusIsAccepted(Long courseId);
    List<Enrollment> executeQuery(String query);
}
