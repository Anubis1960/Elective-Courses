package com.Spring.application.service.impl;

import com.Spring.application.entity.Course;
import com.Spring.application.entity.Student;
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
    @Autowired
    private StudentServiceImpl studentService;

    @Override
    public Course addCourse(String courseName, String category, String description, Integer year, Integer maxStudentsAllowed, String facultySection, String teacherName) throws ObjectNotFound, InvalidInput {
        if (year <= 0){
            return null;
        }
        if (maxStudentsAllowed <= 0){
            return null;
        }
        Course course = new Course(courseName, category, description, year, maxStudentsAllowed, FacultySection.valueOf(facultySection), teacherName);
        courseRepository.save(course);
        return course;
    }

    @Override
    public Course updateCourse(Long courseId, String courseName, String category, String description, Integer year, Integer maxStudentsAllowed, String facultySection, String teacherName) throws InvalidInput, ObjectNotFound {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            return null;
        }
        if (year <= 0){
            return null;
        }
        if (maxStudentsAllowed <= 0){
            return null;
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
            return null;
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
            return null;
        }
        return course;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public List<Course> getCoursesOfStudent(Long studentId) throws ObjectNotFound {
        Student student = studentService.getStudentById(studentId);
//        System.out.println("student = " + student);
        //        for (Course course : courses) {
//            System.out.println("course = " + course);
//        }
        return courseRepository.findAllCoursesByYearAndFacultySection(student.getYear(), student.getFacultySection());
    }

    @Override
    public Course getCourseByName(String name) {
        return courseRepository.findByCourseName(name);
    }

    @Override
    public List<Course> getAcceptedCoursesByStudentId(Long studentId) {
        return courseRepository.findAcceptedCoursesByStudentId(studentId);
    }

    @Override
    public List<Course> getAvailableCourses(Long courseId, Integer year, FacultySection facultySection, String category) {
        return courseRepository.findAvailableCourses(courseId, year, facultySection, category);
    }

    @Override
    public List<String> getFacultySections() { return courseRepository.findFacultySections();}

    @Override
    public List<Course> getPendingCoursesByStudentId(Long studentId) {
        return courseRepository.findPendingCoursesByStudentId(studentId);
    }
}
