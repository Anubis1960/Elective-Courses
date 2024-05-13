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
    List<String> findAllCategoriesByYear(int year);
    List<Course> findAllCoursesByCategoryAndYear(String category, int year);
    List<Course> findAllCoursesByYearAndFacultySection(int year, String facultySection);
    List<Course> findAllCoursesOrderByASC(String field, String order);
    List<Course> findAllCoursesOrderByDESC(String field, String order);
    List<Course> getCoursesOfStudent(Long studentId) throws ObjectNotFound;
}
