package com.Spring.application.controller;

import com.Spring.application.dto.StudentDTO;
import com.Spring.application.entity.Student;
import com.Spring.application.exceptions.InvalidInput;
import com.Spring.application.exceptions.ObjectNotFound;
import com.Spring.application.service.ExcelGeneratorService;
import com.Spring.application.service.impl.ExcelGeneratorServiceImpl;
import com.Spring.application.service.impl.StudentServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentServiceImpl studentService;

    @Autowired
    private ExcelGeneratorServiceImpl excelGeneratorService;

    @PostMapping("/")
    public ResponseEntity<StudentDTO> addStudent(
            @RequestParam String name,
            @RequestParam Float grade,
            @RequestParam String facultySection,
            @RequestParam Integer year,
            @RequestParam String email,
            @RequestParam String password) throws NoSuchAlgorithmException, InvalidInput {
        Student student = studentService.addStudent(name, grade, facultySection, year, email, password);
        if (student == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        StudentDTO studentDTO = new StudentDTO(student.getId(), student.getName(), student.getEmail(), student.getRole().toString(), student.getFacultySection().toString(), student.getYear(), student.getGrade());
        return new ResponseEntity<>(studentDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(
            @PathVariable("id") Long userId,
            @RequestParam String name,
            @RequestParam String role, Float grade,
            @RequestParam String facultySection,
            @RequestParam Integer year,
            @RequestParam String email,
            @RequestParam String password) throws InvalidInput, ObjectNotFound, NoSuchAlgorithmException {
        Student student = studentService.updateStudent(userId, name, role, grade, facultySection, year, email, password);
        if (student == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        StudentDTO studentDTO = new StudentDTO(student.getId(), student.getName(), student.getEmail(), student.getRole().toString(), student.getFacultySection().toString(), student.getYear(), student.getGrade());
        return new ResponseEntity<>(studentDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StudentDTO> deleteStudent(@PathVariable("id")  Long id) throws ObjectNotFound {
        Student student = studentService.deleteStudent(id);
        if (student == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        StudentDTO studentDTO = new StudentDTO(student.getId(), student.getName(), student.getEmail(), student.getRole().toString(), student.getFacultySection().toString(), student.getYear(), student.getGrade());
        return new ResponseEntity<>(studentDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable("id")  Long id) throws ObjectNotFound {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        StudentDTO studentDTO = new StudentDTO(student.getId(), student.getName(), student.getEmail(), student.getRole().toString(), student.getFacultySection().toString(), student.getYear(), student.getGrade());
        return new ResponseEntity<>(studentDTO, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
        List<StudentDTO> studentDTOs = StudentDTO.convertToDTO(students);
        return new ResponseEntity<>(studentDTOs, HttpStatus.OK);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<StudentDTO>> getAllStudentsByCourseId(@PathVariable("courseId") Long courseId) {
        List<Student> students = studentService.getAllStudentsByCourseId(courseId);
        if (students.isEmpty()) {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
        List<StudentDTO> studentDTOs = StudentDTO.convertToDTO(students);
        return new ResponseEntity<>(studentDTOs, HttpStatus.OK);
    }

    @GetMapping("/accepted/{courseId}")
    public ResponseEntity<List<StudentDTO>> getAcceptedEnrollmentsByCourseId(@PathVariable("courseId") Long courseId) {
        List<Student> students = studentService.getAcceptedStudentsByCourseId(courseId);
        if (students.isEmpty()) {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
        List<StudentDTO> studentDTOs = StudentDTO.convertToDTO(students);
        return new ResponseEntity<>(studentDTOs, HttpStatus.OK);
    }

    @GetMapping("/export-to-excel")
    public ResponseEntity<List<StudentDTO>> exportExcel(HttpServletResponse response) throws IOException {
        List<Student> students = studentService.getAllStudents();

        if (students.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }

        List<StudentDTO> studentDTOs = StudentDTO.convertToDTO(students);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=students.xlsx");
        excelGeneratorService.exportStudentsToExcel(response, students);

        return new ResponseEntity<>(studentDTOs, HttpStatus.OK);
    }
}
