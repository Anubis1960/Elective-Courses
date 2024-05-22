package com.Spring.application.entity;

import com.Spring.application.audit.Auditable;
import com.Spring.application.enums.Day;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalTime;

@Entity
@Table(name = "CourseSchedule")
public class CourseSchedule extends Auditable<String> {

    @Id
    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "day", nullable = false)
    private Day day;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "course_id")
    @MapsId
    private Course course;

    public CourseSchedule() {
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "CourseSchedule{" +
                "courseId=" + courseId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", day=" + day +
                ", course=" + course +
                '}';
    }

    public CourseSchedule(Course course, Day day, LocalTime startTime, LocalTime endTime) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.course = course;
    }
}
