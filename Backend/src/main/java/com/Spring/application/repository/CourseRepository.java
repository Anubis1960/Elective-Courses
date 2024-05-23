package com.Spring.application.repository;

import com.Spring.application.entity.Course;
import com.Spring.application.enums.FacultySection;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;


public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT DISTINCT c.category FROM Course c WHERE c.year = ?1")
    List<String> findAllCategoriesByYear(Integer year);

    @Query("SELECT c FROM Course c WHERE c.category = ?1 and c.year = ?2")
    List<Course> findAllCoursesByCategoryAndYear(String category, int year);

    @Query("SELECT c FROM Course c WHERE c.year = ?1 AND c.facultySection = ?2 ")
    List<Course> findAllCoursesByYearAndFacultySection(Integer year, FacultySection facultySection);

    @Query("SELECT c FROM Course c ORDER BY ?1")
    List<Course> findAllCoursesOrderByASC(String field, String order);

    @Query("SELECT c FROM Course c ORDER BY ?1 DESC")
    List<Course> findAllCoursesOrderByDESC(String field, String order);

    Course findByCourseName(String courseName);

    @Query("SELECT COUNT(DISTINCT c.category) FROM Course c WHERE c.year = ?1 GROUP BY c.year ")
    Integer countDistinctCategoriesByYear(Integer year);

    @Query("SELECT c FROM Course c, Enrollment e WHERE e.student.id = ?1 AND e.course.courseId = c.courseId AND e.status = 'ACCEPTED'")
    List<Course> findAcceptedCoursesByStudentId(Long studentId);

}
