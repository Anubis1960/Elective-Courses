package com.Spring.application.service;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface PDFGeneratorService {
    byte[] exportEnrollmentsToPDF() throws IOException;
    byte[] exportScheduleToPDF(Long id) throws IOException;
}
