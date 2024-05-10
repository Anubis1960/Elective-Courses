package com.Spring.application.dto;

import com.Spring.application.entity.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String facultySection;
    private Integer year;
    private Float grade;

    public StudentDTO(Long id, String name, String email, String role, String facultySection, Integer year, Float grade) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.facultySection = facultySection;
        this.year = year;
        this.grade = grade;
    }

    public StudentDTO() {
    }

    public static List<StudentDTO> convertToDTO(List<Student> students) {
        List<StudentDTO> studentDTOS = new ArrayList<>();
        students.forEach(student -> studentDTOS.add(new StudentDTO(student.getId(), student.getName(), student.getEmail(), student.getRole().toString(), student.getFacultySection().toString(), student.getYear(), student.getGrade())));
        return studentDTOS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public Float getGrade() {
        return grade;
    }

    public void setGrade(Float grade) {
        this.grade = grade;
    }
}
