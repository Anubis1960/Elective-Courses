package com.Spring.Main.service;

import com.Spring.Main.entity.Course;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CourseService {
    ResponseEntity<String> addCourse(String courseName, String category, String description, Integer year, Integer maxStudentsAllowed, String facultySection, String teacherName);
    ResponseEntity<String> updateCourse(Long courseId, String courseName, String category, String description, Integer year, Integer maxStudentsAllowed, String facultySection, String teacherName);
    ResponseEntity<String> deleteCourse(Long courseId);
    ResponseEntity<Course> getCourseById(Long courseId);
    ResponseEntity<List<Course>> getAllCourses();
}
