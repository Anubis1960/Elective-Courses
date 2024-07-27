package com.Spring.application.service.impl;

import com.Spring.application.dto.StudentExporter;
import com.Spring.application.entity.Student;
import com.Spring.application.enums.FacultySection;
import com.Spring.application.enums.Role;
import com.Spring.application.exceptions.InvalidInput;
import com.Spring.application.exceptions.ObjectNotFound;
import com.Spring.application.repository.EnrollmentRepository;
import com.Spring.application.repository.StudentRepository;
import com.Spring.application.service.StudentService;
import com.Spring.application.utils.Encrypt;
import com.Spring.application.utils.GeneratorMethods;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService{

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public List<Student> executeQuery(String query) {
        return entityManager.createQuery(query).getResultList();
    }

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

    @Override
    public void export(OutputStream out, Optional<String> facultySection, Optional<Integer> year, boolean includeName, boolean includeEmail, boolean includeGrade, boolean includeSection, boolean includeYear, String extension) throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List<StudentExporter> students;

        StringBuilder query = new StringBuilder();

        query.append("SELECT");

        if (includeName) {
            query.append(" s.name,");
        }

        if (includeEmail) {
            query.append(" s.email,");
        }

        if (includeGrade) {
            query.append(" s.grade,");
        }

        if (includeSection) {
            query.append(" s.facultySection,");
        }

        if (includeYear) {
            query.append(" s.year,");
        }

        query.deleteCharAt(query.length() - 1);

        query.append(" FROM Student s");

        if (facultySection.isPresent() && year.isPresent()) {
            query.append(" WHERE s.facultySection =").append(facultySection.get()).append(" AND s.year = ").append(year.get());
        } else if (facultySection.isPresent()) {
            query.append(" WHERE s.facultySection =").append(facultySection.get());
        } else year.ifPresent(integer -> query.append(" WHERE s.year =").append(integer));

        List<Object> result = entityManager.createQuery(query.toString()).getResultList();

        students = new ArrayList<>();

        for (Object obj : result) {
            Object[] arr = (Object[]) obj;
            StudentExporter.Builder builder = new StudentExporter.Builder();
            int index = 0;

            if (includeName) builder.name((String) arr[index++]);
            if (includeEmail) builder.email((String) arr[index++]);
            if (includeGrade) builder.grade((Float) arr[index++]);
            if (includeSection) builder.section(arr[index++].toString());
            if (includeYear) builder.year((Integer) arr[index]);

            students.add(builder.build());
        }

//        for (StudentExporter student : students) {
//            System.out.println(student.getName() + " " + student.getEmail() + " " + student.getGrade() + " " + student.getSection() + " " + student.getYear());
//        }

        if (extension.equals("pdf")) {
            GeneratorMethods.writePDF(students, out);
        } else if (extension.equals("csv")) {
            GeneratorMethods.writeCSV(students, out);
        }
        else if (extension.equals("excel")) {
            GeneratorMethods.writeXLSX(students, out);
        }


    }

}
