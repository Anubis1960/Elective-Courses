package com.Spring.application.repository;

import com.Spring.application.entity.CourseSchedule;
import com.Spring.application.enums.Day;
import com.Spring.application.enums.FacultySection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalTime;
import java.util.List;

public interface CourseScheduleRepository extends JpaRepository<CourseSchedule, Long> {
    @Query("SELECT cs FROM Enrollment e, CourseSchedule cs WHERE e.student.id = ?1 and e.status='ACCEPTED' and e.course.courseId = cs.courseId")
    List<CourseSchedule> findCourseScheduleOfStudent(Long id);

    @Query("SELECT cs FROM CourseSchedule cs, Course c WHERE cs.day = :day AND c.year = :year AND c.facultySection = :section AND c.courseId = cs.courseId")
    List<CourseSchedule> findCourseScheduleByDayAndYearAndSection(Day day, Integer year, FacultySection section);

}
