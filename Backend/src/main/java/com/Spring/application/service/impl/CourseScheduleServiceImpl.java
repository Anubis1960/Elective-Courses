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
        if (course == null) {
            throw new ObjectNotFound("Course not found");
        }
        CourseSchedule courseSchedule = courseScheduleRepository.findById(courseId).orElse(null);
        if (courseSchedule != null) {
            throw new NonUniqueObjectException("Course Schedule already exists", courseSchedule.getCourseId().toString());
        }

        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);
        List<CourseSchedule> courseSchedules = courseScheduleRepository.findByDayAndStartTimeAndEndTime(day, start, end);
        CourseSchedule newCourseSchedule = new CourseSchedule(course, Day.valueOf(day), start, end);

        if (!courseSchedules.isEmpty()) {
            boolean collide = avoidCollision(newCourseSchedule, courseSchedules);
            if (collide) {
                throw new NonUniqueObjectException("Course Schedule collides with another", newCourseSchedule.getCourseId().toString());
            }
        }

        courseScheduleRepository.save(newCourseSchedule);

        return newCourseSchedule;
    }

    private boolean avoidCollision(CourseSchedule newCourseSchedule, List<CourseSchedule> courseSchedules) throws NonUniqueObjectException {
        boolean collide = false;
        for (CourseSchedule cs : courseSchedules) {
            if (cs.getCourseId().equals(newCourseSchedule.getCourseId())) {
                continue;
            }
            if (cs.getCourse().getFacultySection().equals(newCourseSchedule.getCourse().getFacultySection())) {
                if (cs.getCourse().getYear().equals(newCourseSchedule.getCourse().getYear())) {
                    if (cs.getDay().equals(newCourseSchedule.getDay())) {
                        if (cs.getStartTime().equals(newCourseSchedule.getStartTime()) || cs.getEndTime().equals(newCourseSchedule.getEndTime())) {
                            collide = true;
                        } else if (cs.getStartTime().isBefore(newCourseSchedule.getStartTime()) && cs.getEndTime().isAfter(newCourseSchedule.getStartTime())) {
                            collide = true;
                        } else if (cs.getStartTime().isBefore(newCourseSchedule.getEndTime()) && cs.getEndTime().isAfter(newCourseSchedule.getEndTime())) {
                            collide = true;
                        } else if (cs.getStartTime().isAfter(newCourseSchedule.getStartTime()) && cs.getEndTime().isBefore(newCourseSchedule.getEndTime())) {
                            collide = true;
                        }
                    }
                }
            }
        }
        return collide;
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
        List<CourseSchedule> courseSchedules = courseScheduleRepository.findByDayAndStartTimeAndEndTime(day, start, end);

        if (!courseSchedules.isEmpty()) {
            boolean collide = avoidCollision(courseSchedule, courseSchedules);
            if (collide) {
                throw new NonUniqueObjectException("Course Schedule collides with another", courseSchedule.getCourseId().toString());
            }
        }

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
