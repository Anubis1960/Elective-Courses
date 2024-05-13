package com.Spring.application.repository;

import com.Spring.application.entity.Course;
import com.Spring.application.enums.FacultySection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CourseRepository extends JpaRepository<Course, Long>{
    @Query("SELECT DISTINCT c.category FROM Course c WHERE c.year = ?1")
    List<String> findAllCategoriesByYear(Integer year);

    @Query("SELECT c FROM Course c WHERE c.category = ?1 and c.year = ?2")
    List<Course> findAllCoursesByCategoryAndYear(String category, int year);

    @Query("SELECT c FROM Course c WHERE c.year = ?1 AND c.facultySection = ?2 " )
    List<Course> findAllCoursesByYearAndFacultySection(Integer year, FacultySection facultySection);

    @Query("SELECT c FROM Course c ORDER BY ?1")
    List<Course> findAllCoursesOrderByASC(String field, String order);

    @Query("SELECT c FROM Course c ORDER BY ?1 DESC")
    List<Course> findAllCoursesOrderByDESC(String field, String order);
}
