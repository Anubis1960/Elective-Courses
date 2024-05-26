package com.Spring.application.dto;

import com.Spring.application.entity.CourseSchedule;

import java.util.ArrayList;
import java.util.List;

public class CourseScheduleDTO {
    private Long id;
    private String day;
    private String startTime;
    private String endTime;

    public CourseScheduleDTO(Long id, String day, String startTime, String endTime) {
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

    @Override
    public String toString() {
        return "CourseScheduleDTO{" +
                "id=" + id +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", day='" + day + '\'' +
                '}';
    }
}
