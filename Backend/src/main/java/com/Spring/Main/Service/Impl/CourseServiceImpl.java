package com.Spring.Main.service.impl;

import com.Spring.Main.entity.Course;
import com.Spring.Main.repository.CourseRepository;
import com.Spring.Main.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService{

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public ResponseEntity<String> addCourse(String courseName, String category, String description, Integer year, Integer maxStudentsAllowed, String facultySection, String teacherName) {
        try {
            Course course = new Course(courseName, category, description, year, maxStudentsAllowed, facultySection, teacherName);
            courseRepository.save(course);
            return ResponseEntity.ok("Course added successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Error adding course");
    }

    @Override
    public ResponseEntity<String> updateCourse(Long courseId, String courseName, String category, String description, Integer year, Integer maxStudentsAllowed, String facultySection, String teacherName) {
        try {
            Course course = courseRepository.findById(courseId).orElse(null);
            if (course == null) {
                return ResponseEntity.ok("Course not found");
            }

            course.setCourseName(courseName);
            course.setCategory(category);
            course.setDescription(description);
            course.setYear(year);
            course.setMaximumStudentsAllowed(maxStudentsAllowed);
            course.setFacultySection(facultySection);
            course.setTeacherName(teacherName);
            courseRepository.save(course);

            return ResponseEntity.ok("Course updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Error updating course");
    }

    @Override
    public ResponseEntity<String> deleteCourse(Long courseId) {
        try {
            courseRepository.deleteById(courseId);
            return ResponseEntity.ok("Course deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Error deleting course");
    }

    @Override
    public ResponseEntity<Course> getCourseById(Long courseId) {
        try {
            Course course = courseRepository.findById(courseId).orElse(null);
            if (course == null) {
                return ResponseEntity.badRequest().body(null);
            }

            return ResponseEntity.ok(course);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<List<Course>> getAllCourses() {
        try {
            List<Course> courses = courseRepository.findAll();
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body(null);
    }
}
