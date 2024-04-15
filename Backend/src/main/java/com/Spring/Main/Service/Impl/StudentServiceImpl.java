package com.Spring.Main.Service.Impl;

import com.Spring.Main.Entity.Student;
import com.Spring.Main.Entity.User;
import com.Spring.Main.Repository.StudentRepository;
import com.Spring.Main.Repository.UserRepository;
import com.Spring.Main.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService{

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<String> addStudent(Long id, Float grade, String facultySection, Integer year) {
        try{
            User user = userRepository.findById(id).orElse(null);

            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            Student student = new Student(grade, facultySection, year, user);
            studentRepository.save(student);

            return ResponseEntity.ok("Student added successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Error adding student");
    }

    @Override
    public ResponseEntity<String> updateStudent(Long id, Float grade, String facultySection, Integer year) {
        try {
            Student student = studentRepository.findById(id).orElse(null);

            if (student == null) {
                return ResponseEntity.badRequest().body("Student not found");
            }

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
