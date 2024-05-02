package com.Spring.application.service;

import com.Spring.application.entity.Student;
import com.Spring.application.exceptions.ObjectNotFound;

import java.util.List;

public interface StudentService {
    Student addStudent(String name, Float grade, String facultySection, Integer year, String email, String password);
    Student updateStudent(Long userId, String name, String role, Float grade, String facultySection, Integer year) throws ObjectNotFound;
    Student deleteStudent(Long id) throws ObjectNotFound;
    Student getStudentById(Long id) throws ObjectNotFound;
    List<Student> getAllStudents();
    Student getStudentByEmailAndPassword(String email, String password);
}
