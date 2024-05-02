package com.Spring.application.entity;

import com.Spring.application.enums.Role;
import com.Spring.application.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import org.hibernate.annotations.OnDelete;

import java.util.List;

@Entity
@Table(name = "Student")
@OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
public class Student extends User {

    @Column(name = "grade", nullable = false)
    @JsonView(Views.Public.class)
    private Float grade;

    @Column(name = "faculty_section", nullable = false)
    @JsonView(Views.Public.class)
    private String facultySection;

    @Column(name = "year_of_study", nullable = false)
    @JsonView(Views.Public.class)
    private Integer year;

    public Student(String name, Float grade, String facultySection, Integer year) {
        this.grade = grade;
        this.facultySection = facultySection;
        this.year = year;
        this.name = name;
        this.role = Role.STUDENT;
    }


    public Student(String name, Role role, Float grade, String facultySection, Integer year, String email, String password) {
        this.grade = grade;
        this.facultySection = facultySection;
        this.year = year;
        this.name = name;
        this.role = role;
        this.email = email;
        this.password = password;
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
