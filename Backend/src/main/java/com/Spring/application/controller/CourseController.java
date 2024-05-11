package com.Spring.application.controller;

import com.Spring.application.dto.CourseDTO;
import com.Spring.application.entity.Course;
import com.Spring.application.exceptions.InvalidInput;
import com.Spring.application.exceptions.ObjectNotFound;
import com.Spring.application.service.impl.CourseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseServiceImpl courseService;

    @PostMapping("/")
    public ResponseEntity<CourseDTO> addCourse(String courseName, String category, String description, Integer year, Integer maxStudentsAllowed, String facultySection, String teacherName) throws ObjectNotFound, InvalidInput {
        Course course = courseService.addCourse(courseName, category, description, year, maxStudentsAllowed, facultySection, teacherName);
        CourseDTO courseDTO = new CourseDTO(course.getCourseId(), course.getCourseName(), course.getDescription(), course.getCategory(), course.getMaximumStudentsAllowed(), course.getFacultySection().toString(), course.getYear(), course.getTeacherName());
        return new ResponseEntity<>(courseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable("id") Long courseId,String courseName, String category, String description, Integer year, Integer maxStudentsAllowed, String facultySection, String teacherName) throws ObjectNotFound, InvalidInput {
        Course course = courseService.updateCourse(courseId, courseName, category, description, year, maxStudentsAllowed, facultySection, teacherName);
        CourseDTO courseDTO = new CourseDTO(course.getCourseId(), course.getCourseName(), course.getDescription(), course.getCategory(), course.getMaximumStudentsAllowed(), course.getFacultySection().toString(), course.getYear(), course.getTeacherName());
        return new ResponseEntity<>(courseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CourseDTO> deleteCourse(@PathVariable("id") Long courseId) throws ObjectNotFound {
        Course course = courseService.deleteCourse(courseId);
        CourseDTO courseDTO = new CourseDTO(course.getCourseId(), course.getCourseName(), course.getDescription(), course.getCategory(), course.getMaximumStudentsAllowed(), course.getFacultySection().toString(), course.getYear(), course.getTeacherName());
        return new ResponseEntity<>(courseDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable("id") Long courseId) throws ObjectNotFound {
        Course course = courseService.getCourseById(courseId);
        CourseDTO courseDTO = new CourseDTO(course.getCourseId(), course.getCourseName(), course.getDescription(), course.getCategory(), course.getMaximumStudentsAllowed(), course.getFacultySection().toString(), course.getYear(), course.getTeacherName());
        return new ResponseEntity<>(courseDTO, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
        List<CourseDTO> courseDTOs = CourseDTO.convertToDTO(courses);
        return new ResponseEntity<>(courseDTOs, HttpStatus.OK);
    }
}
