package com.Spring.application.service;

import com.Spring.application.entity.CourseSchedule;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface CourseScheduleService {
    ResponseEntity<String> addCourseSchedule(Long courseId, String day, String startTime, String endTime);
    ResponseEntity<String> updateCourseSchedule(Long courseId, String day, String startTime, String endTime);
    ResponseEntity<String> deleteCourseSchedule(Long courseId);
    ResponseEntity<CourseSchedule> getCourseScheduleById(Long courseId);
    ResponseEntity<List<CourseSchedule>> getAllCourseSchedules();
}
