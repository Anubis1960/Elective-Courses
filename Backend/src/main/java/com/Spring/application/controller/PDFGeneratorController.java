package com.Spring.application.controller;

import com.Spring.application.service.PDFGeneratorService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/pdf")
public class PDFGeneratorController {
    @Autowired
    private PDFGeneratorService pdfGeneratorService;

    @GetMapping("/enrollments")
    public void exportEnrollmentsToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormater.format(System.currentTimeMillis());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=enrollments_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
        this.pdfGeneratorService.exportEnrollmentsToPDF(response);
    }

    @GetMapping("/schedule")
    public void exportScheduleToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormater.format(System.currentTimeMillis());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=schedule_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
        this.pdfGeneratorService.exportScheduleToPDF(response);
    }
}
