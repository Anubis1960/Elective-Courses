package com.Spring.application.service.impl;

import com.Spring.application.entity.Course;
import com.Spring.application.entity.Student;
import com.Spring.application.repository.CourseRepository;
import com.Spring.application.repository.StudentRepository;
import com.Spring.application.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;

import java.util.List;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @Override
    public void sendAllAssignedCoursesMail() {
        List<Student> students = studentRepository.findAll();
        for (Student student : students) {
            String subject = "Assigned Courses";
            StringBuilder text = new StringBuilder("You have been assigned the following courses: ");
            List<Course> courses = courseRepository.findAcceptedCoursesByStudentId(student.getId());
            for (Course course : courses) {
                text.append(course.getCourseName()).append(", ");
            }
            sendMail(student.getEmail(), subject, text.toString());
        }
    }

    @Override
    public void sendReassignment(Long studentId, Long courseId, Long newCourseId){
        Student student = studentRepository.findById(studentId).orElse(null);
        Course course = courseRepository.findById(courseId).orElse(null);
        Course newCourse = courseRepository.findById(newCourseId).orElse(null);
        System.out.println("Student: " + student + " Course: " + course + " New Course: " + newCourse);
        if (student != null && course != null && newCourse != null) {
            String subject = "Course Reassignment";
            String text = "You have been reassigned from " + course.getCourseName() + " to " + newCourse.getCourseName();
            sendMail(student.getEmail(), subject, text);
        }
    }
}
