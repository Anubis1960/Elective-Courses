package com.Spring.application.service.impl;

import com.Spring.application.entity.Course;
import com.Spring.application.entity.Enrollment;
import com.Spring.application.entity.Student;
import com.Spring.application.enums.Status;
import com.Spring.application.exceptions.ObjectNotFound;
import com.Spring.application.repository.CourseRepository;
import com.Spring.application.repository.EnrollmentRepository;
import com.Spring.application.repository.StudentRepository;
import com.Spring.application.service.EnrollmentService;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EnrollmentServiceImpl implements EnrollmentService{

    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Enrollment addEnrollment(Long studentId, Long courseId, Integer priority) throws ObjectNotFound {
        System.out.println("studentId: " + studentId+ " courseId: " + courseId + " priority: " + priority);
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            throw new ObjectNotFound("Student not found");
        }
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            throw new ObjectNotFound("Course not found");
        }
        Enrollment e = enrollmentRepository.findEnrollmentByStudentIdAndCourseId(studentId, courseId);
        if(e != null){
            throw new ObjectNotFound("Course not found");
        }
        Enrollment enrollment = new Enrollment(student, course, priority, Status.valueOf("PENDING"));
        enrollmentRepository.save(enrollment);
        return enrollment;
    }

    @Override
    public Enrollment updateEnrollment(Long enrollmentId, Long studentId, Long courseId, Integer priority, String status) throws ObjectNotFound {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElse(null);
        if (enrollment == null) {
            throw new ObjectNotFound("Enrollment not found");
        }
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            throw new ObjectNotFound("Student not found");
        }
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            throw new ObjectNotFound("Course not found");
        }
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setPriority(priority);
        enrollment.setStatus(Status.valueOf(status));
        enrollmentRepository.save(enrollment);
        return enrollment;
    }

    @Override
    public Enrollment deleteEnrollment(Long enrollmentId) throws ObjectNotFound {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElse(null);
        if (enrollment == null) {
            throw new ObjectNotFound("Enrollment not found");
        }
        enrollmentRepository.deleteById(enrollmentId);
        return enrollment;
    }

    @Override
    public Enrollment getEnrollmentById(Long enrollmentId) throws ObjectNotFound {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElse(null);
        if (enrollment == null) {
            throw new ObjectNotFound("Enrollment not found");
        }
        return enrollment;
    }

    @Override
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    @Override
    public List<Enrollment> getAllEnrollmentsWhereStatusIsAccepted(){
        return enrollmentRepository.findAllWhereStatusIsAccepted();
    }

    @Override
    public Integer countByCourseId(Long courseId){
        return enrollmentRepository.countByCourseId(courseId);
    }

    @Override
    public List<Enrollment> sortEnrollmentsByStudentGradeAsc(){
        return enrollmentRepository.findAllByOrderByStudentGradeAsc();
    }

    @Override
    public Integer countByStudentId(Long studentId){
        return enrollmentRepository.countByStudentId(studentId);
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudentId(Long studentId){
        return enrollmentRepository.findByStudentId(studentId);
    }

    @Override
    public List<Enrollment> assignStudents(){
        List<Enrollment> enrollments = enrollmentRepository.findAll(Sort.by(Sort.Direction.DESC, "student.grade").and(Sort.by(Sort.Direction.ASC, "student.id")).and(Sort.by(Sort.Direction.ASC, "priority")));
//        enrollments.sort((Enrollment enrollment1, Enrollment enrollment2) -> {
//            Student student1 = enrollment1.getStudent();
//            Student student2 = enrollment2.getStudent();
//            if (!student1.getGrade().equals(student2.getGrade())) {
//                return student1.getGrade() < student2.getGrade() ? 1 : -1;
//            } else {
//                if (!student1.getId().equals(student2.getId())) {
//                    return student1.getId() > student2.getId() ? 1 : -1;
//                } else {
//                    return enrollment1.getPriority() > enrollment2.getPriority() ? 1 : -1;
//                }
//            }
//        });

        Long currentStudentId = enrollments.get(0).getStudent().getId();
//        List<Course> courses = courseRepository.findAllCoursesOrderByASC();
        List<Course> courses = courseRepository.findAll();
        List<String> categoriesTaken = new ArrayList<>();
        for (Enrollment enrollment : enrollments) {
            if (!enrollment.getStudent().getId().equals(currentStudentId)) {
                categoriesTaken.clear();
                currentStudentId = enrollment.getStudent().getId();
            }
            Integer low = 0;
            Integer high = courses.size() - 1;
            Integer mid = 0;
            while (low <= high) {
                mid = low + (high - low) / 2;
                if (enrollment.getCourse().getCourseId().equals(courses.get(mid).getCourseId()))
                    break;
                if (enrollment.getCourse().getCourseId() > courses.get(mid).getCourseId())
                    low = mid + 1;
                else
                    high = mid - 1;
            }

            if(courses.get(mid).getMaximumStudentsAllowed() <= 0)
                enrollment.setStatus(Status.valueOf("REJECTED"));
            else {
                if (categoriesTaken.contains(courses.get(mid).getCategory()))
                    enrollment.setStatus(Status.valueOf("REJECTED"));
                else {
                    courses.get(mid).setMaximumStudentsAllowed(courses.get(mid).getMaximumStudentsAllowed() - 1);
                    enrollment.setStatus(Status.valueOf("ACCEPTED"));
                    categoriesTaken.add(courses.get(mid).getCategory());
                }
            }
        }
        enrollmentRepository.saveAll(enrollments);
        return enrollments;
    }
}
