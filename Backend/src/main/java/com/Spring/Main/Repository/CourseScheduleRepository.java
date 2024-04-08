package com.Spring.Main.Repository;

import com.Spring.Main.Entity.CourseSchedule;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseScheduleRepository extends JpaRepository<CourseSchedule, Integer> {
}
