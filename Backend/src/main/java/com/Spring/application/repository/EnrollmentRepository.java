package com.Spring.application.repository;

import com.Spring.application.entity.Enrollment;
import com.Spring.application.entity.Student;
import com.Spring.application.enums.FacultySection;
import com.Spring.application.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

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
    Optional<Integer> countByCourseId(Long courseId);

    @Query("SELECT count(e) FROM Enrollment e WHERE e.course.courseId = :courseId AND e.status = 'ACCEPTED' GROUP BY e.course.courseId")
    Optional<Integer> countAcceptedByCourseId(Long courseId);

    @Query("SELECT count(e) FROM Enrollment e WHERE e.student.id = :studentId GROUP BY e.student.id")
    Optional<Integer> countByStudentId(Long studentId);

    List<Enrollment> findByStudentId(Long studentId);

    @Query("SELECT e.student.id FROM Enrollment e WHERE e.status = 'ACCEPTED' AND e.course.courseId = :courseId")
    List<Long> findAllByCourseIdAndStatusIsAccepted(Long courseId);

    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.course.courseId = :courseId")
    Enrollment findEnrollmentByStudentIdAndCourseId(Long studentId, Long courseId);

    @Query("SELECT e FROM Enrollment e WHERE e.student.year = :year AND e.status = 'ACCEPTED'")
    List<Enrollment> findEnrollmentByYearAndStatusIsAccepted(Integer year);

    @Query("SELECT e FROM Enrollment e WHERE e.student.facultySection = :facultySection AND e.status = 'ACCEPTED'")
    List<Enrollment> findEnrollmentByFacultySectionAndStatusIsAccepted(FacultySection facultySection);

    @Query("SELECT e FROM Enrollment e WHERE e.student.facultySection = :facultySection AND e.student.year = :year AND e.status = 'ACCEPTED'")
    List<Enrollment> findEnrollmentByFacultySectionAndYearAndStatusIsAccepted(FacultySection facultySection, Integer year);

    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.status = 'ACCEPTED' AND e.course.courseId = :courseId")
    Enrollment findEnrollmentByStudentIdAndCourseIdAndStatusIsAccepted(Long studentId, Long courseId);

    @Query("SELECT DISTINCT e.student FROM Enrollment e " +
            "WHERE e.course.courseId = :courseId AND e.status = 'ACCEPTED'")
    List<Student> getCourseAcceptedStudents(@Param("courseId") Long courseId);

    @Query("SELECT count(distinct e.student) FROM Enrollment e " +
            "WHERE e.course.courseId = :courseId AND e.status = 'ACCEPTED'")
    int getCourseAcceptedStudentsNumber(@Param("courseId") Long courseId);

    @Query("SELECT AVG(e.student.grade) FROM Enrollment e " +
            "WHERE e.course.courseId = :courseId AND e.status = 'ACCEPTED'")
    Double gradeAvg(@Param("courseId") Long courseId);
}
