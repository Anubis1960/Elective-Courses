package com.Spring.application.service;

import com.Spring.application.entity.Course;
import com.Spring.application.exceptions.InvalidInput;
import com.Spring.application.exceptions.ObjectNotFound;

import java.util.List;

public interface CourseService {
    Course addCourse(String courseName, String category, String description, Integer year, Integer maxStudentsAllowed, String facultySection, String teacherName) throws InvalidInput, ObjectNotFound;
    Course updateCourse(Long courseId, String courseName, String category, String description, Integer year, Integer maxStudentsAllowed, String facultySection, String teacherName) throws ObjectNotFound, InvalidInput;
    Course deleteCourse(Long courseId) throws ObjectNotFound;
    Course getCourseById(Long courseId) throws ObjectNotFound;
    List<Course> getAllCourses();
}
