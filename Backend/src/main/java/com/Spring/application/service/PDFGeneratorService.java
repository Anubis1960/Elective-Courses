package com.Spring.application.service;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface PDFGeneratorService {
    void exportEnrollmentsToPDF(HttpServletResponse response) throws IOException;
    void exportScheduleToPDF(HttpServletResponse response) throws IOException;
}
