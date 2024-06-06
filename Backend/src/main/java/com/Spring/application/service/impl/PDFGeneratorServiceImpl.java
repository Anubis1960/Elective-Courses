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
import java.util.*;
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

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);
        document.open();

        // Define fonts
        Font font = FontFactory.getFont(FontFactory.COURIER, 12, Color.BLACK);
        Font headerFont = FontFactory.getFont(FontFactory.COURIER, 14, Color.WHITE);

        // Create a table with 6 columns (Time slots + 5 days of the week)
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Set column widths
        float[] columnWidths = {2f, 2f, 2f, 2f, 2f, 2f};
        table.setWidths(columnWidths);

        // Create table header
        String[] headers = {"Time Slot", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
            headerCell.setBackgroundColor(Color.GRAY);
            table.addCell(headerCell);
        }

        // Define time slots
        String[] timeSlots = {"08:00-09:30", "09:40-11:10", "11:20-12:50", "13:00-14:30", "14:40-16:10", "16:20-17:50", "18:00-19:30", "19:40-21:10"};

        // Map to store the courses based on the day and time slot
        Map<String, Map<String, List<CourseSchedule>>> scheduleMap = new HashMap<>();
        for (String day : new String[]{"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"}) {
            scheduleMap.put(day, new HashMap<>());
            for (String timeSlot : timeSlots) {
                scheduleMap.get(day).put(timeSlot, new ArrayList<>());
            }
        }

        // Populate the schedule map
        for (CourseSchedule courseSchedule : courseSchedules) {
            String day = courseSchedule.getDay().toString().toUpperCase();
            String startTime = courseSchedule.getStartTime().toString();
            String endTime = courseSchedule.getEndTime().toString();
            String timeSlot = startTime + "-" + endTime;

            if (scheduleMap.containsKey(day)) {
                scheduleMap.get(day).get(timeSlot).add(courseSchedule);
            }
        }

        // Add rows to the table
        for (String timeSlot : timeSlots) {
            // Add time slot cell
            table.addCell(new PdfPCell(new Phrase(timeSlot, font)));

            // Add course cells for each day
            for (String day : new String[]{"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"}) {
                List<CourseSchedule> courses = scheduleMap.get(day).get(timeSlot);
                PdfPCell cell;
                if (!courses.isEmpty()) {
                    StringBuilder coursesText = new StringBuilder();
                    for (CourseSchedule course : courses) {
                        coursesText.append(course.getCourse().getCourseName()).append("\n");
                    }
                    cell = new PdfPCell(new Phrase(coursesText.toString(), font));
                } else {
                    cell = new PdfPCell(new Phrase("", font));
                }
                table.addCell(cell);
            }
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
