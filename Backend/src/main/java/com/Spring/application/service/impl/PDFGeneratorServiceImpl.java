package com.Spring.application.service.impl;

import com.Spring.application.entity.Course;
import com.Spring.application.entity.CourseSchedule;
import com.Spring.application.entity.Enrollment;
import com.Spring.application.entity.Student;
import com.Spring.application.enums.FacultySection;
import com.Spring.application.repository.CourseRepository;
import com.Spring.application.repository.CourseScheduleRepository;
import com.Spring.application.repository.EnrollmentRepository;
import com.Spring.application.repository.StudentRepository;
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
import java.io.OutputStream;
import java.util.List;

@Service
public class PDFGeneratorServiceImpl implements PDFGeneratorService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private CourseScheduleRepository courseScheduleRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public void exportEnrollmentsToPDF(OutputStream out) throws IOException {
        List<Enrollment> enrollments = enrollmentRepository.findAllWhereStatusIsAccepted();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);
        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, Color.BLACK);
        for (Enrollment enrollment : enrollments) {
            document.add(new Paragraph(enrollment.getStudent().getName()+" "+enrollment.getCourse().getCourseName(), font));
            document.add(new Paragraph(" "));
        }
        document.close();
    }

    @Override
    public void exportScheduleToPDF(OutputStream out,Long id) throws IOException{
        List<CourseSchedule> courseSchedules = courseScheduleRepository.findCourseScheduleOfStudent(id);
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);
        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, Color.BLACK);
        for (CourseSchedule courseSchedule : courseSchedules) {
            document.add(new Paragraph(courseSchedule.getCourse().getCourseName()+" "+courseSchedule.getDay()+" "+courseSchedule.getStartTime()+" "+courseSchedule.getEndTime(), font));
            document.add(new Paragraph(" "));
        }
        document.close();
    }

    @Override
    public void exportStudentsOfCourseToPDF(OutputStream out,Long id) throws IOException{
        List<Student> students = studentRepository.findAcceptedStudentsByCourseId(id);
        Course course = courseRepository.findById(id).orElse(null);
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);
        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, Color.BLACK);
        document.add(new Paragraph("Course: " + course.getCourseName() + "    Faculty Section: " + course.getFacultySection() + "     Year: " + course.getYear(), font));
        for (Student student : students) {
            document.add(new Paragraph("Name: " + student.getName() + "     Grade: " + student.getGrade() + "       Email: " + student.getEmail(), font));
            document.add(new Paragraph(" "));
        }
        document.close();
    }

    @Override
    public void exportEnrollmentByFacultySectionToPDF(OutputStream out,String facultySection) throws IOException{
        List<Enrollment> enrollments = enrollmentRepository.findEnrollmentByFacultySectionAndStatusIsAccepted(FacultySection.valueOf(facultySection));
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);
        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, Color.BLACK);
        document.add(new Paragraph("Faculty Section: " + facultySection, font));
        for (Enrollment enrollment : enrollments) {
            document.add(new Paragraph("Student: " + enrollment.getStudent().getName() + "     Course: " + enrollment.getCourse().getCourseName() + "       Year: " + enrollment.getCourse().getYear(), font));
            document.add(new Paragraph(" "));
        }
        document.close();
    }

    @Override
    public void exportEnrollmentByYearToPDF(OutputStream out, Integer year) throws IOException {
        List<Enrollment> enrollments = enrollmentRepository.findEnrollmentByYearAndStatusIsAccepted(year);
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);
        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, Color.BLACK);
        document.add(new Paragraph("Year: " + year, font));
        for (Enrollment enrollment : enrollments) {
            document.add(new Paragraph("Student: " + enrollment.getStudent().getName() + "     Course: " + enrollment.getCourse().getCourseName() + "       Faculty Section: " + enrollment.getCourse().getFacultySection(), font));
            document.add(new Paragraph(" "));
        }
        document.close();
    }
}
