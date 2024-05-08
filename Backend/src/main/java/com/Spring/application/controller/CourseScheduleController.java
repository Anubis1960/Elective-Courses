package com.Spring.application.controller;

import com.Spring.application.entity.Course;
import com.Spring.application.entity.CourseSchedule;
import com.Spring.application.exceptions.InvalidInput;
import com.Spring.application.exceptions.ObjectNotFound;
import com.Spring.application.service.impl.CourseScheduleServiceImpl;
import com.Spring.application.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.Spring.application.service.PDFGeneratorService;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/schedule")
public class CourseScheduleController {
    @Autowired
    private CourseScheduleServiceImpl courseScheduleService;
    @Autowired
    private PDFGeneratorService pdfGeneratorService;

    @PostMapping("/")
    @JsonView(Views.Public.class)
    public ResponseEntity<CourseSchedule> addCourseSchedule(@RequestParam Long courseId,
                                                            @RequestParam String day,
                                                            @RequestParam String startTime,
                                                            @RequestParam String endTime) throws ObjectNotFound, InvalidInput {
        CourseSchedule courseSchedule = courseScheduleService.addCourseSchedule(courseId, day, startTime, endTime);
        return new ResponseEntity<>(courseSchedule, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<CourseSchedule> updateCourseSchedule(@PathVariable("id")Long id,
                                                               @RequestParam String day,
                                                               @RequestParam String startTime,
                                                               @RequestParam String endTime) throws ObjectNotFound, InvalidInput {
        CourseSchedule courseSchedule = courseScheduleService.updateCourseSchedule(id, day, startTime, endTime);
        return new ResponseEntity<>(courseSchedule, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<CourseSchedule> deleteCourseSchedule(@PathVariable("id") Long id) throws ObjectNotFound {
        CourseSchedule courseSchedule = courseScheduleService.deleteCourseSchedule(id);
        return new ResponseEntity<>(courseSchedule, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<CourseSchedule> getCourseScheduleById(@PathVariable("id") Long id) throws ObjectNotFound {
        CourseSchedule courseSchedule = courseScheduleService.getCourseScheduleById(id);
        return new ResponseEntity<>(courseSchedule, HttpStatus.OK);
    }

    @GetMapping("/")
    @JsonView(Views.Public.class)
    public ResponseEntity<List<CourseSchedule>> getAllCourseSchedules() {
        List<CourseSchedule> courseSchedules = courseScheduleService.getAllCourseSchedules();
        if (courseSchedules.isEmpty()) {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(courseSchedules, HttpStatus.OK);
    }

    @GetMapping("/export")
    public void exportScheduleToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormater.format(System.currentTimeMillis());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=schedule_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
        this.pdfGeneratorService.exportScheduleToPDF(response);
    }
}
