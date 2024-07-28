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

    private static final int NAME_FLAG = 1 << 0; // 1
    private static final int GRADE_FLAG = 1 << 1; // 2
    private static final int SECTION_FLAG = 1 << 2; // 4
    private static final int YEAR_IDX = 1 << 3; //8
    private static final int EMAIL_FLAG = 1 << 4; // 16

    @PersistenceContext
    private EntityManager entityManager;

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
    public void export(OutputStream out, Optional<String> facultySection, Optional<Integer> year, int bitOptions, String extension) throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List<StudentExporter> students;

        StringBuilder query = new StringBuilder();

        query.append("SELECT");

        int columnCount = 0;

        if ((bitOptions & NAME_FLAG) != 0) {
            query.append(" s.name,");
            columnCount++;
        }

        if ((bitOptions & EMAIL_FLAG) != 0) {
            query.append(" s.email,");
            columnCount++;
        }

        if ((bitOptions & GRADE_FLAG) != 0) {
            query.append(" s.grade,");
            columnCount++;
        }

        if ((bitOptions & SECTION_FLAG) != 0) {
            query.append(" s.facultySection,");
            columnCount++;
        }

        if ((bitOptions & YEAR_IDX) != 0) {
            query.append(" s.year,");
            columnCount++;
        }

        query.deleteCharAt(query.length() - 1);

        query.append(" FROM Student s");

        if (facultySection.isPresent() && year.isPresent()) {
            query.append(" WHERE s.facultySection = ").append(facultySection.get()).append(" AND s.year = ").append(year.get());
        } else if (facultySection.isPresent()) {
            query.append(" WHERE s.facultySection = ").append(facultySection.get());
        } else year.ifPresent(integer -> query.append(" WHERE s.year = ").append(integer));

        List<Object> result = entityManager.createQuery(query.toString()).getResultList();

        students = new ArrayList<>();

        for (Object obj : result) {
            StudentExporter.StudentBuilder studentBuilder = new StudentExporter.StudentBuilder();

            if (columnCount == 1) {
                if ((bitOptions & NAME_FLAG) != 0) studentBuilder.name((String) obj);
                if ((bitOptions & EMAIL_FLAG) != 0) studentBuilder.email((String) obj);
                if ((bitOptions & GRADE_FLAG) != 0) studentBuilder.grade((Float) obj);
                if ((bitOptions & SECTION_FLAG) != 0) studentBuilder.section(obj.toString());
                if ((bitOptions & YEAR_IDX) != 0) studentBuilder.year((Integer) obj);
            } else {
                Object[] arr = (Object[]) obj;
                int index = 0;

                if ((bitOptions & NAME_FLAG) != 0) studentBuilder.name((String) arr[index++]);
                if ((bitOptions & EMAIL_FLAG) != 0) studentBuilder.email((String) arr[index++]);
                if ((bitOptions & GRADE_FLAG) != 0) studentBuilder.grade((Float) arr[index++]);
                if ((bitOptions & SECTION_FLAG) != 0) studentBuilder.section(arr[index++].toString());
                if ((bitOptions & YEAR_IDX) != 0) studentBuilder.year((Integer) arr[index]);
            }

            students.add(studentBuilder.build());
        }

        if (extension.equals("pdf")) {
            GeneratorMethods.writePDF(students, out);
        } else if (extension.equals("csv")) {
            GeneratorMethods.writeCSV(students, out);
        } else if (extension.equals("excel")) {
            GeneratorMethods.writeXLSX(students, out);
        }
    }


}
