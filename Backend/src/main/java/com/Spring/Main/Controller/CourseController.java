package com.Spring.Main.controller;

import com.Spring.Main.entity.Course;
import com.Spring.Main.service.CourseService;
import com.Spring.Main.service.impl.CourseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/course")
public class CourseController {
    @Autowired
    private CourseServiceImpl courseService;

    @PostMapping("/addcourse")
    public ResponseEntity<String> addCourse(String courseName, String category, String description, Integer year, Integer maxStudentsAllowed, String facultySection, String teacherName) {
        return courseService.addCourse(courseName, category, description, year, maxStudentsAllowed, facultySection, teacherName);
    }

    @PostMapping("/updatecourse")
    public ResponseEntity<String> updateCourse(Long courseId, String courseName, String category, String description, Integer year, Integer maxStudentsAllowed, String facultySection, String teacherName) {
        return courseService.updateCourse(courseId, courseName, category, description, year, maxStudentsAllowed, facultySection, teacherName);
    }

    @PostMapping("/deletecourse")
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
