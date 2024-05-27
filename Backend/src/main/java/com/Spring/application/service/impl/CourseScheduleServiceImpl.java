package com.Spring.application.service.impl;

import com.Spring.application.entity.Course;
import com.Spring.application.entity.CourseSchedule;
import com.Spring.application.enums.Day;
import com.Spring.application.exceptions.InvalidInput;
import com.Spring.application.exceptions.ObjectNotFound;
import com.Spring.application.repository.CourseRepository;
import com.Spring.application.repository.CourseScheduleRepository;
import com.Spring.application.repository.EnrollmentRepository;
import com.Spring.application.service.CourseScheduleService;
import jakarta.transaction.Transactional;
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
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    @Transactional
    public void addCourseSchedule(Long courseId, String day, String startTime, String endTime) throws ObjectNotFound, InvalidInput, NonUniqueObjectException {
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
        List<CourseSchedule> courseSchedules = courseScheduleRepository.findCourseScheduleByDay(Day.valueOf(day));

        for (CourseSchedule cs : courseSchedules) {
            System.out.println(cs);
        }
        CourseSchedule newCourseSchedule = new CourseSchedule(course, Day.valueOf(day), start, end);

        if (!courseSchedules.isEmpty()) {
            boolean collide = avoidCollision(newCourseSchedule, courseSchedules);
            if (collide) {
                throw new NonUniqueObjectException("Course Schedule collides with another", newCourseSchedule.getCourse().getCourseId().toString());
            }
        }
        courseScheduleRepository.save(newCourseSchedule);
    }

    private boolean avoidCollision(CourseSchedule newCourseSchedule, List<CourseSchedule> courseSchedules) throws NonUniqueObjectException {
        boolean collide = false;
        for (CourseSchedule cs : courseSchedules) {
            if (cs.getCourseId().equals(newCourseSchedule.getCourse().getCourseId())) {
                continue;
            }
            if (cs.getStartTime().equals(newCourseSchedule.getStartTime()) || cs.getEndTime().equals(newCourseSchedule.getEndTime())) {
                collide = checkCondition(cs, newCourseSchedule);
                if (collide) {
                    break;
                }
            } else if (cs.getStartTime().isBefore(newCourseSchedule.getStartTime()) && cs.getEndTime().isAfter(newCourseSchedule.getStartTime())) {
                collide = checkCondition(cs, newCourseSchedule);
                if (collide) {
                    break;
                }
            } else if (cs.getStartTime().isBefore(newCourseSchedule.getEndTime()) && cs.getEndTime().isAfter(newCourseSchedule.getEndTime())) {
                collide = checkCondition(cs, newCourseSchedule);
                if (collide) {
                    break;
                }
            } else if (cs.getStartTime().isAfter(newCourseSchedule.getStartTime()) && cs.getEndTime().isBefore(newCourseSchedule.getEndTime())) {
                collide = checkCondition(cs, newCourseSchedule);
                if (collide) {
                    break;
                }
            }
        }
        return collide;
    }

    public boolean checkCondition(CourseSchedule cs, CourseSchedule newCourseSchedule) {
        if(cs.getCourse().getTeacherName().equals(newCourseSchedule.getCourse().getTeacherName())){
            return true;
        }
        else if(cs.getCourse().getYear().equals(newCourseSchedule.getCourse().getYear()) && cs.getCourse().getFacultySection().equals(newCourseSchedule.getCourse().getFacultySection())){
            return checkBothCourses(cs, newCourseSchedule);
        }
        return false;
    }

    public boolean checkBothCourses(CourseSchedule cs, CourseSchedule newCourseSchedule) {
        List<Long> studentIds1 = enrollmentRepository.findAllByCourseIdAndStatusIsAccepted(cs.getCourseId());
        List<Long> studentIds2 = enrollmentRepository.findAllByCourseIdAndStatusIsAccepted(newCourseSchedule.getCourse().getCourseId());
        for (Long id1 : studentIds1) {
            for (Long id2 : studentIds2) {
                if (id1.equals(id2)) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    @Transactional
    public void updateCourseSchedule(Long courseId, String day, String startTime, String endTime) throws ObjectNotFound, InvalidInput {
        if (LocalTime.parse(startTime).isAfter(LocalTime.parse(endTime))) {
            throw new InvalidInput("Start time cannot be after end time");
        }
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            throw new ObjectNotFound("Course not found");
        }
        CourseSchedule courseSchedule = courseScheduleRepository.findById(courseId).orElse(null);
        if (courseSchedule == null) {
            throw new ObjectNotFound("Course Schedule not found");
        }

        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);
        List<CourseSchedule> courseSchedules = courseScheduleRepository.findCourseScheduleByDay(Day.valueOf(day));
        courseSchedule.setDay(Day.valueOf(day));
        courseSchedule.setStartTime(start);
        courseSchedule.setEndTime(end);

        if (!courseSchedules.isEmpty()) {
            boolean collide = avoidCollision(courseSchedule, courseSchedules);
            if (collide) {
                throw new NonUniqueObjectException("Course Schedule collides with another", courseSchedule.getCourseId().toString());
            }
        }

        courseScheduleRepository.save(courseSchedule);
    }

    @Override
    public CourseSchedule deleteCourseSchedule(Long courseId) throws ObjectNotFound {
        CourseSchedule courseSchedule = courseScheduleRepository.findById(courseId).orElse(null);
        if (courseSchedule == null) {
            return null;
        }
        courseScheduleRepository.delete(courseSchedule);
        return courseSchedule;
    }

    @Override
    public CourseSchedule getCourseScheduleById(Long courseId) throws ObjectNotFound {

        return courseScheduleRepository.findById(courseId).orElse(null);
    }

    @Override
    public List<CourseSchedule> getAllCourseSchedules() {
        return courseScheduleRepository.findAll();
    }
}
