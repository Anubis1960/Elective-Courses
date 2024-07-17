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
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Spring.application.utils.GeneratorMethods;

import java.awt.*;
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

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public List<Enrollment> executeQuery(String query){
        return entityManager.createQuery(query, Enrollment.class).getResultList();
    }

    @Override
    public void exportStudents(OutputStream out, Optional<String> facultySection, Optional<Integer> year,boolean includeId, boolean includeName, boolean includeEmail, boolean includeGrade, boolean includeSection, boolean includeYear) throws IOException {
        List<Student> students;
        if (facultySection.isPresent() && year.isPresent()) {
            students = entityManager.createQuery("SELECT s FROM Student s WHERE s.facultySection = :facultySection AND s.year = :year", Student.class)
                    .setParameter("facultySection", FacultySection.valueOf(facultySection.get()))
                    .setParameter("year", year.get())
                    .getResultList();
        } else if (facultySection.isPresent()) {
            students = entityManager.createQuery("SELECT s FROM Student s WHERE s.facultySection = :facultySection", Student.class)
                    .setParameter("facultySection", FacultySection.valueOf(facultySection.get()))
                    .getResultList();
        } else if (year.isPresent()) {
            students = entityManager.createQuery("SELECT s FROM Student s WHERE s.year = :year", Student.class)
                    .setParameter("year", year.get())
                    .getResultList();
        } else {
            students = studentRepository.findAll();
        }

        writeStudentsToPDF(out, students, includeId, includeName, includeEmail, includeGrade, includeSection, includeYear);
    }


    public void writeStudentsToPDF(OutputStream out, List<Student> students,
                                   boolean includeId, boolean includeName, boolean includeEmail,
                                   boolean includeGrade, boolean includeSection, boolean includeYear) {

        // Set up document
        Document document = GeneratorMethods.setUpDocument(out);

        // Define fonts
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);

        // Add title
        GeneratorMethods.addTitleToPDF("Students", document);

        // Calculate number of columns based on include flags
        int numColumns = 0;
        if (includeId) numColumns++;
        if (includeName) numColumns++;
        if (includeEmail) numColumns++;
        if (includeGrade) numColumns++;
        if (includeSection) numColumns++;
        if (includeYear) numColumns++;

        // Create table with calculated number of columns
        PdfPTable table = new PdfPTable(numColumns);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Set column widths evenly
        float[] columnWidths = new float[numColumns];
        for (int i = 0; i < numColumns; i++) {
            columnWidths[i] = 1f;
        }
        table.setWidths(columnWidths);

        // Add table headers
        if (includeId) {
            GeneratorMethods.addHeadersToPDF("ID", headerFont, table);
        }

        if (includeName) {
            GeneratorMethods.addHeadersToPDF("Name", headerFont, table);
        }

        if (includeEmail) {
            GeneratorMethods.addHeadersToPDF("Email", headerFont, table);
        }

        if (includeGrade) {
            GeneratorMethods.addHeadersToPDF("Grade", headerFont, table);
        }

        if (includeSection) {
            GeneratorMethods.addHeadersToPDF("Faculty Section", headerFont, table);
        }

        if (includeYear) {
            GeneratorMethods.addHeadersToPDF("Year", headerFont, table);
        }

        // Add rows to the table
        for (Student student : students) {
            if (includeId) {
                GeneratorMethods.addDataForPDF(student.getId().toString(), cellFont, table);
            }

            if (includeName) {
                GeneratorMethods.addDataForPDF(student.getName(), cellFont, table);
            }

            if (includeEmail) {
                GeneratorMethods.addDataForPDF(student.getEmail(), cellFont, table);
            }

            if (includeGrade) {
                GeneratorMethods.addDataForPDF(student.getGrade().toString(), cellFont, table);
            }

            if (includeSection) {
                GeneratorMethods.addDataForPDF(student.getFacultySection().toString(), cellFont, table);
            }

            if (includeYear) {
                GeneratorMethods.addDataForPDF(student.getYear().toString(), cellFont, table);
            }
        }

        // Add table to document
        document.add(table);
        // Close document
        document.close();
    }


    @Override
    public void exportScheduleToPDF(OutputStream out, Long id) throws IOException {
        List<CourseSchedule> courseSchedules = courseScheduleRepository.findCourseScheduleOfStudent(id);

        // Set up document
        Document document = GeneratorMethods.setUpDocument(out);

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
            GeneratorMethods.addHeadersToPDF(header, headerFont, table);
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
        // Close document
        document.close();
    }


    @Override
    public void exportStudentsOfCourseToPDF(OutputStream out, Long id) throws IOException {
        List<Student> students = studentRepository.findAcceptedStudentsByCourseId(id);
        Course course = courseRepository.findById(id).orElse(null);

        if (course == null) {
            throw new IllegalArgumentException("Course with ID " + id + " not found.");
        }
        
        // Set up document
        Document document = GeneratorMethods.setUpDocument(out);

        // Define fonts
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);

        // Add title
        GeneratorMethods.addTitleToPDF("Course : " + course.getCourseName(), document);

        // Create a table with 3 columns
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Set column widths
        float[] columnWidths = {3f, 2f, 4f};
        table.setWidths(columnWidths);

        // Create table header
        GeneratorMethods.addHeadersToPDF("Name", headerFont, table);
        GeneratorMethods.addHeadersToPDF("Grade", headerFont, table);
        GeneratorMethods.addHeadersToPDF("Email", headerFont, table);

        // Add rows to the table
        for (Student student : students) {
            GeneratorMethods.addDataForPDF(student.getName(), cellFont, table);
            GeneratorMethods.addDataForPDF(student.getGrade().toString(), cellFont, table);
            GeneratorMethods.addDataForPDF(student.getEmail(), cellFont, table);
        }

        // Add table to document
        document.add(table);
        // Close document
        document.close();
    }

    @Override
    public void exportEnrollments(OutputStream outputStream, Optional<String> facultySection, Optional<Integer> year, boolean includeEnrollmentId, boolean includeStudentId, boolean includeCourseId, boolean includeYear, boolean IncludeSection, boolean includeCourseName, boolean includeStudentName, boolean includeTeacher, boolean includeStudentMail, boolean includeGrade, boolean includeCategory) throws IOException {
        List<Enrollment> enrollments;
        if (facultySection.isPresent() && year.isPresent()) {
            System.out.println();
            enrollments = enrollmentRepository.findEnrollmentByFacultySectionAndYearAndStatusIsAccepted(FacultySection.valueOf(facultySection.get()), year.get());
        } else if (facultySection.isPresent()) {
            enrollments = enrollmentRepository.findEnrollmentByFacultySectionAndStatusIsAccepted(FacultySection.valueOf(facultySection.get()));
        } else if (year.isPresent()) {
            enrollments = enrollmentRepository.findEnrollmentByYearAndStatusIsAccepted(year.get());
        } else {
            enrollments = enrollmentRepository.findAllWhereStatusIsAccepted();
        }

        writeEnrollmentsToPDF(outputStream, enrollments, includeEnrollmentId, includeStudentId, includeCourseId, includeYear, IncludeSection, includeCourseName, includeStudentName, includeTeacher, includeStudentMail, includeGrade, includeCategory);
    }

    public void writeEnrollmentsToPDF(OutputStream outputStream, List<Enrollment> enrollments,
                                      boolean includeEnrollmentId, boolean includeStudentId, boolean includeCourseId, boolean includeYear,
                                      boolean includeSection, boolean includeCourseName, boolean includeStudentName,
                                      boolean includeTeacher, boolean includeStudentMail, boolean includeGrade,
                                      boolean includeCategory) {
        
        // Set up document
        Document document = GeneratorMethods.setUpDocument(outputStream);

        // Define fonts
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.BLACK);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);

        // Add title
        GeneratorMethods.addTitleToPDF("Enrollments", document);

        // Calculate number of columns based on include flags
        int numColumns = 0;
        if (includeEnrollmentId) numColumns++;
        if (includeStudentId) numColumns++;
        if (includeCourseId) numColumns++;
        if (includeYear) numColumns++;
        if (includeSection) numColumns++;
        if (includeCourseName) numColumns++;
        if (includeStudentName) numColumns++;
        if (includeTeacher) numColumns++;
        if (includeStudentMail) numColumns++;
        if (includeGrade) numColumns++;
        if (includeCategory) numColumns++;

        // Create table with calculated number of columns
        PdfPTable table = new PdfPTable(numColumns);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Set column widths evenly
        float[] columnWidths = new float[numColumns];
        for (int i = 0; i < numColumns; i++) {
            columnWidths[i] = 1f;
        }
        table.setWidths(columnWidths);

        // Add table headers
        if (includeEnrollmentId) {
            GeneratorMethods.addHeadersToPDF("ID", headerFont, table);
        }

        if (includeStudentId) {
            GeneratorMethods.addHeadersToPDF("StudentID", headerFont, table);
        }

        if (includeCourseId) {
            GeneratorMethods.addHeadersToPDF("CourseID", headerFont, table);
        }

        if (includeYear) {
            GeneratorMethods.addHeadersToPDF("Year", headerFont, table);
        }

        if (includeSection) {
            GeneratorMethods.addHeadersToPDF("Faculty Section", headerFont, table);
        }

        if (includeCourseName) {
            GeneratorMethods.addHeadersToPDF("Course Name", headerFont, table);
        }

        if (includeStudentName) {
            GeneratorMethods.addHeadersToPDF("Student Name", headerFont, table);
        }

        if (includeTeacher) {
            GeneratorMethods.addHeadersToPDF("Teacher", headerFont, table);
        }

        if (includeStudentMail) {
            GeneratorMethods.addHeadersToPDF("Student Mail", headerFont, table);
        }

        if (includeGrade) {
            GeneratorMethods.addHeadersToPDF("Grade", headerFont, table);
        }

        if (includeCategory) {
            GeneratorMethods.addHeadersToPDF("Category", headerFont, table);
        }

        // Add rows to the table
        for (Enrollment enrollment : enrollments) {
            if (includeEnrollmentId) {
                GeneratorMethods.addDataForPDF(enrollment.getEnrollmentId().toString(), cellFont, table);
            }

            if (includeStudentId) {
                GeneratorMethods.addDataForPDF(enrollment.getStudent().getId().toString(), cellFont, table);
            }

            if (includeCourseId) {
                GeneratorMethods.addDataForPDF(enrollment.getCourse().getCourseId().toString(), cellFont, table);
            }

            if (includeYear) {
                GeneratorMethods.addDataForPDF(enrollment.getStudent().getYear().toString(), cellFont, table);
            }

            if (includeSection) {
                GeneratorMethods.addDataForPDF(enrollment.getStudent().getFacultySection().toString(), cellFont, table);
            }

            if (includeCourseName) {
                GeneratorMethods.addDataForPDF(enrollment.getCourse().getCourseName(), cellFont, table);
            }

            if (includeStudentName) {
                GeneratorMethods.addDataForPDF(enrollment.getStudent().getName(), cellFont, table);
            }

            if (includeTeacher) {
                GeneratorMethods.addDataForPDF(enrollment.getCourse().getTeacherName(), cellFont, table);
            }

            if (includeStudentMail) {
                GeneratorMethods.addDataForPDF(enrollment.getStudent().getEmail(), cellFont, table);
            }

            if (includeGrade) {
                GeneratorMethods.addDataForPDF(enrollment.getStudent().getGrade().toString(), cellFont, table);
            }

            if (includeCategory) {
                GeneratorMethods.addDataForPDF(enrollment.getCourse().getCategory(), cellFont, table);
            }
        }

        // Add table to document
        document.add(table);
        // Close document
        document.close();
    }
}
