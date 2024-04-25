package com.Spring.application.entity;

import com.Spring.application.audit.Auditable;
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
    private Long courseId;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "description")
    private String description;

    @Column(name = "year_of_study", nullable = false)
    private Integer year;

    @Column(name = "maximum_students_allowed", nullable = false)
    private Integer maximumStudentsAllowed;

    @Column(name = "faculty_section", nullable = false)
    private String facultySection;

    @Column(name = "teacher_name")
    private String teacherName;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Enrollment> enrollments;

    @OneToOne(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private CourseSchedule courseSchedule;

    public Course() {

    }

    public Course(Long courseId, String courseName, String category, String description, Integer year, Integer maximumStudentsAllowed, String facultySection, String teacherName) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.category = category;
        this.description = description;
        this.year = year;
        this.maximumStudentsAllowed = maximumStudentsAllowed;
        this.facultySection = facultySection;
        this.teacherName = teacherName;
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
}
