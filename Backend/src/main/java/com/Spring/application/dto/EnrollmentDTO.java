package com.Spring.application.dto;

import com.Spring.application.entity.Enrollment;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.ArrayList;
import java.util.List;

public class EnrollmentDTO {
    private Long id;
    private String studentName;
    private String courseName;
    private Integer priority;
    private String status;

    public EnrollmentDTO() {
    }

    public EnrollmentDTO(Long id, String studentName, String courseName, Integer priority, String status) {
        this.id = id;
        this.studentName = studentName;
        this.courseName = courseName;
        this.priority = priority;
        this.status = status;
    }

    public static List<EnrollmentDTO> convertToDTO(List<Enrollment> enrollments) {
        List<EnrollmentDTO> enrollmentDTOS = new ArrayList<>();
        enrollments.forEach(enrollment -> enrollmentDTOS.add(new EnrollmentDTO(enrollment.getEnrollmentId(), enrollment.getStudent().getName(), enrollment.getCourse().getCourseName(), enrollment.getPriority(), enrollment.getStatus().toString())));
        return enrollmentDTOS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
