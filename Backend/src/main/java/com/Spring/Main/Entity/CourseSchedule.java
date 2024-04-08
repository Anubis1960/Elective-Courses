package com.Spring.Main.Entity;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "CourseSchedule")
public class CourseSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "start_time")
    private java.sql.Date startTime;

    @Column(name = "end_time")
    private java.sql.Date endTime;

    @OneToOne
    @JoinColumn(name = "course_id")
    @MapsId
    private Course course;

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public CourseSchedule() {

    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public CourseSchedule(Integer courseId, Date startTime, Date endTime, Course course) {
        this.courseId = courseId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.course = course;
    }
}
