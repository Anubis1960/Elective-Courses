package com.Spring.Main.entity;

import com.Spring.Main.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
@Table(name = "Student")
public class Student extends User {

    @Column(name = "grade", nullable = false)
    private Float grade;

    @Column(name = "faculty_section", nullable = false)
    private String facultySection;

    @Column(name = "year_of_study", nullable = false)
    private Integer year;

    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Enrollment> enrollments;

    public Student(Long userId, String name, Role role, Float grade, String facultySection, Integer year) {
        this.grade = grade;
        this.facultySection = facultySection;
        this.year = year;
        this.id = userId;
        this.name = name;
        this.role = role;
    }

    public Student(String name, Role role, Float grade, String facultySection, Integer year) {
        this.grade = grade;
        this.facultySection = facultySection;
        this.year = year;
        this.name = name;
        this.role = role;
    }

    public Student() {

    }

    public Float getGrade() {
        return grade;
    }

    public void setGrade(Float grade) {
        this.grade = grade;
    }

    public String getFacultySection() {
        return facultySection;
    }

    public void setFacultySection(String facultySection) {
        this.facultySection = facultySection;
    }


    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
