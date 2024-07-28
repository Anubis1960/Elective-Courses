package com.Spring.application.entity;

import com.Spring.application.audit.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "template")
public class Template extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "year_of_study", nullable = true)
    private Integer year;

    @Column(name = "faculty_section", nullable = true)
    private String facultySection;

    @Column(name = "class_flag", nullable = false)
    private String classFlag;

    @Column(name = "options", nullable = false)
    private int options;

    public Template() {

    }

    public Template(String name, Integer year, String facultySection, String classFlag, int options) {
        this.name = name;
        this.year = year;
        this.facultySection = facultySection;
        this.classFlag = classFlag;
        this.options = options;
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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getFacultySection() {
        return facultySection;
    }

    public void setFacultySection(String facultySection) {
        this.facultySection = facultySection;
    }

    public int getOptions() {
        return options;
    }

    public void setOptions(int options) {
        this.options = options;
    }

    public String getClassFlag() {
        return classFlag;
    }

    public void setClassFlag(String classFlag) {
        this.classFlag = classFlag;
    }
}
