package com.Spring.application.entity;

import com.Spring.application.enums.FacultySection;
import com.Spring.application.enums.Role;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;

@Entity
@Table(name = "Student")
@OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
public class Student extends User {

    @Enumerated(EnumType.STRING)
    @Column(name = "faculty_section", nullable = false)
    private FacultySection facultySection;

    @Column(name = "year_of_study", nullable = false)
    private Integer year;

    @Column(name = "grade", nullable = false)
    private Float grade;

    public Student(String name, Role role, Float grade, FacultySection facultySection, Integer year, String email, String password) {
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

    public FacultySection getFacultySection() {
        return facultySection;
    }

    public void setFacultySection(FacultySection facultySection) {
        this.facultySection = facultySection;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Student{" +
                "grade=" + grade +
                ", facultySection='" + facultySection + '\'' +
                ", year=" + year +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", role=" + role +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
