package com.Spring.application.repository;

import com.Spring.application.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CourseRepository extends JpaRepository<Course, Long>{
    @Query("SELECT DISTINCT c.category FROM Course c WHERE c.year = ?1")
    List<String> findAllCategoriesByYear(int year);
    @Query("SELECT c FROM Course c WHERE c.category = ?1 and c.year = ?2")
    List<Course> findAllCoursesByCategoryAndYear(String category, int year);
    @Query("SELECT c FROM Course c WHERE c.year = ?1")
    List<Course> findAllCoursesByYear(int year);
    @Query("SELECT c FROM Course c ORDER BY c.maximumStudentsAllowed DESC")
    List<Course> findAllCoursesOrderByMaximumStudentsAllowedDesc();
}
