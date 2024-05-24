package com.Spring.application.controller;

import com.Spring.application.dto.EnrollmentDTO;
import com.Spring.application.entity.Course;
import com.Spring.application.entity.Enrollment;
import com.Spring.application.entity.Student;
import com.Spring.application.exceptions.ObjectNotFound;
import com.Spring.application.service.CourseService;
import com.Spring.application.service.impl.CourseServiceImpl;
import com.Spring.application.service.impl.EnrollmentServiceImpl;

import com.Spring.application.service.impl.StudentServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.Spring.application.service.PDFGeneratorService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/enrollment")
public class EnrollmentController {
    @Autowired
    private EnrollmentServiceImpl enrollmentService;
    @Autowired
    private PDFGeneratorService pdfGeneratorService;
    @Autowired
    private StudentServiceImpl studentService;
    @Autowired
    private CourseServiceImpl courseService;

    @PostMapping("/")
    public ResponseEntity<EnrollmentDTO> enroll(
            @RequestParam Long studentId,
            @RequestParam Long courseId) throws ObjectNotFound {
        //System.out.println("studentId: " + studentId + " courseId: " + courseId + " priority: " + priority);
        Integer priority = enrollmentService.countByStudentId(studentId) + 1;
        Enrollment enrollment = enrollmentService.addEnrollment(studentId, courseId, priority);
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO(enrollment.getEnrollmentId(), enrollment.getStudent().getName(), enrollment.getCourse().getCourseName(), enrollment.getPriority(), enrollment.getStatus().toString());
        return new ResponseEntity<>(enrollmentDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> updateEnrollment(
            @PathVariable("id") Long enrollmentId,
            @RequestBody EnrollmentDTO enrollmentDTO) throws ObjectNotFound {
        Student student = studentService.getStudentByName(enrollmentDTO.getStudentName());
        Course course = courseService.getCourseByName(enrollmentDTO.getCourseName());
        enrollmentService.updateEnrollment(enrollmentId, student.getUserId(), course.getCourseId(), enrollmentDTO.getPriority(), enrollmentDTO.getStatus());
        return new ResponseEntity<>(enrollmentDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> deleteEnrollment(@PathVariable("id") Long enrollmentId) throws ObjectNotFound {
        Enrollment enrollment = enrollmentService.deleteEnrollment(enrollmentId);
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO(enrollment.getEnrollmentId(), enrollment.getStudent().getName(), enrollment.getCourse().getCourseName(), enrollment.getPriority(), enrollment.getStatus().toString());
        return new ResponseEntity<>(enrollmentDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> getEnrollmentById(@PathVariable("id") Long enrollmentId) throws ObjectNotFound {
        Enrollment enrollment = enrollmentService.getEnrollmentById(enrollmentId);
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO(enrollment.getEnrollmentId(), enrollment.getStudent().getName(), enrollment.getCourse().getCourseName(), enrollment.getPriority(), enrollment.getStatus().toString());
        return new ResponseEntity<>(enrollmentDTO, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<EnrollmentDTO>> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        if (enrollments.isEmpty()) {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
        List<EnrollmentDTO> enrollmentDTOs = EnrollmentDTO.convertToDTO(enrollments);
        return new ResponseEntity<>(enrollmentDTOs, HttpStatus.OK);
    }


    @GetMapping("/export")
    public void exportEnrollmentsToPDF(HttpServletResponse response, @RequestParam String facultySection, @RequestParam Integer year) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormater.format(System.currentTimeMillis());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=enrollments_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        if (facultySection != null && year != null) {
            pdfGeneratorService.exportEnrollmentByFacultySectionAndYearToPDF(response.getOutputStream(), facultySection, year);
        } else if (facultySection != null) {
            pdfGeneratorService.exportEnrollmentByFacultySectionToPDF(response.getOutputStream(), facultySection);
        } else if (year != null) {
            pdfGeneratorService.exportEnrollmentByYearToPDF(response.getOutputStream(), year);
        } else {
            pdfGeneratorService.exportEnrollmentsToPDF(response.getOutputStream());
        }
    }

    @PutMapping("/")
    public ResponseEntity<List<EnrollmentDTO>> updateEnrollments(@RequestBody List<EnrollmentDTO> enrollmentDTOs) throws ObjectNotFound {
        for (EnrollmentDTO enrollmentDTO : enrollmentDTOs) {
            System.out.println(enrollmentDTO.toString());
            Student student = studentService.getStudentByName(enrollmentDTO.getStudentName());
            Course course = courseService.getCourseByName(enrollmentDTO.getCourseName());
            enrollmentService.updateEnrollment(enrollmentDTO.getId(), student.getUserId(), course.getCourseId(),enrollmentDTOs.indexOf(enrollmentDTO)+1, enrollmentDTO.getStatus());
        }
        return new ResponseEntity<>(enrollmentDTOs, HttpStatus.OK);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsOfStudent(@PathVariable("studentId") Long studentId) throws ObjectNotFound {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudentId(studentId);
        if (enrollments.isEmpty()) {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
        List<EnrollmentDTO> enrollmentDTOs = EnrollmentDTO.convertToDTO(enrollments);
        return new ResponseEntity<>(enrollmentDTOs, HttpStatus.OK);
    }

    @GetMapping("/assign")
    public ResponseEntity<List<EnrollmentDTO>> assignStudents() {
        List<Enrollment> enrollments = enrollmentService.assignStudents();
        if (enrollments.isEmpty()) {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
        List<EnrollmentDTO> enrollmentDTOs = EnrollmentDTO.convertToDTO(enrollments);
        return new ResponseEntity<>(enrollmentDTOs, HttpStatus.OK);
    }

}
