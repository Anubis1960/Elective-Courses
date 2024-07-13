package com.Spring.application.service.impl;

import com.Spring.application.entity.Enrollment;
import com.Spring.application.entity.Student;
import com.Spring.application.enums.FacultySection;
import com.Spring.application.repository.StudentRepository;
import com.Spring.application.service.CSVGeneratorService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CSVGeneratorServiceImpl implements CSVGeneratorService {
	@Autowired
	private StudentRepository studentRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public List<Enrollment> executeQuery(String query){
		return entityManager.createQuery(query, Enrollment.class).getResultList();
	}
	@Override
	public void exportStudentsToCSV(OutputStream out, Optional<String> facultySection, Optional<Integer> year,
									boolean includeId, boolean includeName, boolean includeEmail,
									boolean includeGrade, boolean includeSection, boolean includeYear) throws IOException {
		List<Student> students;
		if (facultySection.isPresent() && year.isPresent()) {
			students = entityManager.createQuery("SELECT s FROM Student s WHERE s.facultySection = :facultySection AND s.year = :year", Student.class)
					.setParameter("facultySection", FacultySection.valueOf(facultySection.get()))
					.setParameter("year", year.get())
					.getResultList();
		} else if (facultySection.isPresent()) {
			students = entityManager.createQuery("SELECT s FROM Student s WHERE s.facultySection = :facultySection", Student.class)
					.setParameter("facultySection", FacultySection.valueOf(facultySection.get()))
					.getResultList();
		} else if (year.isPresent()) {
			students = entityManager.createQuery("SELECT s FROM Student s WHERE s.year = :year", Student.class)
					.setParameter("year", year.get())
					.getResultList();
		} else {
			students = studentRepository.findAll();
		}

		CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(out));

		writeStudentHeader(csvWriter, includeId, includeName, includeEmail, includeGrade, includeSection, includeYear);
		writeStudentData(csvWriter, students, includeId, includeName, includeEmail, includeGrade, includeSection, includeYear);

		csvWriter.flush();
		csvWriter.close();

		out.close();
	}

	private void writeStudentHeader(CSVWriter csvWriter, boolean includeId, boolean includeName, boolean includeEmail,
									boolean includeGrade, boolean includeSection, boolean includeYear) {
		List<String> header = new ArrayList<>();
		if (includeId) header.add("ID");
		if (includeName) header.add("Name");
		if (includeEmail) header.add("Email");
		if (includeGrade) header.add("Grade");
		if (includeSection) header.add("Section");
		if (includeYear) header.add("Year");

		csvWriter.writeNext(header.toArray(new String[0]));
	}

	private void writeStudentData(CSVWriter csvWriter, List<Student> students, boolean includeId, boolean includeName,
								  boolean includeEmail, boolean includeGrade, boolean includeSection, boolean includeYear) {
		for (Student student : students) {
			List<String> data = new ArrayList<>();
			if (includeId) data.add(student.getId().toString());
			if (includeName) data.add(student.getName());
			if (includeEmail) data.add(student.getEmail());
			if (includeGrade) data.add(student.getGrade().toString());
			if (includeSection) data.add(student.getFacultySection().toString());
			if (includeYear) data.add(String.valueOf(student.getYear()));

			csvWriter.writeNext(data.toArray(new String[0]));
		}
	}

	@Override
	public void exportEnrollmentsToCSV(OutputStream out, Optional<String> facultySection, Optional<Integer> year,
									   boolean includeEnrollmentId, boolean includeStudentId, boolean includeCourseId,
									   boolean includeYear, boolean IncludeSection, boolean includeCourseName,
									   boolean includeStudentName, boolean includeTeacher, boolean includeStudentMail,
									   boolean includeGrade, boolean includeCategory) throws IOException {
		List<Enrollment> enrollments;
		if (facultySection.isPresent() && year.isPresent()) {
			enrollments = entityManager.createQuery("SELECT e FROM Enrollment e WHERE e.student.facultySection = :facultySection AND e.student.year = :year AND e.status = 'ACCEPTED'", Enrollment.class)
					.setParameter("facultySection", FacultySection.valueOf(facultySection.get()))
					.setParameter("year", year.get())
					.getResultList();
		} else if (facultySection.isPresent()) {
			enrollments = entityManager.createQuery("SELECT e FROM Enrollment e WHERE e.student.facultySection = :facultySection AND e.status = 'ACCEPTED'", Enrollment.class)
					.setParameter("facultySection", FacultySection.valueOf(facultySection.get()))
					.getResultList();
		} else if (year.isPresent()) {
			enrollments = entityManager.createQuery("SELECT e FROM Enrollment e WHERE e.student.year = :year AND e.status = 'ACCEPTED'", Enrollment.class)
					.setParameter("year", year.get())
					.getResultList();
		} else {
			enrollments = entityManager.createQuery("SELECT e FROM Enrollment e WHERE e.status = 'ACCEPTED'", Enrollment.class)
					.getResultList();
		}

		CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(out));

		writeEnrollmentHeader(csvWriter, includeEnrollmentId, includeStudentId,
				includeCourseId, includeYear, IncludeSection,
				includeCourseName, includeStudentName, includeTeacher,
				includeStudentMail, includeGrade, includeCategory);
		writeEnrollmentData(csvWriter, enrollments, includeEnrollmentId, includeStudentId,
				includeCourseId, includeYear, IncludeSection,
				includeCourseName, includeStudentName, includeTeacher,
				includeStudentMail, includeGrade, includeCategory);

		csvWriter.flush();
		csvWriter.close();

		out.close();
	}

	private void writeEnrollmentHeader(CSVWriter csvWriter,  boolean includeEnrollmentId, boolean includeStudentId, boolean includeCourseId,
									   boolean includeYear, boolean IncludeSection, boolean includeCourseName,
									   boolean includeStudentName, boolean includeTeacher, boolean includeStudentMail,
									   boolean includeGrade, boolean includeCategory)
	{
		List<String> header = new ArrayList<>();
		if (includeEnrollmentId) header.add("ID");
		if (includeStudentId) header.add("StudentID");
		if (includeCourseId) header.add("CourseID");
		if (includeYear) header.add("Year");
		if (IncludeSection) header.add("Section");
		if (includeCourseName) header.add("Course Name");
		if (includeStudentName) header.add("Student Name");
		if (includeTeacher) header.add("Teacher");
		if (includeStudentMail) header.add("Student Mail");
		if (includeGrade) header.add("Grade");
		if (includeCategory) header.add("Category");

		csvWriter.writeNext(header.toArray(new String[0]));
	}

	private void writeEnrollmentData(CSVWriter csvWriter, List<Enrollment> enrollments, boolean includeEnrollmentId,
									 boolean includeStudentId, boolean includeCourseId,
									 boolean includeYear, boolean IncludeSection, boolean includeCourseName,
									 boolean includeStudentName, boolean includeTeacher, boolean includeStudentMail,
									 boolean includeGrade, boolean includeCategory)
	{
		for (Enrollment enrollment : enrollments) {
			List<String> data = new ArrayList<>();
			if (includeEnrollmentId) data.add(enrollment.getEnrollmentId().toString());
			if (includeStudentId) data.add(enrollment.getStudent().getId().toString());
			if (includeCourseId) data.add(enrollment.getCourse().getCourseId().toString());
			if (includeYear) data.add(enrollment.getStudent().getYear().toString());
			if (IncludeSection) data.add(enrollment.getCourse().getFacultySection().toString());
			if (includeCourseName) data.add(enrollment.getCourse().getCourseName());
			if (includeStudentName) data.add(enrollment.getStudent().getName());
			if (includeTeacher) data.add(enrollment.getCourse().getTeacherName());
			if (includeStudentMail)	data.add(enrollment.getStudent().getEmail());
			if (includeGrade) data.add(enrollment.getStudent().getGrade().toString());
			if (includeCategory) data.add(enrollment.getCourse().getCategory());

			csvWriter.writeNext(data.toArray(new String[0]));
		}
	}
}
