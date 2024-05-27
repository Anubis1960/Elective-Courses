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
    @Autowired
    private StudentServiceImpl studentServiceImpl;

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
//        System.out.println("courseId: " + courseId);
//        System.out.println("count: " + enrollmentRepository.countByCourseId(courseId).orElse(0));
        return enrollmentRepository.countByCourseId(courseId).orElse(0);
    }

    @Override
    public Integer countByStudentId(Long studentId){
//        System.out.println("studentId: " + studentId);
//        System.out.println("count: " + enrollmentRepository.countByStudentId(studentId).orElse(0));
        return enrollmentRepository.countByStudentId(studentId).orElse(0);
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudentId(Long studentId){
        return enrollmentRepository.findByStudentId(studentId);
    }

    @Override
    public List<Enrollment> assignStudents() throws ObjectNotFound {
        List<Enrollment> enrollments = enrollmentRepository.findAll(Sort.by(Sort.Direction.DESC, "student.grade").and(Sort.by(Sort.Direction.ASC, "student.id")).and(Sort.by(Sort.Direction.ASC, "priority")));
        List<Long> courseIds = courseRepository.findAllCourseIds();
        Map<Long, Integer> mapCourseIdByMaxStudents = new HashMap<>();
        for(Long courseId : courseIds){
            mapCourseIdByMaxStudents.put(courseId, Objects.requireNonNull(courseRepository.findById(courseId).orElse(null)).getMaximumStudentsAllowed());
        }
        Map<Integer, Integer> mapYearByNoCategories = new HashMap<>();
        mapYearByNoCategories.put(1, courseRepository.countDistinctCategoriesByYear(1));
        mapYearByNoCategories.put(2, courseRepository.countDistinctCategoriesByYear(2));
        mapYearByNoCategories.put(3, courseRepository.countDistinctCategoriesByYear(3));

        for (Map.Entry<Integer, Integer> entry : mapYearByNoCategories.entrySet()) {
            System.out.println("Year: " + entry.getKey() + " No of Categories: " + entry.getValue());
        }

        if(enrollments.isEmpty()){
            Map<Student, List<String>> unassignedStudents = new HashMap<>();
            studentRepository.findStudentsNotEnrolled().forEach(student -> unassignedStudents.put(student, new ArrayList<>()));
            completeAssignment(unassignedStudents, mapCourseIdByMaxStudents, mapYearByNoCategories);
            return enrollmentRepository.findAll();
        }
        Long currentStudentId = enrollments.get(0).getStudent().getId();
        List<String> categoriesTaken = new ArrayList<>();

        Map<Student, List<String>> unassignedStudents = new HashMap<>();

        for (Enrollment enrollment : enrollments) {
            if (!enrollment.getStudent().getId().equals(currentStudentId)) {
                if (categoriesTaken.size() < mapYearByNoCategories.get(enrollment.getStudent().getYear())) {
                    System.out.println("Student: " + studentRepository.findById(currentStudentId).orElse(null) + " Categories Taken: " + categoriesTaken);
                    unassignedStudents.put(studentRepository.findById(currentStudentId).orElse(null), new ArrayList<>(categoriesTaken));
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

    public void completeAssignment(Map<Student, List<String>> students,Map<Long, Integer> mapCourseIdByMaxStudents, Map<Integer, Integer> mapYearByNoCategories) throws ObjectNotFound {
//        for(Map.Entry<Long, Integer> entry : mapCourseIdByMaxStudents.entrySet()){
//            System.out.println("courseId: " + entry.getKey() + " maxStudents: " + entry.getValue());
//        }
//        for(Map.Entry<Integer, Integer> entry : mapYearByNoCategories.entrySet()){
//            System.out.println("year: " + entry.getKey() + " noCategories: " + entry.getValue());
//        }
        for (Map.Entry<Student, List<String>> entry : students.entrySet()) {
            System.out.println("student: " + entry.getKey());
            for (String category : entry.getValue()) {
                System.out.println("category: " + category);
            }
        }
//        System.out.println("student size: " + students.size());
//        System.out.println("course size: " + mapCourseIdByMaxStudents.size());
//        System.out.println("year size: " + mapYearByNoCategories.size());
//        Integer studentSizeTracker = 0;

        for (Map.Entry<Student, List<String>> studentEntry : students.entrySet()) {
            Student student = studentEntry.getKey();
            List<String> categoriesTaken = studentEntry.getValue();

            Iterator<Map.Entry<Long, Integer>> iterator = mapCourseIdByMaxStudents.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Long, Integer> courseEntry = iterator.next();
                Long courseId = courseEntry.getKey();
                Integer maxStudents = courseEntry.getValue();

                if (maxStudents == 0) {
                    iterator.remove();
                    continue;
                }

                Course course = courseRepository.findById(courseId).orElse(null);
                if (course == null)
                    continue;

                if (categoriesTaken.size() < mapYearByNoCategories.getOrDefault(student.getYear(), 0) &&
                        student.getFacultySection() == course.getFacultySection() &&
                        student.getYear().equals(course.getYear()) &&
                        !categoriesTaken.contains(course.getCategory())) {

                    mapCourseIdByMaxStudents.put(courseId, maxStudents - 1);
                    Enrollment enrollment = new Enrollment(student, course, 0, Status.ACCEPTED);
                    enrollmentRepository.save(enrollment);
                    categoriesTaken.add(course.getCategory());
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

    @Override
    public Enrollment reassingStudent(Long studentId, Long courseId, Long newCourseId){
        Enrollment enrollment = enrollmentRepository.findEnrollmentByStudentIdAndCourseIdAndStatusIsAccepted(studentId, courseId);
        //System.out.println("enrollment: " + enrollment);
        if(enrollment == null){
            return null;
        }
        enrollment.setCourse(courseRepository.findById(newCourseId).orElse(null));
        enrollmentRepository.save(enrollment);
        return enrollment;
    }
}
