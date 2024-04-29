package com.Spring.application.controller;

import com.Spring.application.entity.Student;
import com.Spring.application.exceptions.ObjectNotFound;
import com.Spring.application.service.impl.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentServiceImpl studentService;

    @PostMapping("/")
    public ResponseEntity<Student> addStudent(String name, Float grade, String facultySection, Integer year) {
        Student student = studentService.addStudent(name, grade, facultySection, year);
        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable("id") Long userId, String name, String role, Float grade, String facultySection, Integer year) throws ObjectNotFound {
        Student student = studentService.updateStudent(userId, name, role, grade, facultySection, year);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable("id")  Long id) throws ObjectNotFound {
        Student student = studentService.deleteStudent(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable("id")  Long id) throws ObjectNotFound {
        Student student = studentService.getStudentById(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

}
