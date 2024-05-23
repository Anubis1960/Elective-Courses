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

import java.util.*;

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
        if (!student.getYear().equals(course.getYear()) || (student.getFacultySection() != course.getFacultySection())){
            throw new ObjectNotFound("Year of student and course do not match");
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
        Long currentStudentId = enrollments.get(0).getStudent().getId();
        List<Course> courses = courseRepository.findAll();
        List<String> categoriesTaken = new ArrayList<>();

        Map<Integer, Integer> mapYearByNoCategories = new HashMap<>();
        mapYearByNoCategories.put(1, courseRepository.countDistinctCategoriesByYear(1));
        mapYearByNoCategories.put(2, courseRepository.countDistinctCategoriesByYear(2));
        mapYearByNoCategories.put(3, courseRepository.countDistinctCategoriesByYear(3));

        List<Student> unassignedStudents = new ArrayList<>();

        for (Enrollment enrollment : enrollments) {
            if (!enrollment.getStudent().getId().equals(currentStudentId)) {
                if (categoriesTaken.size() < mapYearByNoCategories.get(enrollment.getStudent().getYear())) {
                    unassignedStudents.add(enrollment.getStudent());
                }
                categoriesTaken.clear();
                currentStudentId = enrollment.getStudent().getId();
            }
<<<<<<< HEAD
            int low = 0;
            int high = courses.size() - 1;
            int mid = 0;
            while (low <= high) {
                mid = low + (high - low) / 2;
                if (enrollment.getCourse().getCourseId().equals(courses.get(mid).getCourseId()))
                    break;
                if (enrollment.getCourse().getCourseId() > courses.get(mid).getCourseId())
                    low = mid + 1;
                else
                    high = mid - 1;
            }
=======

            int mid = binarySearchCourse(courses, enrollment.getCourse().getCourseId());
>>>>>>> b4a03d1e3d8fcb932973b75059b546633129daba

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
        unassignedStudents.addAll(studentRepository.findStudentsNotEnrolled());
        List<Course> availableCourses = new ArrayList<>();
        for (Course course : courses) {
            if (course.getMaximumStudentsAllowed() > 0) {
                availableCourses.add(course);
            }
        }

        enrollmentRepository.saveAll(enrollments);
        return enrollments;
    }

    public Integer binarySearchCourse(List<Course> courses, Long courseId){
        int low = 0;
        int high = courses.size() - 1;
        int mid;
        while (low <= high) {
            mid = low + (high - low) / 2;
            if (courseId.equals(courses.get(mid).getCourseId()))
                return mid;
            if (courseId > courses.get(mid).getCourseId())
                low = mid + 1;
            else
                high = mid - 1;
        }
        return -1;
    }

    public List<Enrollment> completeAssignment(List<Student> students, List<Course> courses, Map<Integer, Integer> mapYearByNoCategories){
        List<Enrollment> enrollments = new ArrayList<>();
        for (Student student : students) {
            int numOptionals = mapYearByNoCategories.get(student.getYear());
            for (Course course : courses)
                if (course.getYear().equals(student.getYear()) && course.getFacultySection().equals(student.getFacultySection())) {
                    if (numOptionals == 0)
                        break;
                    if (course.getMaximumStudentsAllowed() > 0) {
                        enrollments.add(new Enrollment(student, course, 1, Status.valueOf("ACCEPTED")));
                        course.setMaximumStudentsAllowed(course.getMaximumStudentsAllowed() - 1);
                        numOptionals--;
                    }   else{
                        courses.remove(course);
                    }

                }
        }
        enrollmentRepository.saveAll(enrollments);
        return enrollments;
    }


}
