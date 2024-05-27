package com.Spring.application.controller;

import com.Spring.application.dto.CourseScheduleDTO;
import com.Spring.application.entity.CourseSchedule;
import com.Spring.application.exceptions.InvalidInput;
import com.Spring.application.exceptions.ObjectNotFound;
import com.Spring.application.service.impl.CourseScheduleServiceImpl;
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

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/course-schedule")
public class CourseScheduleController {
    @Autowired
    private CourseScheduleServiceImpl courseScheduleService;
    @Autowired
    private PDFGeneratorService pdfGeneratorService;

    @PostMapping("/")
    public ResponseEntity<CourseScheduleDTO> addCourseSchedule(@RequestBody CourseScheduleDTO courseScheduleDTO) throws ObjectNotFound, InvalidInput {
        courseScheduleService.addCourseSchedule(courseScheduleDTO.getId(), courseScheduleDTO.getDay(), courseScheduleDTO.getStartTime(), courseScheduleDTO.getEndTime());
        return new ResponseEntity<>(courseScheduleDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseScheduleDTO> updateCourseSchedule(@PathVariable("id")Long id, @RequestBody CourseScheduleDTO courseScheduleDTO) throws ObjectNotFound, InvalidInput {
        courseScheduleService.updateCourseSchedule(id, courseScheduleDTO.getDay(), courseScheduleDTO.getStartTime(), courseScheduleDTO.getEndTime());
        return new ResponseEntity<>(courseScheduleDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CourseScheduleDTO> deleteCourseSchedule(@PathVariable("id") Long id) throws ObjectNotFound {
        CourseSchedule courseSchedule = courseScheduleService.deleteCourseSchedule(id);
        if (courseSchedule == null) {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
        CourseScheduleDTO courseScheduleDTO = new CourseScheduleDTO(courseSchedule.getCourseId(), courseSchedule.getDay().toString(), courseSchedule.getStartTime().toString(), courseSchedule.getEndTime().toString());
        return new ResponseEntity<>(courseScheduleDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseScheduleDTO> getCourseScheduleById(@PathVariable("id") Long id) throws ObjectNotFound {
        CourseSchedule courseSchedule = courseScheduleService.getCourseScheduleById(id);
        if (courseSchedule == null) {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
        CourseScheduleDTO courseScheduleDTO = new CourseScheduleDTO(courseSchedule.getCourseId(), courseSchedule.getDay().toString(), courseSchedule.getStartTime().toString(), courseSchedule.getEndTime().toString());
        return new ResponseEntity<>(courseScheduleDTO, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<CourseScheduleDTO>> getAllCourseSchedules() {
        List<CourseSchedule> courseSchedules = courseScheduleService.getAllCourseSchedules();
        if (courseSchedules.isEmpty()) {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
        List<CourseScheduleDTO> courseScheduleDTOs = CourseScheduleDTO.convertToDTO(courseSchedules);
        return new ResponseEntity<>(courseScheduleDTOs, HttpStatus.OK);
    }

    @GetMapping("/export")
    public void exportScheduleToPDF(HttpServletResponse response,@RequestParam Long id) throws IOException {
        System.out.println("Exporting schedule to PDF");
        response.setContentType("application/pdf");
        DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormater.format(System.currentTimeMillis());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=schedule_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
        pdfGeneratorService.exportScheduleToPDF(response.getOutputStream(), id);
    }
}
