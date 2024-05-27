package com.Spring.application.repository;

import com.Spring.application.entity.Course;
import com.Spring.application.enums.FacultySection;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;


public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT c FROM Course c WHERE c.year = ?1 AND c.facultySection = ?2 ")
    List<Course> findAllCoursesByYearAndFacultySection(Integer year, FacultySection facultySection);

    Course findByCourseName(String courseName);

    @Query("SELECT COUNT(DISTINCT c.category) FROM Course c WHERE c.year = ?1 GROUP BY c.year ")
    Integer countDistinctCategoriesByYear(Integer year);

    @Query("SELECT c FROM Course c, Enrollment e WHERE e.student.id = ?1 AND e.course.courseId = c.courseId AND e.status = 'ACCEPTED'")
    List<Course> findAcceptedCoursesByStudentId(Long studentId);

    @Query("SELECT c FROM Course c, Enrollment e WHERE e.student.id = ?1 AND e.course.courseId = c.courseId AND e.status = 'PENDING'")
    List<Course> findPendingCoursesByStudentId(Long studentId);

    @Query("SELECT c FROM Course c WHERE c.maximumStudentsAllowed > (SELECT COUNT(e) FROM Enrollment e WHERE e.course.courseId = c.courseId AND e.status = 'ACCEPTED') AND c.courseId != ?1 AND c.year = ?2 AND c.facultySection = ?3 AND c.category = ?4")
    List<Course> findAvailableCourses(Long courseId, Integer year, FacultySection facultySection, String category);

    @Query("SELECT c.courseId FROM Course c")
    List<Long> findAllCourseIds();

    @Query("SELECT DISTINCT c.facultySection FROM Course c")
    List<String> findFacultySections();
}
