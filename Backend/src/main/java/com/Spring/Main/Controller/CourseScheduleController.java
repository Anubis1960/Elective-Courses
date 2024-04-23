package com.Spring.Main.controller;

import com.Spring.Main.entity.CourseSchedule;
import com.Spring.Main.service.impl.CourseScheduleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.Spring.Main.service.CourseScheduleService;

import java.util.List;

@RestController("/schedule")
public class CourseScheduleController {
    @Autowired
    private CourseScheduleServiceImpl courseScheduleService;

    @PostMapping("/addschedule")
    public ResponseEntity<String> addCourseSchedule(Long courseId, String day, String startTime, String endTime) {
        return courseScheduleService.addCourseSchedule(courseId, day, startTime, endTime);
    }

    @PostMapping("/updateschedule")
    public ResponseEntity<String> updateCourseSchedule(Long courseId, String day, String startTime, String endTime) {
        return courseScheduleService.updateCourseSchedule(courseId, day, startTime, endTime);
    }

    @PostMapping("/deleteschedule")
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
