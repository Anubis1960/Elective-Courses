package com.Spring.application.service;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;

public interface PDFGeneratorService {
    void exportEnrollmentsToPDF(OutputStream out) throws IOException;
    void exportScheduleToPDF(OutputStream out,Long id) throws IOException;
    void exportStudentsOfCourseToPDF(OutputStream out,Long id) throws IOException;
    void exportEnrollmentByFacultySectionToPDF(OutputStream out,String facultySection) throws IOException;
    void exportEnrollmentByYearToPDF(OutputStream out,Integer year) throws IOException;
    void exportEnrollmentByFacultySectionAndYearToPDF(OutputStream out, String facultySection, Integer year) throws IOException;
}
