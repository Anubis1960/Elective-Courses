package com.Spring.application.service;

import com.Spring.application.entity.Enrollment;
import com.Spring.application.exceptions.ObjectNotFound;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

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
    void export(OutputStream out, Optional<String> facultySection, Optional<Integer> year, boolean includeYear, boolean IncludeSection, boolean includeCourseName, boolean includeStudentName, boolean includeTeacher, boolean includeStudentMail, boolean includeGrade, boolean includeCategory, boolean includeNumOfStudents, boolean includeAVGgrade, String extension) throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;
}
