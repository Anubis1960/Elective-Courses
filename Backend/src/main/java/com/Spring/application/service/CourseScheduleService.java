package com.Spring.application.service;

import com.Spring.application.entity.CourseSchedule;
import com.Spring.application.exceptions.InvalidInput;
import com.Spring.application.exceptions.ObjectNotFound;
import org.hibernate.NonUniqueObjectException;

import java.util.List;

public interface CourseScheduleService {
    CourseSchedule addCourseSchedule(Long courseId, String day, String startTime, String endTime) throws ObjectNotFound, InvalidInput, NonUniqueObjectException;
    CourseSchedule updateCourseSchedule(Long courseId, String day, String startTime, String endTime) throws ObjectNotFound, InvalidInput;
    CourseSchedule deleteCourseSchedule(Long courseId) throws ObjectNotFound;
    CourseSchedule getCourseScheduleById(Long courseId) throws ObjectNotFound;
    List<CourseSchedule> getAllCourseSchedules();
}
