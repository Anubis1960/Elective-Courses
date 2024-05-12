package com.Spring.application.controller;

import com.Spring.application.dto.CourseDTO;
import com.Spring.application.entity.Course;
import com.Spring.application.exceptions.InvalidInput;
import com.Spring.application.exceptions.ObjectNotFound;
import com.Spring.application.service.impl.CourseServiceImpl;
import com.Spring.application.service.impl.EnrollmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseServiceImpl courseService;
    @Autowired
    private EnrollmentServiceImpl enrollmentServiceImpl;

    @PostMapping("/")
    public ResponseEntity<CourseDTO> addCourse(
            @RequestParam String courseName,
            @RequestParam String category,
            @RequestParam String description,
            @RequestParam Integer year,
            @RequestParam Integer maxStudentsAllowed,
            @RequestParam String facultySection,
            @RequestParam String teacherName) throws ObjectNotFound, InvalidInput {
        Course course = courseService.addCourse(courseName, category, description, year, maxStudentsAllowed, facultySection, teacherName);
        CourseDTO courseDTO = new CourseDTO(course.getCourseId(), course.getCourseName(), course.getDescription(), course.getCategory(), course.getMaximumStudentsAllowed(), course.getFacultySection().toString(), course.getYear(), course.getTeacherName(), 0);
        return new ResponseEntity<>(courseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(
            @PathVariable("id") Long courseId,
            @RequestParam String courseName,
            @RequestParam String category,
            @RequestParam String description,
            @RequestParam Integer year,
            @RequestParam Integer maxStudentsAllowed,
            @RequestParam String facultySection,
            @RequestParam String teacherName) throws ObjectNotFound, InvalidInput {
        Course course = courseService.updateCourse(courseId, courseName, category, description, year, maxStudentsAllowed, facultySection, teacherName);
        CourseDTO courseDTO = new CourseDTO(course.getCourseId(), course.getCourseName(), course.getDescription(), course.getCategory(), course.getMaximumStudentsAllowed(), course.getFacultySection().toString(), course.getYear(), course.getTeacherName(), enrollmentServiceImpl.countByCourseId(courseId));
        return new ResponseEntity<>(courseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CourseDTO> deleteCourse(@PathVariable("id") Long courseId) throws ObjectNotFound {
        Course course = courseService.deleteCourse(courseId);
        CourseDTO courseDTO = new CourseDTO(course.getCourseId(), course.getCourseName(), course.getDescription(), course.getCategory(), course.getMaximumStudentsAllowed(), course.getFacultySection().toString(), course.getYear(), course.getTeacherName(), 0);
        return new ResponseEntity<>(courseDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable("id") Long courseId) throws ObjectNotFound {
        Course course = courseService.getCourseById(courseId);
        CourseDTO courseDTO = new CourseDTO(course.getCourseId(), course.getCourseName(), course.getDescription(), course.getCategory(), course.getMaximumStudentsAllowed(), course.getFacultySection().toString(), course.getYear(), course.getTeacherName(), enrollmentServiceImpl.countByCourseId(courseId));
        return new ResponseEntity<>(courseDTO, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return getListResponseEntity(courses);
    }

    @GetMapping("/{facultySection}/{year}")
    public ResponseEntity<List<CourseDTO>> getAllCoursesByFacultySectionAndYear(
            @PathVariable("facultySection") String facultySection,
            @PathVariable("year") int year) {
        List<Course> courses = courseService.findAllCoursesByYearAndFacultySection(year, facultySection);
        return getListResponseEntity(courses);
    }

    private ResponseEntity<List<CourseDTO>> getListResponseEntity(List<Course> courses) {
        List<Integer> numberOfStudents = new ArrayList<>();
        courses.forEach(course -> numberOfStudents.add(enrollmentServiceImpl.countByCourseId(course.getCourseId())));
        if (courses.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        List<CourseDTO> courseDTOs = CourseDTO.convertToDTO(courses, numberOfStudents);
        return new ResponseEntity<>(courseDTOs, HttpStatus.OK);
    }
}
