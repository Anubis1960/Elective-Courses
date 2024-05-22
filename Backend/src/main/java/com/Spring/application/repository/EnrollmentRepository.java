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

    @Query("SELECT e FROM Enrollment e WHERE e.status = 'ACCEPTED'")
    List<Enrollment> findAllWhereStatusIsAccepted();

    @Query("SELECT e FROM Enrollment e ORDER BY e.student.grade DESC")
    List<Enrollment> findAllByOrderByStudentGradeDesc();

    @Query("SELECT count(e) FROM Enrollment e WHERE e.course.courseId = :courseId GROUP BY e.course.courseId")
    Integer countByCourseId(Long courseId);

    @Query("SELECT count(e) FROM Enrollment e WHERE e.student.id = :studentId GROUP BY e.student.id")
    Integer countByStudentId(Long studentId);

    List<Enrollment> findByStudentId(Long studentId);

    @Query("SELECT e.student.id FROM Enrollment e WHERE e.status = 'ACCEPTED' AND e.course.courseId = :courseId")
    List<Long> findAllByCourseIdAndStatusIsAccepted(Long courseId);

    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.course.courseId = :courseId")
    Enrollment findEnrollmentByStudentIdAndCourseId(Long studentId, Long courseId);

}
