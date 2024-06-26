package com.Spring.application.entity;

import com.Spring.application.audit.Auditable;
import com.Spring.application.enums.FacultySection;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Course")
public class Course extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "description")
    private String description;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "maximum_students_allowed", nullable = false)
    private Integer maximumStudentsAllowed;

    @Enumerated(EnumType.STRING)
    @Column(name = "faculty_section", nullable = false)
    private FacultySection facultySection;

    @Column(name = "year_of_study", nullable = false)
    private Integer year;

    @Column(name = "teacher_name")
    private String teacherName;

    public Course() {

    }

    public Course(String courseName, String category, String description, Integer year, Integer maximumStudentsAllowed, FacultySection facultySection, String teacherName) {
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

    public FacultySection getFacultySection() {
        return facultySection;
    }

    public void setFacultySection(FacultySection facultySection) {
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
