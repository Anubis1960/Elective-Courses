package com.Spring.application.controller;

import com.Spring.application.entity.Course;
import com.Spring.application.service.impl.CourseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseServiceImpl courseService;

    @PostMapping("/addcourse")
    public ResponseEntity<String> addCourse(String courseName, String category, String description, Integer year, Integer maxStudentsAllowed, String facultySection, String teacherName) {
        return courseService.addCourse(courseName, category, description, year, maxStudentsAllowed, facultySection, teacherName);
    }

    @PutMapping("/updatecourse")
    public ResponseEntity<String> updateCourse(Long courseId, String courseName, String category, String description, Integer year, Integer maxStudentsAllowed, String facultySection, String teacherName) {
        return courseService.updateCourse(courseId, courseName, category, description, year, maxStudentsAllowed, facultySection, teacherName);
    }

    @DeleteMapping("/deletecourse")
    public ResponseEntity<String> deleteCourse(Long courseId) {
        return courseService.deleteCourse(courseId);
    }

    @GetMapping("/getcoursebyid")
    public ResponseEntity<Course> getCourseById(Long courseId) {
        return courseService.getCourseById(courseId);
    }

    @GetMapping("/getallcourses")
    public ResponseEntity<List<Course>> getAllCourses() {
        return courseService.getAllCourses();
    }
}
