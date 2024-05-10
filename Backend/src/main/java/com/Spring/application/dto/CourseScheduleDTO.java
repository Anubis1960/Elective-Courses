package com.Spring.application.dto;

import com.Spring.application.entity.CourseSchedule;

import java.util.ArrayList;
import java.util.List;

public class CourseScheduleDTO {
    private Long id;
    private String startTime;
    private String endTime;
    private String day;

    public CourseScheduleDTO(Long id, String startTime, String endTime, String day) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
    }

    public CourseScheduleDTO() {
    }

    public static List<CourseScheduleDTO> convertToDTO(List<CourseSchedule> courseSchedules) {
        List<CourseScheduleDTO> courseScheduleDTOS = new ArrayList<>();
        courseSchedules.forEach(courseSchedule -> courseScheduleDTOS.add(new CourseScheduleDTO(courseSchedule.getCourseId(), courseSchedule.getStartTime().toString(), courseSchedule.getEndTime().toString(), courseSchedule.getDay().toString())));
        return courseScheduleDTOS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
