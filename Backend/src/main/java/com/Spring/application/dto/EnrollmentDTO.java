package com.Spring.application.dto;

import com.Spring.application.entity.Enrollment;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.ArrayList;
import java.util.List;

public class EnrollmentDTO {
    private Long id;
    private Long studentId;
    private Long courseId;
    private Integer priority;
    private String status;

    public EnrollmentDTO(Long id, Long studentId, Long courseId, Integer priority, String status) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.priority = priority;
        this.status = status;
    }

    public EnrollmentDTO() {
    }

    public static List<EnrollmentDTO> convertToDTO(List<Enrollment> enrollments) {
        List<EnrollmentDTO> enrollmentDTOS = new ArrayList<>();
        enrollments.forEach(enrollment -> enrollmentDTOS.add(new EnrollmentDTO(enrollment.getEnrollmentId(), enrollment.getStudent().getId(), enrollment.getCourse().getCourseId(), enrollment.getPriority(), enrollment.getStatus().toString())));
        return enrollmentDTOS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
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
