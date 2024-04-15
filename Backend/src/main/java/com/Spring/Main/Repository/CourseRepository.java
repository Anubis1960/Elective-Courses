package com.Spring.Main.Repository;

import com.Spring.Main.Entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long>{
}
