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
    public void updateEnrollment(Long enrollmentId, Long studentId, Long courseId, Integer priority, String status) throws ObjectNotFound {
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
    public Integer countByCourseId(Long courseId){
        return enrollmentRepository.countByCourseId(courseId);
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

        Map<Student, List<String>> unassignedStudents = new HashMap<>();

        for (Enrollment enrollment : enrollments) {
            if (!enrollment.getStudent().getId().equals(currentStudentId)) {
                if (categoriesTaken.size() < mapYearByNoCategories.get(enrollment.getStudent().getYear())) {
                    unassignedStudents.put(studentRepository.findById(currentStudentId).orElse(null), categoriesTaken);
                }
                categoriesTaken.clear();
                currentStudentId = enrollment.getStudent().getId();
            }

            int mid = binarySearchCourse(courses, enrollment.getCourse().getCourseId());

            if(courses.get(mid).getMaximumStudentsAllowed() == 0)
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
        studentRepository.findStudentsNotEnrolled().forEach(student -> unassignedStudents.put(student, new ArrayList<>()));
        courses.removeIf(course -> course.getMaximumStudentsAllowed() == 0);
        enrollmentRepository.saveAll(enrollments);
        completeAssignment(unassignedStudents, courses, mapYearByNoCategories);

        return enrollmentRepository.findAll();
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

    public void completeAssignment(Map<Student, List<String>> students, List<Course> courses, Map<Integer, Integer> mapYearByNoCategories){
        for (Map.Entry<Student, List<String>> entry : students.entrySet()) {
            List<String> categoriesTaken = entry.getValue();
            for (Course course : courses) {
                if (course.getYear().equals(entry.getKey().getYear()) && course.getFacultySection().equals(entry.getKey().getFacultySection()) && !categoriesTaken.contains(course.getCategory())) {
                    Enrollment enrollment = new Enrollment(entry.getKey(), course, 0, Status.valueOf("ACCEPTED"));
                    enrollmentRepository.save(enrollment);
                    categoriesTaken.add(course.getCategory());
                    if (categoriesTaken.size() == mapYearByNoCategories.get(entry.getKey().getYear())) {
                        break;
                    }
                }
            }
        }
    }
}
