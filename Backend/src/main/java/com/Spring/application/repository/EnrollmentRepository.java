package com.Spring.application.repository;

import com.Spring.application.entity.Enrollment;
import jakarta.persistence.NamedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    @Modifying
    @Query("DELETE FROM Enrollment e WHERE e.student.id = :studentId")
    void deleteByStudentId(Long studentId);

    @Modifying
    @Query("DELETE FROM Enrollment e WHERE e.course.courseId = :courseId")
    void deleteByCourseId(Long courseId);

    @Query("SELECT e FROM Enrollment e JOIN e.student s ORDER BY s.grade ASC")
    List<Enrollment> findAllByOrderByStudentGradeAsc();

}