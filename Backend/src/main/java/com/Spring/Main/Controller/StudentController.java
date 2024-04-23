package com.Spring.Main.controller;

import com.Spring.Main.entity.Student;
import com.Spring.Main.service.impl.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/student")

public class StudentController {
    @Autowired
    private StudentServiceImpl studentService;

    @PostMapping("/addStudent")
    public ResponseEntity<String> addStudent(String name, String role, Float grade, String facultySection, Integer year) {
        return studentService.addStudent(name, role, grade, facultySection, year);
    }

    @PostMapping("/updateStudent")
    public ResponseEntity<String> updateStudent(Long userId, String name, String role, Float grade, String facultySection, Integer year) {
        return studentService.updateStudent(userId, name, role, grade, facultySection, year);
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

}
