package com.Spring.application.service;

import com.Spring.application.entity.Enrollment;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

public interface PDFGeneratorService {
    //void exportEnrollmentsToPDF(OutputStream out) throws IOException;
    void exportScheduleToPDF(OutputStream out,Long id) throws IOException;
    void exportStudentsOfCourseToPDF(OutputStream out,Long id) throws IOException;
    //void exportEnrollmentByFacultySectionToPDF(OutputStream out,String facultySection) throws IOException;
    //void exportEnrollmentByYearToPDF(OutputStream out,Integer year) throws IOException;
    //void exportEnrollmentByFacultySectionAndYearToPDF(OutputStream out, String facultySection, Integer year) throws IOException;
    void exportEnrollments(OutputStream outputStream, Optional<String> facultySection, Optional<Integer> year) throws IOException;
    void writeEnrollmentsToPDF(OutputStream outputStream, List<Enrollment> enrollments) throws IOException;
}
