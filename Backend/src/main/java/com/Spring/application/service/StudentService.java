package com.Spring.application.service;

import com.Spring.application.entity.Student;
import com.Spring.application.exceptions.InvalidInput;
import com.Spring.application.exceptions.ObjectNotFound;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface StudentService {
    Student addStudent(String name, Float grade, String facultySection, Integer year, String email, String password) throws NoSuchAlgorithmException, InvalidInput;
    Student updateStudent(Long userId, String name, String role, Float grade, String facultySection, Integer year, String email, String password) throws InvalidInput,ObjectNotFound, NoSuchAlgorithmException;
    Student deleteStudent(Long id) throws ObjectNotFound;
    Student getStudentById(Long id) throws ObjectNotFound;
    List<Student> getAllStudents();
    Student getStudentByEmailAndPassword(String email, String password);
    List<Student> getAllStudentsByCourseId(Long courseId);
    Student getStudentByName(String name);
    List<Long> getAcceptedEnrollmentsByCourseId(Long courseId);
}
