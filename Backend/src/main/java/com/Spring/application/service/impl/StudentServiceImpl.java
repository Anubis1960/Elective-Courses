package com.Spring.application.service.impl;

import com.Spring.application.entity.Student;
import com.Spring.application.enums.FacultySection;
import com.Spring.application.enums.Role;
import com.Spring.application.exceptions.InvalidInput;
import com.Spring.application.exceptions.ObjectNotFound;
import com.Spring.application.repository.EnrollmentRepository;
import com.Spring.application.repository.StudentRepository;
import com.Spring.application.service.StudentService;
import com.Spring.application.utils.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService{

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    public Student addStudent(String name, Float grade, String facultySection, Integer year, String email, String password) throws NoSuchAlgorithmException, InvalidInput {
        if (grade <= 0 || grade > 10){
            return null;
        }
        Student student = new Student(name, Role.STUDENT, grade, FacultySection.valueOf(facultySection), year, email, Encrypt.toHexString(Encrypt.encrypt(password)));
        studentRepository.save(student);
        return student;
    }

    @Override
    public Student updateStudent(Long userId, String name, String role, Float grade, String facultySection, Integer year, String email, String password) throws ObjectNotFound, NoSuchAlgorithmException, InvalidInput {
        Student student = studentRepository.findById(userId).orElse(null);
        if (student == null) {
            return null;
        }
        if (grade <= 0 || grade > 10){
            return null;
        }
        student.setName(name);
        student.setRole(Role.valueOf(role));
        student.setGrade(grade);
        student.setFacultySection(FacultySection.valueOf(facultySection));
        student.setYear(year);
        student.setEmail(email);
        student.setPassword(Encrypt.toHexString(Encrypt.encrypt(password)));
        studentRepository.save(student);
        return student;
    }

    @Override
    public Student deleteStudent(Long id) throws ObjectNotFound {
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            return null;
        }
        enrollmentRepository.deleteByStudentId(id);
        studentRepository.deleteById(id);
        return student;
    }

    @Override
    public Student getStudentById(Long id) throws ObjectNotFound {
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
//            System.out.println("Student not found");
            return null;
        }
        return student;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentByEmailAndPassword(String email, String password) {
        return studentRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public List<Student> getAllStudentsByCourseId(Long courseId) {
        return studentRepository.findAllStudentsByCourseId(courseId);
    }

    @Override
    public Student getStudentByName(String name) {
        return studentRepository.findByName(name);
    }

    @Override
    public List<Student> getAcceptedStudentsByCourseId(Long courseId) {
        return studentRepository.findAcceptedStudentsByCourseId(courseId);
    }
}
