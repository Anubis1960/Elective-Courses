package com.Spring.application.service.impl;

import com.Spring.application.entity.Enrollment;
import com.Spring.application.repository.CourseScheduleRepository;
import com.Spring.application.repository.EnrollmentRepository;
import com.Spring.application.service.PDFGeneratorService;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PDFGeneratorServiceImpl implements PDFGeneratorService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private CourseScheduleRepository courseScheduleRepository;

    @Override
    public void exportEnrollmentsToPDF(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        font.setSize(12);
        for (Enrollment enrollment : enrollments) {
            document.add(new Paragraph(enrollment.getStudent().getName()+" "+enrollment.getCourse().getCourseName(), font));

        }
        document.close();
    }

    @Override
    public void exportScheduleToPDF(HttpServletResponse response) throws IOException{

    }
}
