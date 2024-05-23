package com.Spring.application.dto;

public class EnrollmentRequest {
    private Long studentId;
    private Long courseId;
    private Integer priority;

    public EnrollmentRequest(Long studentId, Long courseId, Integer priority) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.priority = priority;
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
}
