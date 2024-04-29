package com.Spring.application.service.impl;

import com.Spring.application.entity.Course;
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
    public Course addCourse(String courseName, String category, String description, Integer year, Integer maxStudentsAllowed, String facultySection, String teacherName) {
        Course course = new Course(courseName, category, description, year, maxStudentsAllowed, facultySection, teacherName);
        courseRepository.save(course);
        return course;
    }

    @Override
    public Course updateCourse(Long courseId, String courseName, String category, String description, Integer year, Integer maxStudentsAllowed, String facultySection, String teacherName) throws ObjectNotFound {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            throw new ObjectNotFound("Course not found");
        }
        course.setCourseName(courseName);
        course.setCategory(category);
        course.setDescription(description);
        course.setYear(year);
        course.setMaximumStudentsAllowed(maxStudentsAllowed);
        course.setFacultySection(facultySection);
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
}
