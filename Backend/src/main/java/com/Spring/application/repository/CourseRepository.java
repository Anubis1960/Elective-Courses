package com.Spring.application.repository;

import com.Spring.application.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CourseRepository extends JpaRepository<Course, Long>{
    @Query("SELECT DISTINCT c.category FROM Course c WHERE c.year = ?1")
    List<String> findAllCategoriesByYear(int year);
}
