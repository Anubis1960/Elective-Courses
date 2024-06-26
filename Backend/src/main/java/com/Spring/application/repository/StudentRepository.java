package com.Spring.application.repository;

import com.Spring.application.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long>{
    Student findByEmailAndPassword(String email, String password);

    @Query("SELECT s FROM Student s, Enrollment e WHERE s.id = e.student.id AND e.course.courseId = ?1")

    List<Student> findAllStudentsByCourseId(Long courseId);

    Student findByName(String name);

    @Query("SELECT s FROM Student s WHERE s.id NOT IN (SELECT e.student.id FROM Enrollment e )")
    List<Student> findStudentsNotEnrolled();

    @Query("SELECT s FROM Student s WHERE s.id IN (SELECT e.student.id FROM Enrollment e WHERE e.course.courseId = ?1 AND e.status = 'ACCEPTED')")
    List<Student> findAcceptedStudentsByCourseId(Long courseId);
}
