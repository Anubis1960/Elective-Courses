package com.Spring.application.service;

import com.Spring.application.entity.Student;
import com.Spring.application.exceptions.InvalidInput;
import com.Spring.application.exceptions.ObjectNotFound;

import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

public interface StudentService {
    Student addStudent(String name, Float grade, String facultySection, Integer year, String email, String password) throws NoSuchAlgorithmException, InvalidInput;
    Student updateStudent(Long userId, String name, String role, Float grade, String facultySection, Integer year, String email, String password) throws InvalidInput,ObjectNotFound, NoSuchAlgorithmException;
    Student deleteStudent(Long id) throws ObjectNotFound;
    Student getStudentById(Long id) throws ObjectNotFound;
    List<Student> getAllStudents();
    Student getStudentByEmailAndPassword(String email, String password);
    List<Student> getAllStudentsByCourseId(Long courseId);
    Student getStudentByName(String name);
    List<Student> getAcceptedStudentsByCourseId(Long courseId);
    List<Student> executeQuery(String query);
    void export(OutputStream out, Optional<String> facultySection, Optional<Integer> year, boolean includeName, boolean includeEmail, boolean includeGrade, boolean includeSection, boolean includeYear, String extension) throws IOException, IllegalAccessException;
}
