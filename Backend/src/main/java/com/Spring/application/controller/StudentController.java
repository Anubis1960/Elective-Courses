package com.Spring.application.controller;

import com.Spring.application.entity.Student;
import com.Spring.application.exceptions.InvalidInput;
import com.Spring.application.exceptions.ObjectNotFound;
import com.Spring.application.service.impl.StudentServiceImpl;
import com.Spring.application.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.Spring.application.utils.Encrypt;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentServiceImpl studentService;

    @PostMapping("/")
    @JsonView(Views.Public.class)
    public ResponseEntity<Student> addStudent(@RequestParam String name,
                                              @RequestParam Float grade,
                                              @RequestParam String facultySection,
                                              @RequestParam Integer year,
                                              @RequestParam String email,
                                              @RequestParam String password) throws NoSuchAlgorithmException, InvalidInput {
        Student student = studentService.addStudent(name, grade, facultySection, year, email, password);
        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<Student> updateStudent(@PathVariable("id") Long userId,
                                                 @RequestParam String name,
                                                 @RequestParam String role,
                                                 @RequestParam Float grade,
                                                 @RequestParam String facultySection,
                                                 @RequestParam Integer year,
                                                 @RequestParam String email,
                                                 @RequestParam String password) throws InvalidInput, ObjectNotFound, NoSuchAlgorithmException {
        Student student = studentService.updateStudent(userId, name, role, grade, facultySection, year, email, password);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<Student> deleteStudent(@PathVariable("id")  Long id) throws ObjectNotFound {
        Student student = studentService.deleteStudent(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<Student> getStudentById(@PathVariable("id")  Long id) throws ObjectNotFound {
        Student student = studentService.getStudentById(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @GetMapping("/")
    @JsonView(Views.Public.class)
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

}
