package com.Spring.application.service;

import com.Spring.application.entity.Course;
import com.Spring.application.enums.FacultySection;
import com.Spring.application.exceptions.InvalidInput;
import com.Spring.application.exceptions.ObjectNotFound;

import java.util.List;

public interface CourseService {
    Course addCourse(String courseName, String category, String description, Integer year, Integer maxStudentsAllowed, String facultySection, String teacherName) throws InvalidInput, ObjectNotFound;
    Course updateCourse(Long courseId, String courseName, String category, String description, Integer year, Integer maxStudentsAllowed, String facultySection, String teacherName) throws ObjectNotFound, InvalidInput;
    Course deleteCourse(Long courseId) throws ObjectNotFound;
    Course getCourseById(Long courseId) throws ObjectNotFound;
    List<Course> getAllCourses();
    List<Course> getCoursesOfStudent(Long studentId) throws ObjectNotFound;
    Course getCourseByName(String name);
    List<Course> getAcceptedCoursesByStudentId(Long studentId);
    List<Course> getAvailableCourses(Long courseId, Integer year, FacultySection facultySection, String category);
    List<String> getFacultySections();
    List<Course> getPendingCoursesByStudentId(Long studentId);
}
