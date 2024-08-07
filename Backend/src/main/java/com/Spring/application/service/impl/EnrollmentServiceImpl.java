package com.Spring.application.service.impl;

import com.Spring.application.dto.EnrollmentExporter;
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
import com.Spring.application.utils.GeneratorMethods;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service
public class EnrollmentServiceImpl implements EnrollmentService{
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @PersistenceContext
    private EntityManager entityManager;

    private static final int COURSE_NAME_FLAG = 1 << 0; // 1
    private static final int CATEGORY_FLAG = 1 << 1; // 2
    private static final int YEAR_FLAG = 1 << 2; // 4
    private static final int SECTION_FLAG = 1 << 3; // 8
    private static final int TEACHER_FLAG = 1 << 4; //16
    private static final int STUDENT_NAME_FLAG = 1 << 5; //32
    private static final int STUDENT_MAIL_FLAG = 1 << 6; //64
    private static final int GRADE_FLAG = 1 << 7; //128
    private static final int NUM_OF_STUDENTS_FLAG = 1 << 8; //256
    private static final int AVG_GRADE_FLAG = 1 << 9; //512

    @Override
    public Enrollment addEnrollment(Long studentId, Long courseId, Integer priority) throws ObjectNotFound {
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
        return enrollmentRepository.countByCourseId(courseId).orElse(0);
    }

    @Override
    public Integer countByCourseIdAndStatusIsAccepted(Long courseId){
        return enrollmentRepository.countAcceptedByCourseId(courseId).orElse(0);
    }

    @Override
    public Integer countByStudentId(Long studentId){
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
                if (categoriesTaken.size() < mapYearByNoCategories.get(studentRepository.findById(currentStudentId).orElse(null).getYear())) {
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
        if(enrollment == null){
            return null;
        }
        enrollment.setCourse(courseRepository.findById(newCourseId).orElse(null));
        enrollmentRepository.save(enrollment);
        return enrollment;
    }

    @Override
    public void export(OutputStream out, Optional<String> facultySection, Optional<Integer> year, int bitOptions, String extension) throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List<EnrollmentExporter> enrollments;

        StringBuilder query = new StringBuilder();

        int columnCount = 0;

        query.append("SELECT");

        if ((bitOptions & COURSE_NAME_FLAG) != 0) {
            query.append(" e.course.courseName,");
            columnCount++;
        }

        if ((bitOptions & CATEGORY_FLAG) != 0) {
            query.append(" e.course.category,");
            columnCount++;
        }

        if ((bitOptions & YEAR_FLAG) != 0) {
            query.append(" e.student.year,");
            columnCount++;
        }

        if ((bitOptions & SECTION_FLAG) != 0) {
            query.append(" e.student.facultySection,");
            columnCount++;
        }

        if ((bitOptions & TEACHER_FLAG) != 0) {
            query.append(" e.course.teacherName,");
            columnCount++;
        }

        if ((bitOptions & STUDENT_NAME_FLAG) != 0) {
            query.append(" e.student.name,");
            columnCount++;
        }

        if ((bitOptions & STUDENT_MAIL_FLAG) != 0) {
            query.append(" e.student.email,");
            columnCount++;
        }

        if ((bitOptions & GRADE_FLAG) != 0) {
            query.append(" e.student.grade,");
            columnCount++;
        }

        if ((bitOptions & NUM_OF_STUDENTS_FLAG) != 0) {
            query.append(" n.numOfStudents,");
            columnCount++;
        }

        if ((bitOptions & AVG_GRADE_FLAG) != 0) {
            query.append(" n.avgGrade,");
            columnCount++;
        }

        query.deleteCharAt(query.length() - 1); // Remove the trailing comma

        query.append(" FROM Enrollment e");
        query.append(", (SELECT e.course.courseId AS courseId, COUNT(e.student.id) AS numOfStudents, AVG(e.student.grade) AS avgGrade FROM Enrollment e GROUP BY e.course.courseId) n");

        query.append(" WHERE e.status = 'ACCEPTED' AND e.course.courseId = n.courseId");

        if (facultySection.isPresent() && year.isPresent()) {
            query.append(" AND e.student.facultySection = ").append(facultySection.get()).append(" AND e.student.year = ").append(year.get());
        } else if (facultySection.isPresent()) {
            query.append(" AND e.student.facultySection = ").append(facultySection.get());
        } else if (year.isPresent()) {
            query.append(" AND e.student.year = ").append(year.get());
        }

        List<Object> result = entityManager.createQuery(query.toString()).getResultList();

        enrollments = new ArrayList<>();

        for (Object obj : result) {
            EnrollmentExporter.EnrollmentBuilder enrollmentBuilder = new EnrollmentExporter.EnrollmentBuilder();

            if (columnCount == 1){
                if ((bitOptions & COURSE_NAME_FLAG) != 0) enrollmentBuilder.courseName((String) obj);
                if ((bitOptions & CATEGORY_FLAG) != 0) enrollmentBuilder.category((String) obj);
                if ((bitOptions & YEAR_FLAG) != 0) enrollmentBuilder.year((Integer) obj);
                if ((bitOptions & SECTION_FLAG) != 0) enrollmentBuilder.section(obj.toString());
                if ((bitOptions & TEACHER_FLAG) != 0) enrollmentBuilder.teacher((String) obj);
                if ((bitOptions & STUDENT_NAME_FLAG) != 0) enrollmentBuilder.name((String) obj);
                if ((bitOptions & STUDENT_MAIL_FLAG) != 0) enrollmentBuilder.email((String) obj);
                if ((bitOptions & GRADE_FLAG) != 0) enrollmentBuilder.grade((Float) obj);
                if ((bitOptions & NUM_OF_STUDENTS_FLAG) != 0) enrollmentBuilder.numberOfStudents((Long) obj);
                if ((bitOptions & AVG_GRADE_FLAG) != 0) enrollmentBuilder.avgGrade((Double) obj);
            } else {
                Object[] arr = (Object[]) obj;
                int index = 0;

                if ((bitOptions & COURSE_NAME_FLAG) != 0) enrollmentBuilder.courseName((String) arr[index++]);
                if ((bitOptions & CATEGORY_FLAG) != 0) enrollmentBuilder.category((String) arr[index++]);
                if ((bitOptions & YEAR_FLAG) != 0) enrollmentBuilder.year((Integer) arr[index++]);
                if ((bitOptions & SECTION_FLAG) != 0) enrollmentBuilder.section(arr[index++].toString());
                if ((bitOptions & TEACHER_FLAG) != 0) enrollmentBuilder.teacher((String) arr[index++]);
                if ((bitOptions & STUDENT_NAME_FLAG) != 0) enrollmentBuilder.name((String) arr[index++]);
                if ((bitOptions & STUDENT_MAIL_FLAG) != 0) enrollmentBuilder.email((String) arr[index++]);
                if ((bitOptions & GRADE_FLAG) != 0) enrollmentBuilder.grade((Float) arr[index++]);
                if ((bitOptions & NUM_OF_STUDENTS_FLAG) != 0) enrollmentBuilder.numberOfStudents((Long) arr[index++]);
                if ((bitOptions & AVG_GRADE_FLAG) != 0) enrollmentBuilder.avgGrade((Double) arr[index]);

            }

            enrollments.add(enrollmentBuilder.build());
        }

        if (extension.equals("pdf")) {
            GeneratorMethods.writePDF(enrollments, out);
        } else if (extension.equals("csv")) {
            GeneratorMethods.writeCSV(enrollments, out);
        } else if (extension.equals("excel")) {
            GeneratorMethods.writeXLSX(enrollments, out);
        }
    }
}
