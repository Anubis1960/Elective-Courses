package com.Spring.application.controller;

import com.Spring.application.entity.Course;
import com.Spring.application.entity.CourseSchedule;
import com.Spring.application.exceptions.InvalidInput;
import com.Spring.application.exceptions.ObjectNotFound;
import com.Spring.application.service.impl.CourseScheduleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedule")
public class CourseScheduleController {
    @Autowired
    private CourseScheduleServiceImpl courseScheduleService;

    @PostMapping("/")
    public ResponseEntity<CourseSchedule> addCourseSchedule(Long courseId, String day, String startTime, String endTime) throws ObjectNotFound, InvalidInput {
        CourseSchedule courseSchedule = courseScheduleService.addCourseSchedule(courseId, day, startTime, endTime);
        return new ResponseEntity<>(courseSchedule, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseSchedule> updateCourseSchedule(@PathVariable("id")Long id, String day, String startTime, String endTime) throws ObjectNotFound, InvalidInput {
        CourseSchedule courseSchedule = courseScheduleService.updateCourseSchedule(id, day, startTime, endTime);
        return new ResponseEntity<>(courseSchedule, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CourseSchedule> deleteCourseSchedule(@PathVariable("id") Long id) throws ObjectNotFound {
        CourseSchedule courseSchedule = courseScheduleService.deleteCourseSchedule(id);
        return new ResponseEntity<>(courseSchedule, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseSchedule> getCourseScheduleById(@PathVariable("id") Long id) throws ObjectNotFound {
        CourseSchedule courseSchedule = courseScheduleService.getCourseScheduleById(id);
        return new ResponseEntity<>(courseSchedule, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<CourseSchedule>> getAllCourseSchedules() {
        List<CourseSchedule> courseSchedules = courseScheduleService.getAllCourseSchedules();
        if (courseSchedules.isEmpty()) {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(courseSchedules, HttpStatus.OK);
    }
}
