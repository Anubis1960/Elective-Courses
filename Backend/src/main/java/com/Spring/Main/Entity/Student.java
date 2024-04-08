package com.Spring.Main.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Entity
@Table(name = "Student")
public class Student{
    @Id
    @Column(name = "student_id")
    private Integer studentId;

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "grade")
    private Float grade;

    @Column(name = "faculty_section")
    private String facultySection;

    @Column(name = "year")
    private int year;

    @OneToOne
    @JoinColumn(name = "student_id")
    @MapsId
    private Users user;

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Student(Integer studentId, String studentName, Float grade, String facultySection, int year, Users user) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.grade = grade;
        this.facultySection = facultySection;
        this.year = year;
        this.user = user;
    }

    public Student() {
    }
}
