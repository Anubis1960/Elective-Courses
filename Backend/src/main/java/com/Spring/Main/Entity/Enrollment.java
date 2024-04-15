package com.Spring.Main.Entity;

import com.Spring.Main.Audit.Auditable;
import com.Spring.Main.Enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@Table(name = "Enrollments")
public class Enrollment extends Auditable<String> {
    @Id
    @Column(name = "enrollment_id", nullable = false)
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private Long enrollmentId;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "student_id", nullable = false)
    private Student studentId;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "course_id", nullable = false)
    private Course courseId;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    public Enrollment() {

    }

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
    }

    public Course getCourseId() {
        return courseId;
    }

    public void setCourseId(Course courseId) {
        this.courseId = courseId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Enrollment(Student studentId, Course courseId, Integer priority, Status status) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.priority = priority;
        this.status = status;
    }

    public Enrollment(Long enrollmentId, Student studentId, Course courseId, Integer priority, Status status) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.priority = priority;
        this.status = status;
    }
}
