package com.Spring.application.controller;

import com.Spring.application.entity.Student;
import com.Spring.application.service.impl.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentServiceImpl studentService;

    @PostMapping("/addstudent")
    public ResponseEntity<String> addStudent(String name, Float grade, String facultySection, Integer year) {
        return studentService.addStudent(name, "STUDENT", grade, facultySection, year);
    }

    @PutMapping("/updatestudent")
    public ResponseEntity<String> updateStudent(Long userId, String name, String role, Float grade, String facultySection, Integer year) {
        return studentService.updateStudent(userId, name, role, grade, facultySection, year);
    }

    @DeleteMapping("/deletestudent")
    public ResponseEntity<String> deleteStudent(Long id) {
        return studentService.deleteStudent(id);
    }

    @GetMapping("/getstudentsyid")
    public ResponseEntity<Student> getStudentById(Long id) {
        return studentService.getStudentById(id);
    }

    @GetMapping("/getallstudents")
    public ResponseEntity<List<Student>> getAllStudents() {
        return studentService.getAllStudents();
    }

}
