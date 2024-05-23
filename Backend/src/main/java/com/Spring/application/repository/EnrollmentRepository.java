package com.Spring.application.repository;

import com.Spring.application.entity.Enrollment;
import com.Spring.application.entity.Student;
import com.Spring.application.enums.FacultySection;
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

    @Query("SELECT e FROM Enrollment e WHERE e.status = 'ACCEPTED'")
    List<Enrollment> findAllWhereStatusIsAccepted();

    @Query("SELECT count(e) FROM Enrollment e WHERE e.course.courseId = :courseId GROUP BY e.course.courseId")
    Integer countByCourseId(Long courseId);

    @Query("SELECT count(e) FROM Enrollment e WHERE e.student.id = :studentId GROUP BY e.student.id")
    Integer countByStudentId(Long studentId);

    List<Enrollment> findByStudentId(Long studentId);

    @Query("SELECT e.student.id FROM Enrollment e WHERE e.status = 'ACCEPTED' AND e.course.courseId = :courseId")
    List<Long> findAllByCourseIdAndStatusIsAccepted(Long courseId);

    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.course.courseId = :courseId")
    Enrollment findEnrollmentByStudentIdAndCourseId(Long studentId, Long courseId);

    @Query("SELECT e FROM Enrollment e WHERE e.student.year = :year AND e.status = 'ACCEPTED'")
    List<Enrollment> findEnrollmentByYearAndStatusIsAccepted(Integer year);

    @Query("SELECT e FROM Enrollment e WHERE e.student.facultySection = :facultySection AND e.status = 'ACCEPTED'")
    List<Enrollment> findEnrollmentByFacultySectionAndStatusIsAccepted(FacultySection facultySection);

}
