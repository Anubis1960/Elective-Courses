package com.Spring.application.service.impl;

import com.Spring.application.entity.Course;
import com.Spring.application.entity.CourseSchedule;
import com.Spring.application.enums.Day;
import com.Spring.application.exceptions.InvalidInput;
import com.Spring.application.exceptions.ObjectNotFound;
import com.Spring.application.repository.CourseRepository;
import com.Spring.application.repository.CourseScheduleRepository;
import com.Spring.application.service.CourseScheduleService;
import org.hibernate.NonUniqueObjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.util.List;

@Service
public class CourseScheduleServiceImpl implements CourseScheduleService{

    @Autowired
    private CourseScheduleRepository courseScheduleRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Override
    public CourseSchedule addCourseSchedule(Long courseId, String day, String startTime, String endTime) throws ObjectNotFound, InvalidInput, NonUniqueObjectException {
        if (LocalTime.parse(startTime).isAfter(LocalTime.parse(endTime))) {
            throw new InvalidInput("Start time cannot be after end time");
        }
        Course course = courseRepository.findById(courseId).orElse(null);
        CourseSchedule courseSchedule = courseScheduleRepository.findById(courseId).orElse(null);
        if (courseSchedule != null) {
            throw new NonUniqueObjectException("Course Schedule already exists", courseSchedule.getCourseId().toString());
        }
        if (course == null) {
            throw new ObjectNotFound("Course not found");
        }
        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);
        courseSchedule = new CourseSchedule(course, Day.valueOf(day), start, end);
        courseScheduleRepository.save(courseSchedule);
        return courseSchedule;
    }

    @Override
    public CourseSchedule updateCourseSchedule(Long courseId, String day, String startTime, String endTime) throws ObjectNotFound, InvalidInput {
        if (LocalTime.parse(startTime).isAfter(LocalTime.parse(endTime))) {
            throw new InvalidInput("Start time cannot be after end time");
        }
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            throw new ObjectNotFound("Course not found");
        }
        CourseSchedule courseSchedule = new CourseSchedule();
        courseSchedule.setCourse(course);
        courseSchedule.setDay(Day.valueOf(day));
        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);
        courseSchedule.setStartTime(start);
        courseSchedule.setEndTime(end);
        courseScheduleRepository.save(courseSchedule);
        return courseSchedule;
    }

    @Override
    public CourseSchedule deleteCourseSchedule(Long courseId) throws ObjectNotFound {
        CourseSchedule courseSchedule = courseScheduleRepository.findById(courseId).orElse(null);
        if (courseSchedule == null) {
            throw new ObjectNotFound("Course Schedule not found");
        }
        courseScheduleRepository.delete(courseSchedule);
        return courseSchedule;
    }

    @Override
    public CourseSchedule getCourseScheduleById(Long courseId) throws ObjectNotFound {

        CourseSchedule courseSchedule = courseScheduleRepository.findById(courseId).orElse(null);
        if (courseSchedule == null) {
            throw new ObjectNotFound("Course Schedule not found");
        }
        return courseSchedule;
    }

    @Override
    public List<CourseSchedule> getAllCourseSchedules() {
        return courseScheduleRepository.findAll();
    }
}
