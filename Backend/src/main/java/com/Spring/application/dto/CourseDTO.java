package com.Spring.application.dto;

import com.Spring.application.entity.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseDTO {
    private Long id;
    private String name;
    private String description;
    private String category;
    private Integer maximumStudentsAllowed;
    private String facultySection;
    private Integer year;
    private String teacherName;

    public CourseDTO(Long id, String name, String description, String category, Integer maximumStudentsAllowed, String facultySection, Integer year, String teacherName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.maximumStudentsAllowed = maximumStudentsAllowed;
        this.facultySection = facultySection;
        this.year = year;
        this.teacherName = teacherName;
    }

    public CourseDTO() {
    }

    public static List<CourseDTO> convertToDTO(List<Course> courses) {
        List<CourseDTO> courseDTOS = new ArrayList<>();
        courses.forEach(course -> courseDTOS.add(new CourseDTO(course.getCourseId(), course.getCourseName(), course.getDescription(), course.getCategory(), course.getMaximumStudentsAllowed(), course.getFacultySection().toString(), course.getYear(), course.getTeacherName())));
        return courseDTOS;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
