package com.Spring.Main.service.impl;

import com.Spring.Main.entity.Student;
import com.Spring.Main.enums.Role;
import com.Spring.Main.repository.StudentRepository;
import com.Spring.Main.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService{

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public ResponseEntity<String> addStudent(String name, String role, Float grade, String facultySection, Integer year) {
        try{
            Student student = new Student(name, Role.valueOf(role), grade, facultySection, year);
            studentRepository.save(student);
            return ResponseEntity.ok("Student added successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Error adding student");
    }

    @Override
    public ResponseEntity<String> updateStudent(Long userId, String name, String role, Float grade, String facultySection, Integer year) {
        try {
            Student student = studentRepository.findById(userId).orElse(null);
            if (student == null) {
                return ResponseEntity.badRequest().body("Student not found");
            }
            student.setName(name);
            student.setRole(Role.valueOf(role));
            student.setGrade(grade);
            student.setFacultySection(facultySection);
            student.setYear(year);
            studentRepository.save(student);
            return ResponseEntity.ok("Student updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Error updating student");
    }

    @Override
    public ResponseEntity<String> deleteStudent(Long id) {
        try{
            studentRepository.deleteById(id);
            return ResponseEntity.ok("Student deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Error deleting student");
    }

    @Override
    public ResponseEntity<Student> getStudentById(Long id) {
        try{
            Student student = studentRepository.findById(id).orElse(null);
            if (student == null) {
                return ResponseEntity.badRequest().body(null);
            }

            return ResponseEntity.ok(student);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body(null);
    }

    @Override
    public ResponseEntity<List<Student>> getAllStudents() {
        try{
            List<Student> students = studentRepository.findAll();
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body(null);
    }
}
