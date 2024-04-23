package com.Spring.Main.service;

import com.Spring.Main.entity.Student;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StudentService {
    ResponseEntity<String> addStudent(String name, String role, Float grade, String facultySection, Integer year);
    ResponseEntity<String> updateStudent(Long userId, String name, String role, Float grade, String facultySection, Integer year);
    ResponseEntity<String> deleteStudent(Long id);
    ResponseEntity<Student> getStudentById(Long id);
    ResponseEntity<List<Student>> getAllStudents();
}
