package com.Spring.application.entity;

import com.Spring.application.audit.Auditable;
import com.Spring.application.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;

import java.util.List;

@Entity
@Table(name = "Course")
public class Course extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id", nullable = false)
    @JsonView(Views.Public.class)
    private Long courseId;

    @Column(name = "course_name", nullable = false)
    @JsonView(Views.Public.class)
    private String courseName;

    @Column(name = "category", nullable = false)
    @JsonView(Views.Public.class)
    private String category;

    @Column(name = "description")
    @JsonView(Views.Public.class)
    private String description;

    @Column(name = "year_of_study", nullable = false)
    @JsonView(Views.Public.class)
    private Integer year;

    @Column(name = "maximum_students_allowed", nullable = false)
    @JsonView(Views.Public.class)
    private Integer maximumStudentsAllowed;

    @Column(name = "faculty_section", nullable = false)
    @JsonView(Views.Public.class)
    private String facultySection;

    @Column(name = "teacher_name")
    @JsonView(Views.Public.class)
    private String teacherName;

    public Course() {

    }

    public Course(String courseName, String category, String description, Integer year, Integer maximumStudentsAllowed, String facultySection, String teacherName) {
        this.courseName = courseName;
        this.category = category;
        this.description = description;
        this.year = year;
        this.maximumStudentsAllowed = maximumStudentsAllowed;
        this.facultySection = facultySection;
        this.teacherName = teacherName;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMaximumStudentsAllowed() {
        return maximumStudentsAllowed;
    }

    public void setMaximumStudentsAllowed(Integer maximumStudentsAllowed) {
        this.maximumStudentsAllowed = maximumStudentsAllowed;
    }

    public String getFacultySection() {
        return facultySection;
    }

    public void setFacultySection(String facultySection) {
        this.facultySection = facultySection;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", year=" + year +
                ", maximumStudentsAllowed=" + maximumStudentsAllowed +
                ", facultySection='" + facultySection + '\'' +
                ", teacherName='" + teacherName + '\'' +
                '}';
    }
}
