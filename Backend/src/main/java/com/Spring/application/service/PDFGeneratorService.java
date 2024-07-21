package com.Spring.application.service;

import com.Spring.application.entity.Enrollment;
import com.Spring.application.entity.Student;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

public interface PDFGeneratorService {
    void exportScheduleToPDF(OutputStream out,Long id) throws IOException;
    void exportStudentsOfCourseToPDF(OutputStream out,Long id) throws IOException, IllegalAccessException;
    void exportEnrollments(OutputStream outputStream, Optional<String> facultySection, Optional<Integer> year, boolean includeEnrollmentId, boolean includeStudentId, boolean includeCourseId, boolean includeYear, boolean IncludeSection, boolean includeCourseName, boolean includeStudentName, boolean includeTeacher, boolean includeStudentMail, boolean includeGrade, boolean includeCategory) throws IOException;    void exportStudents(OutputStream out, Optional<String> facultySection, Optional<Integer> year,boolean includeId, boolean includeName, boolean includeEmail, boolean includeGrade, boolean includeSection, boolean includeYear) throws IOException;
    void exportEnrollmentsCopy(OutputStream out, Optional<String> facultySection, Optional<Integer> year, boolean includeYear, boolean IncludeSection, boolean includeCourseName, boolean includeStudentName, boolean includeTeacher, boolean includeStudentMail, boolean includeGrade, boolean includeCategory, boolean includeNumOfStudents, boolean includeAVGgrade, String extension) throws IOException, IllegalAccessException;
}
