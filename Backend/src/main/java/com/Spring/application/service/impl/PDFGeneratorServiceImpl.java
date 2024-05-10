package com.Spring.application.service.impl;

import com.Spring.application.entity.CourseSchedule;
import com.Spring.application.entity.Enrollment;
import com.Spring.application.repository.CourseRepository;
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

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class PDFGeneratorServiceImpl implements PDFGeneratorService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private CourseScheduleRepository courseScheduleRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Override
    public byte[] exportEnrollmentsToPDF() throws IOException {
        List<Enrollment> enrollments = enrollmentRepository.findAllWhereStatusIsAccepted();
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter pdfWriter = PdfWriter.getInstance(document, out);
        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, Color.BLACK);
        for (Enrollment enrollment : enrollments) {
            document.add(new Paragraph(enrollment.getStudent().getName()+" "+enrollment.getCourse().getCourseName(), font));
            pdfWriter.flush();
        }
        document.close();
        return out.toByteArray();
    }

    @Override
    public byte[] exportScheduleToPDF(Long id) throws IOException{
        List<CourseSchedule> courseSchedules = courseScheduleRepository.findCourseScheduleOfStudent(id);
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter pdfWriter = PdfWriter.getInstance(document, out);
        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, Color.BLACK);
        for (CourseSchedule courseSchedule : courseSchedules) {
            document.add(new Paragraph(courseSchedule.getCourse().getCourseName()+" "+courseSchedule.getDay()+" "+courseSchedule.getStartTime()+" "+courseSchedule.getEndTime(), font));
            pdfWriter.flush();
        }
        document.close();
        return out.toByteArray();

    }
}
