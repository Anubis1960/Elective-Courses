package com.Spring.application.repository;

import com.Spring.application.entity.CourseSchedule;
import jakarta.persistence.NamedQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseScheduleRepository extends JpaRepository<CourseSchedule, Long> {
}
