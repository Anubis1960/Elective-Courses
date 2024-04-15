package com.Spring.Main.Repository;

import com.Spring.Main.Entity.CourseSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseScheduleRepository extends JpaRepository<CourseSchedule, Long> {
}
