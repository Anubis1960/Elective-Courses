package com.Spring.application.service.impl;

import com.Spring.application.dto.EnrollmentExporter;
import com.Spring.application.dto.StudentDTO;
import com.Spring.application.dto.StudentExporter;
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
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
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

//    public void exportStudentsCopy(OutputStream out, Optional<String> facultySection, Optional<Integer> year,boolean includeId, boolean includeName, boolean includeEmail, boolean includeGrade, boolean includeSection, boolean includeYear) throws IOException, IllegalAccessException {
//        List<StudentExporter> students;
//
//        StringBuilder query = new StringBuilder();
//
//        query.append("SELECT");
//
//        if (includeName) {
//            query.append(" s.name,");
//        }
//
//        if (includeEmail) {
//            query.append(" s.email,");
//        }
//
//        if (includeGrade) {
//            query.append(" s.grade,");
//        }
//
//        if (includeSection) {
//            query.append(" s.facultySection,");
//        }
//
//        if (includeYear) {
//            query.append(" s.year,");
//        }
//
//        query.deleteCharAt(query.length() - 1);
//
//        query.append(" FROM Student s");
//
//        if (facultySection.isPresent() && year.isPresent()) {
//            query.append(" WHERE s.facultySection = :facultySection AND s.year = :year");
//        } else if (facultySection.isPresent()) {
//            query.append(" WHERE s.facultySection = :facultySection");
//        } else if (year.isPresent()) {
//            query.append(" WHERE s.year = :year");
//        }
//
//        List<Object> result = entityManager.createQuery(query.toString()).getResultList();
//
//        students = new ArrayList<>();
//
//        for (Object obj : result) {
//            Object[] arr = (Object[]) obj;
//            StudentExporter.Builder builder = new StudentExporter.Builder();
//            int index = 0;
//
//            if (includeName) builder.name((String) arr[index++]);
//            if (includeEmail) builder.email((String) arr[index++]);
//            if (includeGrade) builder.grade((Float) arr[index++]);
//            if (includeSection) builder.section(arr[index++].toString());
//            if (includeYear) builder.year((Integer) arr[index]);
//
//            students.add(builder.build());
//        }
//
//        GeneratorMethods.writePDF(students, out);
//
//    }
//
//    @Override
//    public void exportStudents(OutputStream out, Optional<String> facultySection, Optional<Integer> year,boolean includeId, boolean includeName, boolean includeEmail, boolean includeGrade, boolean includeSection, boolean includeYear) throws IOException {
//        List<Student> students;
//        if (facultySection.isPresent() && year.isPresent()) {
//            students = entityManager.createQuery("SELECT s FROM Student s WHERE s.facultySection = :facultySection AND s.year = :year", Student.class)
//                    .setParameter("facultySection", FacultySection.valueOf(facultySection.get()))
//                    .setParameter("year", year.get())
//                    .getResultList();
//        } else if (facultySection.isPresent()) {
//            students = entityManager.createQuery("SELECT s FROM Student s WHERE s.facultySection = :facultySection", Student.class)
//                    .setParameter("facultySection", FacultySection.valueOf(facultySection.get()))
//                    .getResultList();
//        } else if (year.isPresent()) {
//            students = entityManager.createQuery("SELECT s FROM Student s WHERE s.year = :year", Student.class)
//                    .setParameter("year", year.get())
//                    .getResultList();
//        } else {
//            students = studentRepository.findAll();
//        }
//
////        writeStudentsToPDF(out, students, includeId, includeName, includeEmail, includeGrade, includeSection, includeYear);
//    }


//    public void writeStudentsToPDF(OutputStream out, List<Student> students,
//                                   boolean includeId, boolean includeName, boolean includeEmail,
//                                   boolean includeGrade, boolean includeSection, boolean includeYear) {
//
//        // Set up document
//        Document document = GeneratorMethods.setUpDocument(out);
//
//        // Add title
//        GeneratorMethods.addTitleToPDF("Students", document);
//
//        // Calculate number of columns based on include flags
//        int numColumns = 0;
//        if (includeId) numColumns++;
//        if (includeName) numColumns++;
//        if (includeEmail) numColumns++;
//        if (includeGrade) numColumns++;
//        if (includeSection) numColumns++;
//        if (includeYear) numColumns++;
//
//        // Create table with calculated number of columns
//        PdfPTable table = new PdfPTable(numColumns);
//        table.setWidthPercentage(100);
//        table.setSpacingBefore(10f);
//        table.setSpacingAfter(10f);
//
//        // Set column widths evenly
//        float[] columnWidths = new float[numColumns];
//        for (int i = 0; i < numColumns; i++) {
//            columnWidths[i] = 1f;
//        }
//        table.setWidths(columnWidths);
//
//        // Add table headers
//        if (includeId) {
//            GeneratorMethods.addHeadersToPDF("ID", table);
//        }
//
//        if (includeName) {
//            GeneratorMethods.addHeadersToPDF("Name", table);
//        }
//
//        if (includeEmail) {
//            GeneratorMethods.addHeadersToPDF("Email", table);
//        }
//
//        if (includeGrade) {
//            GeneratorMethods.addHeadersToPDF("Grade", table);
//        }
//
//        if (includeSection) {
//            GeneratorMethods.addHeadersToPDF("Faculty Section", table);
//        }
//
//        if (includeYear) {
//            GeneratorMethods.addHeadersToPDF("Year", table);
//        }
//
//        // Add rows to the table
//        for (Student student : students) {
//            if (includeId) {
//                GeneratorMethods.addDataForPDF(student.getId().toString(), table);
//            }
//
//            if (includeName) {
//                GeneratorMethods.addDataForPDF(student.getName(), table);
//            }
//
//            if (includeEmail) {
//                GeneratorMethods.addDataForPDF(student.getEmail(), table);
//            }
//
//            if (includeGrade) {
//                GeneratorMethods.addDataForPDF(student.getGrade().toString(), table);
//            }
//
//            if (includeSection) {
//                GeneratorMethods.addDataForPDF(student.getFacultySection().toString(), table);
//            }
//
//            if (includeYear) {
//                GeneratorMethods.addDataForPDF(student.getYear().toString(), table);
//            }
//        }
//
//        // Add table to document
//        document.add(table);
//        // Close document
//        document.close();
//    }


//    @Override
//    public void exportScheduleToPDF(OutputStream out, Long id) throws IOException {
//        List<CourseSchedule> courseSchedules = courseScheduleRepository.findCourseScheduleOfStudent(id);
//
//        // Set up document
//        Document document = GeneratorMethods.setUpDocument(out);
//
//        // Define fonts
//        Font font = FontFactory.getFont(FontFactory.COURIER, 12, Color.BLACK);
//
//        // Create a table with 6 columns (Time slots + 5 days of the week)
//        PdfPTable table = new PdfPTable(6);
//        table.setWidthPercentage(100);
//        table.setSpacingBefore(10f);
//        table.setSpacingAfter(10f);
//
//        // Set column widths
//        float[] columnWidths = {2f, 2f, 2f, 2f, 2f, 2f};
//        table.setWidths(columnWidths);
//
//        // Create table header
//        String[] headers = {"Time Slot", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
//        for (String header : headers) {
//            GeneratorMethods.writeHeadersPDF(header, table);
//        }
//
//        // Define time slots
//        String[] timeSlots = {"08:00-09:30", "09:40-11:10", "11:20-12:50", "13:00-14:30", "14:40-16:10", "16:20-17:50", "18:00-19:30", "19:40-21:10"};
//
//        // Map to store the courses based on the day and time slot
//        Map<String, Map<String, List<CourseSchedule>>> scheduleMap = new HashMap<>();
//        for (String day : new String[]{"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"}) {
//            scheduleMap.put(day, new HashMap<>());
//            for (String timeSlot : timeSlots) {
//                scheduleMap.get(day).put(timeSlot, new ArrayList<>());
//            }
//        }
//
//        // Populate the schedule map
//        for (CourseSchedule courseSchedule : courseSchedules) {
//            String day = courseSchedule.getDay().toString().toUpperCase();
//            String startTime = courseSchedule.getStartTime().toString();
//            String endTime = courseSchedule.getEndTime().toString();
//            String timeSlot = startTime + "-" + endTime;
//
//            if (scheduleMap.containsKey(day)) {
//                scheduleMap.get(day).get(timeSlot).add(courseSchedule);
//            }
//        }
//
//        // Add rows to the table
//        for (String timeSlot : timeSlots) {
//            // Add time slot cell
//            table.addCell(new PdfPCell(new Phrase(timeSlot, font)));
//
//            // Add course cells for each day
//            for (String day : new String[]{"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"}) {
//                List<CourseSchedule> courses = scheduleMap.get(day).get(timeSlot);
//                PdfPCell cell;
//                if (!courses.isEmpty()) {
//                    StringBuilder coursesText = new StringBuilder();
//                    for (CourseSchedule course : courses) {
//                        coursesText.append(course.getCourse().getCourseName()).append("\n");
//                    }
//                    cell = new PdfPCell(new Phrase(coursesText.toString(), font));
//                } else {
//                    cell = new PdfPCell(new Phrase("", font));
//                }
//                table.addCell(cell);
//            }
//        }
//
//        // Add table to document
//        document.add(table);
//        // Close document
//        document.close();
//    }


//    @Override
//    public void exportStudentsOfCourseToPDF(OutputStream out, Long id) throws IOException, IllegalAccessException {
//        List<Student> students = studentRepository.findAcceptedStudentsByCourseId(id);
//        List<StudentDTO> studentDTOs = StudentDTO.convertToDTO(students);
//        Course course = courseRepository.findById(id).orElse(null);
//
//        if (course == null) {
//            throw new IllegalArgumentException("Course with ID " + id + " not found.");
//        }
//        GeneratorMethods.writePDF(studentDTOs, out);
//    }

//    @Override
//    public void exportEnrollments(OutputStream outputStream, Optional<String> facultySection, Optional<Integer> year, boolean includeEnrollmentId, boolean includeStudentId, boolean includeCourseId, boolean includeYear, boolean IncludeSection, boolean includeCourseName, boolean includeStudentName, boolean includeTeacher, boolean includeStudentMail, boolean includeGrade, boolean includeCategory) throws IOException {
//        List<Enrollment> enrollments;
//        if (facultySection.isPresent() && year.isPresent()) {
//            System.out.println();
//            enrollments = enrollmentRepository.findEnrollmentByFacultySectionAndYearAndStatusIsAccepted(FacultySection.valueOf(facultySection.get()), year.get());
//        } else if (facultySection.isPresent()) {
//            enrollments = enrollmentRepository.findEnrollmentByFacultySectionAndStatusIsAccepted(FacultySection.valueOf(facultySection.get()));
//        } else if (year.isPresent()) {
//            enrollments = enrollmentRepository.findEnrollmentByYearAndStatusIsAccepted(year.get());
//        } else {
//            enrollments = enrollmentRepository.findAllWhereStatusIsAccepted();
//        }
//
////        writeEnrollmentsToPDF(outputStream, enrollments, includeEnrollmentId, includeStudentId, includeCourseId, includeYear, IncludeSection, includeCourseName, includeStudentName, includeTeacher, includeStudentMail, includeGrade, includeCategory);
//    }

//    @Override
//    public void exportEnrollmentsCopy(OutputStream out, Optional<String> facultySection, Optional<Integer> year, boolean includeYear, boolean IncludeSection, boolean includeCourseName, boolean includeStudentName, boolean includeTeacher, boolean includeStudentMail, boolean includeGrade, boolean includeCategory, boolean includeNumOfStudents, boolean includeAVGgrade, String extension) throws IOException, IllegalAccessException {
//        List<EnrollmentExporter> enrollments;
//
//        StringBuilder query = new StringBuilder();
//
//        query.append("SELECT");
//
//        if (includeCourseName) {
//            query.append(" e.course.courseName,");
//        }
//
//        if (includeCategory) {
//            query.append(" e.course.category,");
//        }
//
//        if (includeYear) {
//            query.append(" e.student.year,");
//        }
//
//        if (IncludeSection) {
//            query.append(" e.student.facultySection,");
//        }
//
//        if (includeTeacher) {
//            query.append(" e.course.teacherName,");
//        }
//
//        if (includeStudentName) {
//            query.append(" e.student.name,");
//        }
//
//        if (includeStudentMail) {
//            query.append(" e.student.email,");
//        }
//
//        if (includeGrade) {
//            query.append(" e.student.grade,");
//        }
//
//        if (includeNumOfStudents) {
//            query.append(" n.numOfStudents,");
//        }
//
//        if (includeAVGgrade) {
//            query.append(" n.avgGrade,");
//        }
//
//        query.deleteCharAt(query.length() - 1); // Remove the trailing comma
//
//        query.append(" FROM Enrollment e");
//        query.append(", (SELECT e.course.courseId AS courseId, COUNT(e.student.id) AS numOfStudents, AVG(e.student.grade) AS avgGrade FROM Enrollment e GROUP BY e.course.courseId) n");
//
//        query.append(" WHERE e.status = 'ACCEPTED' AND e.course.courseId = n.courseId");
//
//        if (facultySection.isPresent() && year.isPresent()) {
//            query.append(" AND e.student.facultySection = :facultySection AND e.student.year = :year");
//        } else if (facultySection.isPresent()) {
//            query.append(" AND e.student.facultySection = :facultySection");
//        } else if (year.isPresent()) {
//            query.append(" AND e.student.year = :year");
//        }
//
//        List<Object> result = entityManager.createQuery(query.toString()).getResultList();
//
//        enrollments = new ArrayList<>();
//
//        for (Object obj : result) {
//            Object[] arr = (Object[]) obj;
//            EnrollmentExporter.Builder builder = new EnrollmentExporter.Builder();
//            int index = 0;
//
//            if (includeCourseName) builder.courseName((String) arr[index++]);
//            if (includeCategory) builder.category((String) arr[index++]);
//            if (includeYear) builder.year((Integer) arr[index++]);
//            if (IncludeSection) builder.section(arr[index++].toString());
//            if (includeTeacher) builder.teacher((String) arr[index++]);
//            if (includeStudentName) builder.name((String) arr[index++]);
//            if (includeStudentMail) builder.email((String) arr[index++]);
//            if (includeGrade) builder.grade((Float) arr[index++]);
//            if (includeNumOfStudents) builder.numberOfStudents((Long) arr[index++]);
//            if (includeAVGgrade) builder.avgGrade((Double) arr[index]);
//
//            enrollments.add(builder.build());
//        }
//
//        if (extension.equals("pdf")) {
//            GeneratorMethods.writePDF(enrollments, out);
//        } else if (extension.equals("csv")) {
//            GeneratorMethods.writeCSV(enrollments, out);
//        } else if (extension.equals("excel")) {
//            GeneratorMethods.writeXLSX(enrollments, out);
//        }
//    }

//    public void writeEnrollmentsToPDF(OutputStream outputStream, List<Enrollment> enrollments,
//                                      boolean includeEnrollmentId, boolean includeStudentId, boolean includeCourseId, boolean includeYear,
//                                      boolean includeSection, boolean includeCourseName, boolean includeStudentName,
//                                      boolean includeTeacher, boolean includeStudentMail, boolean includeGrade,
//                                      boolean includeCategory) {
//
//        // Set up document
//        Document document = GeneratorMethods.setUpDocument(outputStream);
//
//        // Add title
//        GeneratorMethods.addTitleToPDF("Enrollments", document);
//
//        // Calculate number of columns based on include flags
//        int numColumns = 0;
//        if (includeEnrollmentId) numColumns++;
//        if (includeStudentId) numColumns++;
//        if (includeCourseId) numColumns++;
//        if (includeYear) numColumns++;
//        if (includeSection) numColumns++;
//        if (includeCourseName) numColumns++;
//        if (includeStudentName) numColumns++;
//        if (includeTeacher) numColumns++;
//        if (includeStudentMail) numColumns++;
//        if (includeGrade) numColumns++;
//        if (includeCategory) numColumns++;
//
//        // Create table with calculated number of columns
//        PdfPTable table = new PdfPTable(numColumns);
//        table.setWidthPercentage(100);
//        table.setSpacingBefore(10f);
//        table.setSpacingAfter(10f);
//
//        // Set column widths evenly
//        float[] columnWidths = new float[numColumns];
//        for (int i = 0; i < numColumns; i++) {
//            columnWidths[i] = 1f;
//        }
//        table.setWidths(columnWidths);
//
//        // Add table headers
//        if (includeEnrollmentId) {
//            GeneratorMethods.addHeadersToPDF("ID", table);
//        }
//
//        if (includeStudentId) {
//            GeneratorMethods.addHeadersToPDF("StudentID", table);
//        }
//
//        if (includeCourseId) {
//            GeneratorMethods.addHeadersToPDF("CourseID", table);
//        }
//
//        if (includeYear) {
//            GeneratorMethods.addHeadersToPDF("Year", table);
//        }
//
//        if (includeSection) {
//            GeneratorMethods.addHeadersToPDF("Faculty Section", table);
//        }
//
//        if (includeCourseName) {
//            GeneratorMethods.addHeadersToPDF("Course Name", table);
//        }
//
//        if (includeStudentName) {
//            GeneratorMethods.addHeadersToPDF("Student Name", table);
//        }
//
//        if (includeTeacher) {
//            GeneratorMethods.addHeadersToPDF("Teacher", table);
//        }
//
//        if (includeStudentMail) {
//            GeneratorMethods.addHeadersToPDF("Student Mail", table);
//        }
//
//        if (includeGrade) {
//            GeneratorMethods.addHeadersToPDF("Grade", table);
//        }
//
//        if (includeCategory) {
//            GeneratorMethods.addHeadersToPDF("Category", table);
//        }
//
//        // Add rows to the table
//        for (Enrollment enrollment : enrollments) {
//            if (includeEnrollmentId) {
//                GeneratorMethods.addDataForPDF(enrollment.getEnrollmentId().toString(), table);
//            }
//
//            if (includeStudentId) {
//                GeneratorMethods.addDataForPDF(enrollment.getStudent().getId().toString(), table);
//            }
//
//            if (includeCourseId) {
//                GeneratorMethods.addDataForPDF(enrollment.getCourse().getCourseId().toString(), table);
//            }
//
//            if (includeYear) {
//                GeneratorMethods.addDataForPDF(enrollment.getStudent().getYear().toString(), table);
//            }
//
//            if (includeSection) {
//                GeneratorMethods.addDataForPDF(enrollment.getStudent().getFacultySection().toString(), table);
//            }
//
//            if (includeCourseName) {
//                GeneratorMethods.addDataForPDF(enrollment.getCourse().getCourseName(), table);
//            }
//
//            if (includeStudentName) {
//                GeneratorMethods.addDataForPDF(enrollment.getStudent().getName(), table);
//            }
//
//            if (includeTeacher) {
//                GeneratorMethods.addDataForPDF(enrollment.getCourse().getTeacherName(), table);
//            }
//
//            if (includeStudentMail) {
//                GeneratorMethods.addDataForPDF(enrollment.getStudent().getEmail(), table);
//            }
//
//            if (includeGrade) {
//                GeneratorMethods.addDataForPDF(enrollment.getStudent().getGrade().toString(), table);
//            }
//
//            if (includeCategory) {
//                GeneratorMethods.addDataForPDF(enrollment.getCourse().getCategory(), table);
//            }
//        }
//
//        // Add table to document
//        document.add(table);
//        // Close document
//        document.close();
//    }
}
