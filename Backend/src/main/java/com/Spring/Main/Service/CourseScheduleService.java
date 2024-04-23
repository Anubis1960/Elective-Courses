package com.Spring.Main.service;

import com.Spring.Main.entity.CourseSchedule;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface CourseScheduleService {
    ResponseEntity<String> addCourseSchedule(Long courseId, String day, String startTime, String endTime);
    ResponseEntity<String> updateCourseSchedule(Long courseId, String day, String startTime, String endTime);
    ResponseEntity<String> deleteCourseSchedule(Long courseId);
    ResponseEntity<CourseSchedule> getCourseScheduleById(Long courseId);
    ResponseEntity<List<CourseSchedule>> getAllCourseSchedules();
}
