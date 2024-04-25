package com.Spring.application.controller;

import com.Spring.application.entity.CourseSchedule;
import com.Spring.application.service.impl.CourseScheduleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedule")
public class CourseScheduleController {
    @Autowired
    private CourseScheduleServiceImpl courseScheduleService;

    @PostMapping("/addschedule")
    public ResponseEntity<String> addCourseSchedule(Long courseId, String day, String startTime, String endTime) {
        return courseScheduleService.addCourseSchedule(courseId, day, startTime, endTime);
    }

    @PutMapping("/updateschedule")
    public ResponseEntity<String> updateCourseSchedule(Long courseId, String day, String startTime, String endTime) {
        return courseScheduleService.updateCourseSchedule(courseId, day, startTime, endTime);
    }

    @DeleteMapping("/deleteschedule")
    public ResponseEntity<String> deleteCourseSchedule(Long id) {
        return courseScheduleService.deleteCourseSchedule(id);
    }

    @GetMapping("/getschedulebyid")
    public ResponseEntity<CourseSchedule> getCourseScheduleById(Long id) {
        return courseScheduleService.getCourseScheduleById(id);
    }

    @GetMapping("/getschedules")
    public ResponseEntity<List<CourseSchedule>> getAllCourseSchedules() {
        return courseScheduleService.getAllCourseSchedules();
    }
}
