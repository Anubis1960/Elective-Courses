package com.Spring.application.service.impl;

import com.Spring.application.entity.Course;
import com.Spring.application.enums.FacultySection;
import com.Spring.application.exceptions.InvalidInput;
import com.Spring.application.exceptions.ObjectNotFound;
import com.Spring.application.repository.CourseRepository;
import com.Spring.application.repository.CourseScheduleRepository;
import com.Spring.application.repository.EnrollmentRepository;
import com.Spring.application.service.CourseService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService{

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseScheduleRepository courseScheduleRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    public Course addCourse(String courseName, String category, String description, Integer year, Integer maxStudentsAllowed, String facultySection, String teacherName) throws ObjectNotFound, InvalidInput {
        if (year <= 0){
            throw new InvalidInput("Invalid year");
        }
        if (maxStudentsAllowed <= 0){
            throw new InvalidInput("Invalid number of students");
        }
        Course course = new Course(courseName, category, description, year, maxStudentsAllowed, FacultySection.valueOf(facultySection), teacherName);
        courseRepository.save(course);
        return course;
    }

    @Override
    public Course updateCourse(Long courseId, String courseName, String category, String description, Integer year, Integer maxStudentsAllowed, String facultySection, String teacherName) throws InvalidInput, ObjectNotFound {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            throw new ObjectNotFound("Course not found");
        }
        if (year <= 0){
            throw new InvalidInput("Invalid year");
        }
        if (maxStudentsAllowed <= 0){
            throw new InvalidInput("Invalid number of students");
        }
        course.setCourseName(courseName);
        course.setCategory(category);
        course.setDescription(description);
        course.setYear(year);
        course.setMaximumStudentsAllowed(maxStudentsAllowed);
        course.setFacultySection(FacultySection.valueOf(facultySection));
        course.setTeacherName(teacherName);
        courseRepository.save(course);
        return course;
    }

    @Override
    @Transactional
    public Course deleteCourse(Long courseId) throws ObjectNotFound {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            throw new ObjectNotFound("Course not found");
        }
        courseScheduleRepository.deleteById(courseId);
        enrollmentRepository.deleteByCourseId(courseId);
        courseRepository.deleteById(courseId);
        return course;
    }

    @Override
    public Course getCourseById(Long courseId) throws ObjectNotFound {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            throw new ObjectNotFound("Course not found");
        }
        return course;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public List<String> findAllCategoriesByYear(int year) {
        return courseRepository.findAllCategoriesByYear(year);
    }

    @Override
    public List<Course> findAllCoursesByCategoryAndYear(String category, int year) {
        return courseRepository.findAllCoursesByCategoryAndYear(category, year);
    }

    @Override
    public List<Course> findAllCoursesByYearAndFacultySection(int year, String facultySection) {
        return courseRepository.findAllCoursesByYearAndFacultySection(year, facultySection);
    }

    @Override
    public List<Course> findAllCoursesOrderByASC(String field, String order) {
        return courseRepository.findAllCoursesOrderByASC(field, order);
    }

    @Override
    public List<Course> findAllCoursesOrderByDESC(String field, String order) {
        return courseRepository.findAllCoursesOrderByDESC(field, order);
    }
}
