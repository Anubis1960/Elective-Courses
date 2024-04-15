package com.Spring.Main.Controller;

import com.Spring.Main.Entity.Course;
import com.Spring.Main.Entity.CourseSchedule;
import com.Spring.Main.Entity.Student;
import com.Spring.Main.Entity.User;
import com.Spring.Main.Service.Impl.CourseScheduleServiceImpl;
import com.Spring.Main.Service.Impl.CourseServiceImpl;
import com.Spring.Main.Service.Impl.StudentServiceImpl;
import com.Spring.Main.Service.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.List;

@RestController
public class AdminController {
    @Autowired
    StudentServiceImpl studentService;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    CourseScheduleServiceImpl courseScheduleService;
    @Autowired
    CourseServiceImpl courseService;


    @PostMapping("/addStudent")
    public ResponseEntity<String> addStudent(Long id, Float grade, String facultySection, Integer year) {
        return studentService.addStudent(id, grade, facultySection, year);
    }

    @PostMapping("/addUser")
    public ResponseEntity<String> addUser(String name, String role) {
        return userService.addUser(name, role);
    }

    @PostMapping("/updateUser")
    public ResponseEntity<String> updateUser(Long id, String name, String role) {
        return userService.updateUser(id, name, role);
    }

    @PostMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(Long id) {
        return userService.deleteUser(id);
    }

    @PostMapping("/updateStudent")
    public ResponseEntity<String> updateStudent(Long id, Float grade, String facultySection, Integer year) {
        return studentService.updateStudent(id, grade, facultySection, year);
    }

    @PostMapping("/deleteStudent")
    public ResponseEntity<String> deleteStudent(Long id) {
        return studentService.deleteStudent(id);
    }

    @GetMapping("/getStudentById")
    public ResponseEntity<Student> getStudentById(Long id) {
        return studentService.getStudentById(id);
    }

    @GetMapping("/getAllStudents")
    public ResponseEntity<List<Student>> getAllStudents() {
        return studentService.getAllStudents();
    }

    @PostMapping("/addCourse")
    public ResponseEntity<String> addCourse(String courseName, String category, String description, Integer year, Integer maxStudentsAllowed, String facultySection, String teacherName) {
        return courseService.addCourse(courseName, category, description, year, maxStudentsAllowed, facultySection, teacherName);
    }

    @PostMapping("/updateCourse")
    public ResponseEntity<String> updateCourse(Long courseId, String courseName, String category, String description, Integer year, Integer maxStudentsAllowed, String facultySection, String teacherName) {
        return courseService.updateCourse(courseId, courseName, category, description, year, maxStudentsAllowed, facultySection, teacherName);
    }

    @PostMapping("/deleteCourse")
    public ResponseEntity<String> deleteCourse(Long courseId) {
        return courseService.deleteCourse(courseId);
    }

    @GetMapping("/getCourseById")
    public ResponseEntity<Course> getCourseById(Long courseId) {
        return courseService.getCourseById(courseId);
    }

    @GetMapping("/getAllCourses")
    public ResponseEntity<List<Course>> getAllCourses() {
        return courseService.getAllCourses();
    }

    @PostMapping("/addCourseSchedule")
    public ResponseEntity<String> addCourseSchedule(Long courseId, String day, String startTime, String endTime) {
        return courseScheduleService.addCourseSchedule(courseId, day, startTime, endTime);
    }

    @PostMapping("/updateCourseSchedule")
    public ResponseEntity<String> updateCourseSchedule(Long courseId, String day, String startTime, String endTime) {
        return courseScheduleService.updateCourseSchedule(courseId, day, startTime, endTime);
    }

    @PostMapping("/deleteCourseSchedule")
    public ResponseEntity<String> deleteCourseSchedule(Long id) {
        return courseScheduleService.deleteCourseSchedule(id);
    }

    @GetMapping("/getCourseScheduleById")
    public ResponseEntity<CourseSchedule> getCourseScheduleById(Long id) {
        return courseScheduleService.getCourseScheduleById(id);
    }

    @GetMapping("/getAllCourseSchedules")
    public ResponseEntity<List<CourseSchedule>> getAllCourseSchedules() {
        return courseScheduleService.getAllCourseSchedules();
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        return userService.getAllUsers();
    }

}
