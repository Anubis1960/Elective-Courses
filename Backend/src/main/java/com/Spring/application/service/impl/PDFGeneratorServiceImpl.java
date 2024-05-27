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
import com.lowagie.text.*;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

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

//    @Override
//    public void exportEnrollmentsToPDF(OutputStream out) throws IOException {
//        List<Enrollment> enrollments = enrollmentRepository.findAllWhereStatusIsAccepted();
//        Document document = new Document(PageSize.A4);
//        PdfWriter.getInstance(document, out);
//        document.open();
//
//        // Define font
//        Font font = FontFactory.getFont(FontFactory.COURIER, 12, Color.BLACK);
//        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
//
//        // Create a table with 2 columns
//        PdfPTable table = new PdfPTable(2);
//        table.setWidthPercentage(100);
//        table.setSpacingBefore(10f);
//        table.setSpacingAfter(10f);
//
//        // Set column widths
//        float[] columnWidths = {3f, 3f};
//        table.setWidths(columnWidths);
//
//        // Create table header
//        PdfPCell header1 = new PdfPCell(new Phrase("Student Name", headerFont));
//        header1.setBackgroundColor(Color.GRAY);
//        header1.setHorizontalAlignment(Element.ALIGN_CENTER);
//        header1.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table.addCell(header1);
//
//        PdfPCell header2 = new PdfPCell(new Phrase("Course Name", headerFont));
//        header2.setBackgroundColor(Color.GRAY);
//        header2.setHorizontalAlignment(Element.ALIGN_CENTER);
//        header2.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table.addCell(header2);
//
//        // Add rows to the table
//        for (Enrollment enrollment : enrollments) {
//            PdfPCell studentCell = new PdfPCell(new Phrase(enrollment.getStudent().getName(), font));
//            studentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            studentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table.addCell(studentCell);
//
//            PdfPCell courseCell = new PdfPCell(new Phrase(enrollment.getCourse().getCourseName(), font));
//            courseCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            courseCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table.addCell(courseCell);
//        }
//
//        // Add table to document
//        document.add(table);
//        document.close();
//    }


    @Override
    public void exportScheduleToPDF(OutputStream out, Long id) throws IOException {
        List<CourseSchedule> courseSchedules = courseScheduleRepository.findCourseScheduleOfStudent(id);
        for(CourseSchedule courseSchedule : courseSchedules){
            System.out.println(courseSchedule);
        }
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);
        document.open();

        // Define font
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, Color.BLACK);
        Font headerFont = FontFactory.getFont(FontFactory.COURIER, 16, Color.WHITE);

        // Create a table with 4 columns
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Set column widths
        float[] columnWidths = {2f, 2f, 2f, 2f};
        table.setWidths(columnWidths);

        // Create table header
        PdfPCell header1 = new PdfPCell(new com.lowagie.text.Phrase("Course Name", headerFont));
        header1.setBackgroundColor(Color.GRAY);
        table.addCell(header1);

        PdfPCell header2 = new PdfPCell(new Phrase("Day", headerFont));
        header2.setBackgroundColor(Color.GRAY);
        table.addCell(header2);

        PdfPCell header3 = new PdfPCell(new Phrase("Start Time", headerFont));
        header3.setBackgroundColor(Color.GRAY);
        table.addCell(header3);

        PdfPCell header4 = new PdfPCell(new Phrase("End Time", headerFont));
        header4.setBackgroundColor(Color.GRAY);
        table.addCell(header4);

        // Add rows to the table
        for (CourseSchedule courseSchedule : courseSchedules) {
            table.addCell(new PdfPCell(new Phrase(courseSchedule.getCourse().getCourseName(), font)));
            table.addCell(new PdfPCell(new Phrase(courseSchedule.getDay().toString(), font)));
            table.addCell(new PdfPCell(new Phrase(courseSchedule.getStartTime().toString(), font)));
            table.addCell(new PdfPCell(new Phrase(courseSchedule.getEndTime().toString(), font)));
        }

        // Add table to document
        document.add(table);
        document.close();
    }



    @Override
    public void exportStudentsOfCourseToPDF(OutputStream out, Long id) throws IOException {
        List<Student> students = studentRepository.findAcceptedStudentsByCourseId(id);
        Course course = courseRepository.findById(id).orElse(null);

        if (course == null) {
            throw new IllegalArgumentException("Course with ID " + id + " not found.");
        }

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);
        document.open();

        // Define fonts
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.BLACK);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);

        // Add title
        Paragraph title = new Paragraph("Course: " + course.getCourseName() + "    Faculty Section: " + course.getFacultySection() + "    Year: " + course.getYear(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" ")); // Add a blank line

        // Create a table with 3 columns
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Set column widths
        float[] columnWidths = {3f, 2f, 4f};
        table.setWidths(columnWidths);

        // Create table header
        PdfPCell header1 = new PdfPCell(new Phrase("Name", headerFont));
        header1.setBackgroundColor(Color.GRAY);
        header1.setHorizontalAlignment(Element.ALIGN_CENTER);
        header1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(header1);

        PdfPCell header2 = new PdfPCell(new Phrase("Grade", headerFont));
        header2.setBackgroundColor(Color.GRAY);
        header2.setHorizontalAlignment(Element.ALIGN_CENTER);
        header2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(header2);

        PdfPCell header3 = new PdfPCell(new Phrase("Email", headerFont));
        header3.setBackgroundColor(Color.GRAY);
        header3.setHorizontalAlignment(Element.ALIGN_CENTER);
        header3.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(header3);

        // Add rows to the table
        for (Student student : students) {
            PdfPCell nameCell = new PdfPCell(new Phrase(student.getName(), cellFont));
            nameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            nameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(nameCell);

            PdfPCell gradeCell = new PdfPCell(new Phrase(student.getGrade().toString(), cellFont));
            gradeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            gradeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(gradeCell);

            PdfPCell emailCell = new PdfPCell(new Phrase(student.getEmail(), cellFont));
            emailCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            emailCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(emailCell);
        }

        // Add table to document
        document.add(table);
        document.close();
    }

//    @Override
//    public void exportEnrollmentByFacultySectionToPDF(OutputStream out, String facultySection) throws IOException {
//        List<Enrollment> enrollments = enrollmentRepository.findEnrollmentByFacultySectionAndStatusIsAccepted(FacultySection.valueOf(facultySection));
//
//        Document document = new Document(PageSize.A4);
//        PdfWriter.getInstance(document, out);
//        document.open();
//
//        // Define fonts
//        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.BLACK);
//        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
//        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);
//
//        // Add title
//        Paragraph title = new Paragraph("Faculty Section: " + facultySection, titleFont);
//        title.setAlignment(Element.ALIGN_CENTER);
//        document.add(title);
//        document.add(new Paragraph(" ")); // Add a blank line
//
//        // Create a table with 3 columns
//        PdfPTable table = new PdfPTable(3);
//        table.setWidthPercentage(100);
//        table.setSpacingBefore(10f);
//        table.setSpacingAfter(10f);
//
//        // Set column widths
//        float[] columnWidths = {3f, 3f, 2f};
//        table.setWidths(columnWidths);
//
//        // Create table header
//        PdfPCell header1 = new PdfPCell(new Phrase("Student", headerFont));
//        header1.setBackgroundColor(Color.GRAY);
//        header1.setHorizontalAlignment(Element.ALIGN_CENTER);
//        header1.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table.addCell(header1);
//
//        PdfPCell header2 = new PdfPCell(new Phrase("Course", headerFont));
//        header2.setBackgroundColor(Color.GRAY);
//        header2.setHorizontalAlignment(Element.ALIGN_CENTER);
//        header2.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table.addCell(header2);
//
//        PdfPCell header3 = new PdfPCell(new Phrase("Year", headerFont));
//        header3.setBackgroundColor(Color.GRAY);
//        header3.setHorizontalAlignment(Element.ALIGN_CENTER);
//        header3.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table.addCell(header3);
//
//        // Add rows to the table
//        for (Enrollment enrollment : enrollments) {
//            PdfPCell studentCell = new PdfPCell(new Phrase(enrollment.getStudent().getName(), cellFont));
//            studentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            studentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table.addCell(studentCell);
//
//            PdfPCell courseCell = new PdfPCell(new Phrase(enrollment.getCourse().getCourseName(), cellFont));
//            courseCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            courseCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table.addCell(courseCell);
//
//            PdfPCell yearCell = new PdfPCell(new Phrase(enrollment.getCourse().getYear().toString(), cellFont));
//            yearCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            yearCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table.addCell(yearCell);
//        }
//
//        // Add table to document
//        document.add(table);
//        document.close();
//    }


//    @Override
//    public void exportEnrollmentByYearToPDF(OutputStream out, Integer year) throws IOException {
//        List<Enrollment> enrollments = enrollmentRepository.findEnrollmentByYearAndStatusIsAccepted(year);
//
//        Document document = new Document(PageSize.A4);
//        PdfWriter.getInstance(document, out);
//        document.open();
//
//        // Define fonts
//        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.BLACK);
//        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
//        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);
//
//        // Add title
//        Paragraph title = new Paragraph("Year: " + year, titleFont);
//        title.setAlignment(Element.ALIGN_CENTER);
//        document.add(title);
//        document.add(new Paragraph(" ")); // Add a blank line
//
//        // Create a table with 3 columns
//        PdfPTable table = new PdfPTable(3);
//        table.setWidthPercentage(100);
//        table.setSpacingBefore(10f);
//        table.setSpacingAfter(10f);
//
//        // Set column widths
//        float[] columnWidths = {3f, 3f, 3f};
//        table.setWidths(columnWidths);
//
//        // Create table header
//        PdfPCell header1 = new PdfPCell(new Phrase("Student", headerFont));
//        header1.setBackgroundColor(Color.GRAY);
//        header1.setHorizontalAlignment(Element.ALIGN_CENTER);
//        header1.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table.addCell(header1);
//
//        PdfPCell header2 = new PdfPCell(new Phrase("Course", headerFont));
//        header2.setBackgroundColor(Color.GRAY);
//        header2.setHorizontalAlignment(Element.ALIGN_CENTER);
//        header2.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table.addCell(header2);
//
//        PdfPCell header3 = new PdfPCell(new Phrase("Faculty Section", headerFont));
//        header3.setBackgroundColor(Color.GRAY);
//        header3.setHorizontalAlignment(Element.ALIGN_CENTER);
//        header3.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table.addCell(header3);
//
//        // Add rows to the table
//        for (Enrollment enrollment : enrollments) {
//            PdfPCell studentCell = new PdfPCell(new Phrase(enrollment.getStudent().getName(), cellFont));
//            studentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            studentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table.addCell(studentCell);
//
//            PdfPCell courseCell = new PdfPCell(new Phrase(enrollment.getCourse().getCourseName(), cellFont));
//            courseCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            courseCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table.addCell(courseCell);
//
//            PdfPCell facultySectionCell = new PdfPCell(new Phrase(enrollment.getCourse().getFacultySection().toString(), cellFont));
//            facultySectionCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            facultySectionCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table.addCell(facultySectionCell);
//        }
//
//        // Add table to document
//        document.add(table);
//        document.close();
//    }

//    @Override
//    public void exportEnrollmentByFacultySectionAndYearToPDF(OutputStream out, String facultySection, Integer year) throws IOException {
//        List<Enrollment> enrollments = enrollmentRepository.findEnrollmentByFacultySectionAndYearAndStatusIsAccepted(FacultySection.valueOf(facultySection), year);
//
//        Document document = new Document(PageSize.A4);
//        PdfWriter.getInstance(document, out);
//        document.open();
//
//        // Define fonts
//        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.BLACK);
//        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
//        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);
//
//        // Add title
//        Paragraph title = new Paragraph("Faculty Section: " + facultySection + "    Year: " + year, titleFont);
//        title.setAlignment(Element.ALIGN_CENTER);
//        document.add(title);
//        document.add(new Paragraph(" ")); // Add a blank line
//
//        // Create a table with 3 columns
//        PdfPTable table = new PdfPTable(3);
//        table.setWidthPercentage(100);
//        table.setSpacingBefore(10f);
//        table.setSpacingAfter(10f);
//
//        // Set column widths
//        float[] columnWidths = {3f, 3f, 3f};
//        table.setWidths(columnWidths);
//
//        // Create table header
//        PdfPCell header1 = new PdfPCell(new Phrase("Student", headerFont));
//        header1.setBackgroundColor(Color.GRAY);
//        header1.setHorizontalAlignment(Element.ALIGN_CENTER);
//        header1.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table.addCell(header1);
//
//        PdfPCell header2 = new PdfPCell(new Phrase("Course", headerFont));
//        header2.setBackgroundColor(Color.GRAY);
//        header2.setHorizontalAlignment(Element.ALIGN_CENTER);
//        header2.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table.addCell(header2);
//
//        PdfPCell header3 = new PdfPCell(new Phrase("Faculty Section", headerFont));
//        header3.setBackgroundColor(Color.GRAY);
//        header3.setHorizontalAlignment(Element.ALIGN_CENTER);
//        header3.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table.addCell(header3);
//
//        // Add rows to the table
//        for (Enrollment enrollment : enrollments) {
//            PdfPCell studentCell = new PdfPCell(new Phrase(enrollment.getStudent().getName(), cellFont));
//            studentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            studentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table.addCell(studentCell);
//
//            PdfPCell courseCell = new PdfPCell(new Phrase(enrollment.getCourse().getCourseName(), cellFont));
//            courseCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            courseCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table.addCell(courseCell);
//
//            PdfPCell facultySectionCell = new PdfPCell(new Phrase(enrollment.getCourse().getFacultySection().toString(), cellFont));
//            facultySectionCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            facultySectionCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table.addCell(facultySectionCell);
//        }
//
//        // Add table to document
//        document.add(table);
//        document.close();
//    }

    @Override
    public void exportEnrollments(OutputStream outputStream, Optional<String> facultySection, Optional<Integer> year) throws IOException {
        List<Enrollment> enrollments;
        if (facultySection.isPresent() && year.isPresent()) {
            enrollments = enrollmentRepository.findEnrollmentByFacultySectionAndYearAndStatusIsAccepted(FacultySection.valueOf(facultySection.get()), year.get());
        } else if (facultySection.isPresent()) {
            enrollments = enrollmentRepository.findEnrollmentByFacultySectionAndStatusIsAccepted(FacultySection.valueOf(facultySection.get()));
        } else if (year.isPresent()) {
            enrollments = enrollmentRepository.findEnrollmentByYearAndStatusIsAccepted(year.get());
        } else {
            enrollments = enrollmentRepository.findAllWhereStatusIsAccepted();
        }

        writeEnrollmentsToPDF(outputStream, enrollments);
    }


    @Override
    public void writeEnrollmentsToPDF(OutputStream outputStream, List<Enrollment> enrollments) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        // Define fonts
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.BLACK);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);

        // Add title
        Paragraph title = new Paragraph("Enrollments ", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" ")); // Add a blank line

        // Create a table with 3 columns
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Set column widths
        float[] columnWidths = {3f, 3f, 3f, 3f};
        table.setWidths(columnWidths);

        // Create table header
        PdfPCell header1 = new PdfPCell(new Phrase("Student", headerFont));
        header1.setBackgroundColor(Color.GRAY);
        header1.setHorizontalAlignment(Element.ALIGN_CENTER);
        header1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(header1);

        PdfPCell header2 = new PdfPCell(new Phrase("Course", headerFont));
        header2.setBackgroundColor(Color.GRAY);
        header2.setHorizontalAlignment(Element.ALIGN_CENTER);
        header2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(header2);

        PdfPCell header3 = new PdfPCell(new Phrase("Faculty Section", headerFont));
        header3.setBackgroundColor(Color.GRAY);
        header3.setHorizontalAlignment(Element.ALIGN_CENTER);
        header3.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(header3);

        PdfPCell header4 = new PdfPCell(new Phrase("Year", headerFont));
        header4.setBackgroundColor(Color.GRAY);
        header4.setHorizontalAlignment(Element.ALIGN_CENTER);
        header4.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(header4);

        // Add rows to the table
        for (Enrollment enrollment : enrollments) {
            PdfPCell studentCell = new PdfPCell(new Phrase(enrollment.getStudent().getName(), cellFont));
            studentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            studentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(studentCell);

            PdfPCell courseCell = new PdfPCell(new Phrase(enrollment.getCourse().getCourseName(), cellFont));
            courseCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            courseCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(courseCell);

            PdfPCell facultySectionCell = new PdfPCell(new Phrase(enrollment.getCourse().getFacultySection().toString(), cellFont));
            facultySectionCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            facultySectionCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(facultySectionCell);

            PdfPCell yearCell = new PdfPCell(new Phrase(enrollment.getStudent().getYear().toString(), cellFont));
            yearCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            yearCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(yearCell);
        }

        // Add table to document
        document.add(table);
        document.close();
    }
}
