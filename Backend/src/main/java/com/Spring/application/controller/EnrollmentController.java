package com.Spring.application.controller;

import com.Spring.application.dto.EnrollmentDTO;
import com.Spring.application.entity.Enrollment;
import com.Spring.application.exceptions.ObjectNotFound;
import com.Spring.application.repository.EnrollmentRepository;
import com.Spring.application.service.impl.EnrollmentServiceImpl;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.Spring.application.service.PDFGeneratorService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/enrollment")
public class EnrollmentController {
    @Autowired
    private EnrollmentServiceImpl enrollmentService;
    @Autowired
    private PDFGeneratorService pdfGeneratorService;

    @PostMapping("/")
    public ResponseEntity<EnrollmentDTO> enroll(Long studentId, Long courseId, Integer priority) throws ObjectNotFound {
        Enrollment enrollment = enrollmentService.addEnrollment(studentId, courseId, priority);
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO(enrollment.getEnrollmentId(), enrollment.getStudent().getId(), enrollment.getCourse().getCourseId(), enrollment.getPriority(), enrollment.getStatus().toString());
        return new ResponseEntity<>(enrollmentDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> updateEnrollment(@PathVariable("id") Long enrollmentId, Long studentId, Long courseId, Integer priority, String status) throws ObjectNotFound {
        Enrollment enrollment = enrollmentService.updateEnrollment(enrollmentId, studentId, courseId, priority, status);
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO(enrollment.getEnrollmentId(), enrollment.getStudent().getId(), enrollment.getCourse().getCourseId(), enrollment.getPriority(), enrollment.getStatus().toString());
        return new ResponseEntity<>(enrollmentDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> deleteEnrollment(@PathVariable("id") Long enrollmentId) throws ObjectNotFound {
        Enrollment enrollment = enrollmentService.deleteEnrollment(enrollmentId);
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO(enrollment.getEnrollmentId(), enrollment.getStudent().getId(), enrollment.getCourse().getCourseId(), enrollment.getPriority(), enrollment.getStatus().toString());
        return new ResponseEntity<>(enrollmentDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> getEnrollmentById(@PathVariable("id") Long enrollmentId) throws ObjectNotFound {
        Enrollment enrollment = enrollmentService.getEnrollmentById(enrollmentId);
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO(enrollment.getEnrollmentId(), enrollment.getStudent().getId(), enrollment.getCourse().getCourseId(), enrollment.getPriority(), enrollment.getStatus().toString());
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
    public void exportEnrollmentsToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormater.format(System.currentTimeMillis());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=enrollments_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
        response.getOutputStream().write(pdfGeneratorService.exportEnrollmentsToPDF());
    }
}
