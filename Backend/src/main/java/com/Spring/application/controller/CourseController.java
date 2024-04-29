package com.Spring.application.controller;

import com.Spring.application.entity.Course;
import com.Spring.application.exceptions.ObjectNotFound;
import com.Spring.application.service.impl.CourseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseServiceImpl courseService;

    @PostMapping("/")
    public ResponseEntity<Course> addCourse(@RequestParam String courseName,
                                            @RequestParam String category,
                                            @RequestParam String description,
                                            @RequestParam Integer year,
                                            @RequestParam Integer maxStudentsAllowed,
                                            @RequestParam String facultySection,
                                            @RequestParam String teacherName) throws ObjectNotFound {
        Course course = courseService.addCourse(courseName, category, description, year, maxStudentsAllowed, facultySection, teacherName);
        return new ResponseEntity<>(course, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable("id") Long courseId, String courseName, String category, String description, Integer year, Integer maxStudentsAllowed, String facultySection, String teacherName) throws ObjectNotFound {
        Course course = courseService.updateCourse(courseId, courseName, category, description, year, maxStudentsAllowed, facultySection, teacherName);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Course> deleteCourse(@PathVariable("id") Long courseId) throws ObjectNotFound {
        Course course = courseService.deleteCourse(courseId);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable("id") Long courseId) throws ObjectNotFound {
        Course course = courseService.getCourseById(courseId);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }
}
