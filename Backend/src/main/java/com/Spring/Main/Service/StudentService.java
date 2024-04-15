package com.Spring.Main.Service;

import com.Spring.Main.Entity.Student;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StudentService {
    ResponseEntity<String> addStudent(Long id, Float grade, String facultySection, Integer year);
    ResponseEntity<String> updateStudent(Long id, Float grade, String facultySection, Integer year);
    ResponseEntity<String> deleteStudent(Long id);
    ResponseEntity<Student> getStudentById(Long id);
    ResponseEntity<List<Student>> getAllStudents();
}
