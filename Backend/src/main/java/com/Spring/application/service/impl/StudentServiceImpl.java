package com.Spring.application.service.impl;

import com.Spring.application.entity.Student;
import com.Spring.application.enums.Role;
import com.Spring.application.exceptions.ObjectNotFound;
import com.Spring.application.repository.EnrollmentRepository;
import com.Spring.application.repository.StudentRepository;
import com.Spring.application.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService{

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    public Student addStudent(String name, Float grade, String facultySection, Integer year) {
        Student student = new Student(name, Role.STUDENT, grade, facultySection, year);
        studentRepository.save(student);
        return student;
    }

    @Override
    public Student updateStudent(Long userId, String name, String role, Float grade, String facultySection, Integer year) throws ObjectNotFound {
        Student student = studentRepository.findById(userId).orElse(null);
        if (student == null) {
            throw new ObjectNotFound("Student not found");
        }
        student.setName(name);
        student.setRole(Role.valueOf(role));
        student.setGrade(grade);
        student.setFacultySection(facultySection);
        student.setYear(year);
        studentRepository.save(student);
        return student;
    }

    @Override
    public Student deleteStudent(Long id) throws ObjectNotFound {
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            throw new ObjectNotFound("Student not found");
        }
        enrollmentRepository.deleteByStudentId(id);
        studentRepository.deleteById(id);
        return student;
    }

    @Override
    public Student getStudentById(Long id) throws ObjectNotFound {
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            throw new ObjectNotFound("Student not found");
        }
        return student;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}
