package com.Spring.Main.Service.Impl;

import com.Spring.Main.Entity.Course;
import com.Spring.Main.Entity.CourseSchedule;
import com.Spring.Main.Enums.Day;
import com.Spring.Main.Repository.CourseRepository;
import com.Spring.Main.Repository.CourseScheduleRepository;
import com.Spring.Main.Service.CourseScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class CourseScheduleServiceImpl implements CourseScheduleService{

    @Autowired
    private CourseScheduleRepository courseScheduleRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Override
    public ResponseEntity<String> addCourseSchedule(Long courseId, String day, String startTime, String endTime) {
        try{
            Course course = courseRepository.findById(courseId).orElse(null);
            if (course == null) {
                return ResponseEntity.badRequest().body("Course not found");
            }
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);
            CourseSchedule courseSchedule = new CourseSchedule(course, Day.valueOf(day), start, end);
            courseScheduleRepository.save(courseSchedule);
            return ResponseEntity.ok("Course schedule added successfully");
        } catch (DateTimeParseException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid time format");
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid day");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Error adding course schedule");
    }

    @Override
    public ResponseEntity<String> updateCourseSchedule(Long courseId, String day, String startTime, String endTime) {
        try{
            Course course = courseRepository.findById(courseId).orElse(null);
            if (course == null) {
                return ResponseEntity.badRequest().body("Course not found");
            }
            CourseSchedule courseSchedule = new CourseSchedule();
            courseSchedule.setCourse(course);
            courseSchedule.setDay(Day.valueOf(day));
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);
            courseSchedule.setStartTime(start);
            courseSchedule.setEndTime(end);

            courseScheduleRepository.save(courseSchedule);
            return ResponseEntity.ok("Course schedule updated successfully");
        }
        catch (DateTimeParseException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid time format");
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid day");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Error updating course schedule");
    }

    @Override
    public ResponseEntity<String> deleteCourseSchedule(Long courseId) {
        try{
            courseScheduleRepository.deleteById(courseId);
            return ResponseEntity.ok("Course schedule deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Error deleting course schedule");
    }

    @Override
    public ResponseEntity<CourseSchedule> getCourseScheduleById(Long courseId) {
        try{
            CourseSchedule courseSchedule = courseScheduleRepository.findById(courseId).orElse(null);
            if (courseSchedule == null) {
                return ResponseEntity.badRequest().body(null);
            }
            return ResponseEntity.ok(courseSchedule);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body(null);
    }

    @Override
    public ResponseEntity<List<CourseSchedule>> getAllCourseSchedules() {
        try{
            List<CourseSchedule> courseSchedules = courseScheduleRepository.findAll();
            return ResponseEntity.ok(courseSchedules);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body(null);
    }
}
