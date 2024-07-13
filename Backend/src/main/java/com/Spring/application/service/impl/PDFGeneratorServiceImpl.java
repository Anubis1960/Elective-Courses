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
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Define fonts
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.BLACK);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);

            // Add title
            Paragraph title = new Paragraph("Students", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ")); // Add a blank line

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
                PdfPCell idHeader = new PdfPCell(new Paragraph("ID", headerFont));
                idHeader.setBackgroundColor(Color.GRAY);
                idHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(idHeader);
            }

            if (includeName) {
                PdfPCell nameHeader = new PdfPCell(new Paragraph("Name", headerFont));
                nameHeader.setBackgroundColor(Color.GRAY);
                nameHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(nameHeader);
            }

            if (includeEmail) {
                PdfPCell emailHeader = new PdfPCell(new Paragraph("Email", headerFont));
                emailHeader.setBackgroundColor(Color.GRAY);
                emailHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(emailHeader);
            }

            if (includeGrade) {
                PdfPCell gradeHeader = new PdfPCell(new Paragraph("Grade", headerFont));
                gradeHeader.setBackgroundColor(Color.GRAY);
                gradeHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(gradeHeader);
            }

            if (includeSection) {
                PdfPCell sectionHeader = new PdfPCell(new Paragraph("Section", headerFont));
                sectionHeader.setBackgroundColor(Color.GRAY);
                sectionHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(sectionHeader);
            }

            if (includeYear) {
                PdfPCell yearHeader = new PdfPCell(new Paragraph("Year", headerFont));
                yearHeader.setBackgroundColor(Color.GRAY);
                yearHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(yearHeader);
            }

            // Add rows to the table
            for (Student student : students) {
                if (includeId) {
                    PdfPCell idCell = new PdfPCell(new Paragraph(student.getId().toString(), cellFont));
                    idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(idCell);
                }

                if (includeName) {
                    PdfPCell nameCell = new PdfPCell(new Paragraph(student.getName(), cellFont));
                    nameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(nameCell);
                }

                if (includeEmail) {
                    PdfPCell emailCell = new PdfPCell(new Paragraph(student.getEmail(), cellFont));
                    emailCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(emailCell);
                }

                if (includeGrade) {
                    PdfPCell gradeCell = new PdfPCell(new Paragraph(student.getGrade().toString(), cellFont));
                    gradeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(gradeCell);
                }

                if (includeSection) {
                    PdfPCell sectionCell = new PdfPCell(new Paragraph(student.getFacultySection().toString(), cellFont)); // Assuming getSection() retrieves the section
                    sectionCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(sectionCell);
                }

                if (includeYear) {
                    PdfPCell yearCell = new PdfPCell(new Paragraph(String.valueOf(student.getYear()), cellFont)); // Assuming getYear() retrieves the year
                    yearCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(yearCell);
                }
            }

            // Add table to document
            document.add(table);

        } catch (com.lowagie.text.DocumentException e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }


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

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Define fonts
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.BLACK);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);

            // Add title
            Paragraph title = new Paragraph("Enrollments", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ")); // Add a blank line

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
                PdfPCell enrollmentIdHeader = new PdfPCell(new Paragraph("Enrollment ID", headerFont));
                enrollmentIdHeader.setBackgroundColor(Color.GRAY);
                enrollmentIdHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(enrollmentIdHeader);
            }

            if (includeStudentId) {
                PdfPCell studentIdHeader = new PdfPCell(new Paragraph("Student ID", headerFont));
                studentIdHeader.setBackgroundColor(Color.GRAY);
                studentIdHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(studentIdHeader);
            }

            if (includeCourseId) {
                PdfPCell courseIdHeader = new PdfPCell(new Paragraph("Course ID", headerFont));
                courseIdHeader.setBackgroundColor(Color.GRAY);
                courseIdHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(courseIdHeader);
            }

            if (includeYear) {
                PdfPCell yearHeader = new PdfPCell(new Paragraph("Year", headerFont));
                yearHeader.setBackgroundColor(Color.GRAY);
                yearHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(yearHeader);
            }

            if (includeSection) {
                PdfPCell sectionHeader = new PdfPCell(new Paragraph("Section", headerFont));
                sectionHeader.setBackgroundColor(Color.GRAY);
                sectionHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(sectionHeader);
            }

            if (includeCourseName) {
                PdfPCell courseNameHeader = new PdfPCell(new Paragraph("Course Name", headerFont));
                courseNameHeader.setBackgroundColor(Color.GRAY);
                courseNameHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(courseNameHeader);
            }

            if (includeStudentName) {
                PdfPCell studentNameHeader = new PdfPCell(new Paragraph("Student Name", headerFont));
                studentNameHeader.setBackgroundColor(Color.GRAY);
                studentNameHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(studentNameHeader);
            }

            if (includeTeacher) {
                PdfPCell teacherHeader = new PdfPCell(new Paragraph("Teacher", headerFont));
                teacherHeader.setBackgroundColor(Color.GRAY);
                teacherHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(teacherHeader);
            }

            if (includeStudentMail) {
                PdfPCell studentMailHeader = new PdfPCell(new Paragraph("Student Mail", headerFont));
                studentMailHeader.setBackgroundColor(Color.GRAY);
                studentMailHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(studentMailHeader);
            }

            if (includeGrade) {
                PdfPCell gradeHeader = new PdfPCell(new Paragraph("Grade", headerFont));
                gradeHeader.setBackgroundColor(Color.GRAY);
                gradeHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(gradeHeader);
            }

            if (includeCategory) {
                PdfPCell categoryHeader = new PdfPCell(new Paragraph("Category", headerFont));
                categoryHeader.setBackgroundColor(Color.GRAY);
                categoryHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(categoryHeader);
            }

            // Add rows to the table
            for (Enrollment enrollment : enrollments) {
                if (includeEnrollmentId) {
                    PdfPCell enrollmentIdCell = new PdfPCell(new Paragraph(enrollment.getEnrollmentId().toString(), cellFont));
                    enrollmentIdCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(enrollmentIdCell);
                }

                if (includeStudentId) {
                    PdfPCell studentIdCell = new PdfPCell(new Paragraph(enrollment.getStudent().getId().toString(), cellFont));
                    studentIdCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(studentIdCell);
                }

                if (includeCourseId) {
                    PdfPCell courseIdCell = new PdfPCell(new Paragraph(enrollment.getCourse().getCourseId().toString(), cellFont));
                    courseIdCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(courseIdCell);
                }

                if (includeYear) {
                    PdfPCell yearCell = new PdfPCell(new Paragraph(String.valueOf(enrollment.getStudent().getYear()), cellFont));
                    yearCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(yearCell);
                }

                if (includeSection) {
                    PdfPCell sectionCell = new PdfPCell(new Paragraph(enrollment.getStudent().getFacultySection().toString(), cellFont));
                    sectionCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(sectionCell);
                }

                if (includeCourseName) {
                    PdfPCell courseNameCell = new PdfPCell(new Paragraph(enrollment.getCourse().getCourseName(), cellFont));
                    courseNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(courseNameCell);
                }

                if (includeStudentName) {
                    PdfPCell studentNameCell = new PdfPCell(new Paragraph(enrollment.getStudent().getName(), cellFont));
                    studentNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(studentNameCell);
                }

                if (includeTeacher) {
                    PdfPCell teacherCell = new PdfPCell(new Paragraph(enrollment.getCourse().getTeacherName(), cellFont));
                    teacherCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(teacherCell);
                }

                if (includeStudentMail) {
                    PdfPCell studentMailCell = new PdfPCell(new Paragraph(enrollment.getStudent().getEmail(), cellFont));
                    studentMailCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(studentMailCell);
                }

                if (includeGrade) {
                    PdfPCell gradeCell = new PdfPCell(new Paragraph(enrollment.getStudent().getGrade().toString(), cellFont));
                    gradeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(gradeCell);
                }

                if (includeCategory) {
                    PdfPCell categoryCell = new PdfPCell(new Paragraph(enrollment.getCourse().getCategory(), cellFont));
                    categoryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(categoryCell);
                }
            }

            // Add table to document
            document.add(table);

        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }
}
