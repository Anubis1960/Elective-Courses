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
import com.Spring.application.utils.GeneratorMethods;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import jakarta.transaction.Transactional;
import org.hibernate.NonUniqueObjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        List<String> startTimeList = new ArrayList<>();
        List<String> endTimeList = new ArrayList<>();
        startTimeList.add("08:00");
        startTimeList.add("09:40");
        startTimeList.add("11:20");
        startTimeList.add("13:00");
        startTimeList.add("14:40");
        startTimeList.add("16:20");
        startTimeList.add("18:00");
        startTimeList.add("19:30");
        endTimeList.add("09:30");
        endTimeList.add("11:10");
        endTimeList.add("12:50");
        endTimeList.add("14:30");
        endTimeList.add("16:10");
        endTimeList.add("17:50");
        endTimeList.add("19:30");
        endTimeList.add("21:00");

        if (!startTimeList.contains(startTime) || !endTimeList.contains(endTime)) {
            throw new InvalidInput("Invalid time");
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
        List<String> startTimeList = new ArrayList<>();
        List<String> endTimeList = new ArrayList<>();
        startTimeList.add("08:00");
        startTimeList.add("09:40");
        startTimeList.add("11:20");
        startTimeList.add("13:00");
        startTimeList.add("14:40");
        startTimeList.add("16:20");
        startTimeList.add("18:00");
        startTimeList.add("19:30");
        endTimeList.add("09:30");
        endTimeList.add("11:10");
        endTimeList.add("12:50");
        endTimeList.add("14:30");
        endTimeList.add("16:10");
        endTimeList.add("17:50");
        endTimeList.add("19:30");
        endTimeList.add("21:00");

        if (!startTimeList.contains(startTime) || !endTimeList.contains(endTime)) {
            throw new InvalidInput("Invalid time");
        }

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

    @Override
    public void export(OutputStream out, Long id) throws IOException {
        List<CourseSchedule> courseSchedules = courseScheduleRepository.findCourseScheduleOfStudent(id);

        // Set up document
        Document document = GeneratorMethods.setUpDocument(out);

        // Define fonts
        Font font = FontFactory.getFont(FontFactory.COURIER, 12, Color.BLACK);

        // Create a table with 6 columns (Time slots + 5 days of the week)
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Set column widths
        float[] columnWidths = {2f, 2f, 2f, 2f, 2f, 2f};
        table.setWidths(columnWidths);

        // Create table header
        String[] headers = {"Time Slot", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        for (String header : headers) {
            GeneratorMethods.writeHeadersPDF(header, table);
        }

        // Define time slots
        String[] timeSlots = {"08:00-09:30", "09:40-11:10", "11:20-12:50", "13:00-14:30", "14:40-16:10", "16:20-17:50", "18:00-19:30", "19:40-21:10"};

        // Map to store the courses based on the day and time slot
        Map<String, Map<String, List<CourseSchedule>>> scheduleMap = new HashMap<>();
        for (String day : new String[]{"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"}) {
            scheduleMap.put(day, new HashMap<>());
            for (String timeSlot : timeSlots) {
                scheduleMap.get(day).put(timeSlot, new ArrayList<>());
            }
        }

        // Populate the schedule map
        for (CourseSchedule courseSchedule : courseSchedules) {
            String day = courseSchedule.getDay().toString().toUpperCase();
            String startTime = courseSchedule.getStartTime().toString();
            String endTime = courseSchedule.getEndTime().toString();
            String timeSlot = startTime + "-" + endTime;

            if (scheduleMap.containsKey(day)) {
                scheduleMap.get(day).get(timeSlot).add(courseSchedule);
            }
        }

        // Add rows to the table
        for (String timeSlot : timeSlots) {
            // Add time slot cell
            table.addCell(new PdfPCell(new Phrase(timeSlot, font)));

            // Add course cells for each day
            for (String day : new String[]{"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"}) {
                List<CourseSchedule> courses = scheduleMap.get(day).get(timeSlot);
                PdfPCell cell;
                if (!courses.isEmpty()) {
                    StringBuilder coursesText = new StringBuilder();
                    for (CourseSchedule course : courses) {
                        coursesText.append(course.getCourse().getCourseName()).append("\n");
                    }
                    cell = new PdfPCell(new Phrase(coursesText.toString(), font));
                } else {
                    cell = new PdfPCell(new Phrase("", font));
                }
                table.addCell(cell);
            }
        }

        // Add table to document
        document.add(table);
        // Close document
        document.close();
    }
}
