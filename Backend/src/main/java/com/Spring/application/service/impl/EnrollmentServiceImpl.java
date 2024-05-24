package com.Spring.application.service.impl;

import com.Spring.application.entity.Course;
import com.Spring.application.entity.Enrollment;
import com.Spring.application.entity.Student;
import com.Spring.application.enums.FacultySection;
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
        List<String> categoriesTaken = new ArrayList<>();
        List<Long> courseIds = courseRepository.findAllCourseIds();
        Map<Long, Integer> mapCourseIdByMaxStudents = new HashMap<>();
        for(Long courseId : courseIds){
            mapCourseIdByMaxStudents.put(courseId, courseRepository.findById(courseId).get().getMaximumStudentsAllowed());
        }

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

            if(mapCourseIdByMaxStudents.get(enrollment.getCourse().getCourseId()) == 0)
                enrollment.setStatus(Status.valueOf("REJECTED"));
            else {
                if (categoriesTaken.contains(enrollment.getCourse().getCategory())) {
                    enrollment.setStatus(Status.valueOf("REJECTED"));
                }
                else {
                    mapCourseIdByMaxStudents.put(enrollment.getCourse().getCourseId(), mapCourseIdByMaxStudents.get(enrollment.getCourse().getCourseId()) - 1);
                    enrollment.setStatus(Status.valueOf("ACCEPTED"));
                    categoriesTaken.add(enrollment.getCourse().getCategory());
                }
            }
        }
        studentRepository.findStudentsNotEnrolled().forEach(student -> unassignedStudents.put(student, new ArrayList<>()));
        completeAssignment(unassignedStudents, mapCourseIdByMaxStudents, mapYearByNoCategories);

        return enrollmentRepository.findAll();
    }

    public void completeAssignment(Map<Student, List<String>> students,Map<Long, Integer> mapCourseIdByMaxStudents, Map<Integer, Integer> mapYearByNoCategories){
        for (Map.Entry<Student, List<String>> entry : students.entrySet()) {
            List<String> categoriesTaken = entry.getValue();
            for (Map.Entry<Long, Integer> entry1 : mapCourseIdByMaxStudents.entrySet()) {
                if (entry1.getValue() == 0) {
                    mapCourseIdByMaxStudents.remove(entry1.getKey());
                    continue;
                }
                Course course = courseRepository.findById(entry1.getKey()).orElse(null);
                if (course == null)
                    continue;
                if (categoriesTaken.size() < mapYearByNoCategories.get(entry.getKey().getYear())) {
                    if (!categoriesTaken.contains(course.getCategory())) {
                        mapCourseIdByMaxStudents.put(course.getCourseId(), mapCourseIdByMaxStudents.get(course.getCourseId()) - 1);
                        Enrollment enrollment = new Enrollment(entry.getKey(), course, 0, Status.valueOf("ACCEPTED"));
                        enrollmentRepository.save(enrollment);
                        categoriesTaken.add(course.getCategory());
                    }
                }
            }
        }
    }

    @Override
    public List<Enrollment> getEnrollmentsByYearAndStatusIsAccepted(Integer year){
        return enrollmentRepository.findEnrollmentByYearAndStatusIsAccepted(year);
    }

    @Override
    public List<Enrollment> getEnrollmentsByFacultySectionAndStatusIsAccepted(String facultySection){
        return enrollmentRepository.findEnrollmentByFacultySectionAndStatusIsAccepted(FacultySection.valueOf(facultySection));
    }
}
