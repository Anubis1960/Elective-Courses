package com.Spring.application.service.impl;

import com.Spring.application.entity.Enrollment;
import com.Spring.application.entity.Student;
import com.Spring.application.enums.FacultySection;
import com.Spring.application.enums.Role;
import com.Spring.application.repository.StudentRepository;
import com.Spring.application.service.ExcelGeneratorService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

@Service
public class ExcelGeneratorServiceImpl implements ExcelGeneratorService {
	@Autowired
	private StudentRepository studentRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public List<Enrollment> executeQuery(String query){
		return entityManager.createQuery(query, Enrollment.class).getResultList();
	}

	@Override
	public void exportStudentsToExcel(OutputStream out, Optional<String> facultySection, Optional<Integer> year, boolean includeId, boolean includeName, boolean includeEmail, boolean includeGrade, boolean includeSection, boolean includeYear) throws IOException {
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

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Students");

		writeStudentHeaderLine(sheet, workbook, includeId, includeName, includeEmail, includeGrade, includeSection, includeYear);
		writeStudentDataLines(sheet, workbook, students, includeId, includeName, includeEmail, includeGrade, includeSection, includeYear);

		workbook.write(out);
		workbook.close();

		out.close();
	}

	private void writeStudentHeaderLine(XSSFSheet sheet, XSSFWorkbook workbook, boolean includeId, boolean includeName, boolean includeEmail, boolean includeGrade, boolean includeSection, boolean includeYear) {
		Row row = sheet.createRow(0);

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);

		int columnCount = 0;
		if (includeId) {
			createCell(sheet, row, columnCount++, "ID", style);
		}
		if (includeName) {
			createCell(sheet, row, columnCount++, "Name", style);
		}
		if (includeEmail) {
			createCell(sheet, row, columnCount++, "Email", style);
		}
		if (includeGrade) {
			createCell(sheet, row, columnCount++, "Grade", style);
		}
		if (includeSection) {
			createCell(sheet, row, columnCount++, "Section", style);
		}
		if (includeYear) {
			createCell(sheet, row, columnCount++, "Year", style);
		}
	}

	private void createCell(XSSFSheet sheet, Row row, int columnCount, Object value, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);

		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);

		} else if (value instanceof Long) {
			cell.setCellValue((Long) value);

		} else if (value instanceof Float) {
			cell.setCellValue((Float) value);

		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);

		} else if (value instanceof Role || value instanceof FacultySection){
			cell.setCellValue(value.toString());

		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}

	private void writeStudentDataLines(XSSFSheet sheet, XSSFWorkbook workbook, List<Student> students, boolean includeId, boolean includeName, boolean includeEmail, boolean includeGrade, boolean includeSection, boolean includeYear) {
		int rowCount = 1;

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);

		for (Student student : students) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;

			if (includeId) {
				createCell(sheet, row, columnCount++, student.getId(), style);
			}
			if (includeName) {
				createCell(sheet, row, columnCount++, student.getName(), style);
			}
			if (includeEmail) {
				createCell(sheet, row, columnCount++, student.getEmail(), style);
			}
			if (includeGrade) {
				createCell(sheet, row, columnCount++, student.getGrade(), style);
			}
			if (includeSection) {
				createCell(sheet, row, columnCount++, student.getFacultySection(), style);
			}
			if (includeYear) {
				createCell(sheet, row, columnCount++, student.getYear(), style);
			}
		}
	}

	@Override
	public void exportEnrollmentsToExcel(OutputStream out, Optional<String> facultySection, Optional<Integer> year, boolean includeEnrollmentId, boolean includeStudentId, boolean includeCourseId, boolean includeYear, boolean IncludeSection, boolean includeCourseName, boolean includeStudentName, boolean includeTeacher, boolean includeStudentMail, boolean includeGrade, boolean includeCategory) throws IOException {
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

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Enrollments");

		writeEnrollmentHeaderLine(sheet, workbook, includeEnrollmentId, includeStudentId, includeCourseId, includeYear, IncludeSection, includeCourseName, includeStudentName, includeTeacher, includeStudentMail, includeGrade, includeCategory);
		writeEnrollmentDataLines(sheet, workbook, enrollments, includeEnrollmentId, includeStudentId, includeCourseId, includeYear, IncludeSection, includeCourseName, includeStudentName, includeTeacher, includeStudentMail, includeGrade, includeCategory);

		workbook.write(out);
		workbook.close();

		out.close();
	}

	private void writeEnrollmentHeaderLine(XSSFSheet sheet, XSSFWorkbook workbook, boolean includeEnrollmentId, boolean includeStudentId, boolean includeCourseId, boolean includeYear, boolean IncludeSection, boolean includeCourseName, boolean includeStudentName, boolean includeTeacher, boolean includeStudentMail, boolean includeGrade, boolean includeCategory) {
		Row row = sheet.createRow(0);

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);

		int columnCount = 0;
		if (includeEnrollmentId) {
			createCell(sheet, row, columnCount++, "Enrollment ID", style);
		}
		if (includeStudentId) {
			createCell(sheet, row, columnCount++, "Student ID", style);
		}
		if (includeCourseId) {
			createCell(sheet, row, columnCount++, "Course ID", style);
		}
		if (includeYear) {
			createCell(sheet, row, columnCount++, "Year", style);
		}
		if (IncludeSection) {
			createCell(sheet, row, columnCount++, "Section", style);
		}
		if (includeCourseName) {
			createCell(sheet, row, columnCount++, "Course Name", style);
		}
		if (includeStudentName) {
			createCell(sheet, row, columnCount++, "Student Name", style);
		}
		if (includeTeacher) {
			createCell(sheet, row, columnCount++, "Teacher", style);
		}
		if (includeStudentMail) {
			createCell(sheet, row, columnCount++, "Student Mail", style);
		}
		if (includeGrade) {
			createCell(sheet, row, columnCount++, "Grade", style);
		}
		if (includeCategory) {
			createCell(sheet, row, columnCount++, "Category", style);
		}
	}

	private void writeEnrollmentDataLines(XSSFSheet sheet, XSSFWorkbook workbook, List<Enrollment> enrollments, boolean includeEnrollmentId, boolean includeStudentId, boolean includeCourseId, boolean includeYear, boolean IncludeSection, boolean includeCourseName, boolean includeStudentName, boolean includeTeacher, boolean includeStudentMail, boolean includeGrade, boolean includeCategory) {
		int rowCount = 1;

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);

		for (Enrollment enrollment : enrollments) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;

			if (includeEnrollmentId) {
				createCell(sheet, row, columnCount++, enrollment.getEnrollmentId(), style);
			}
			if (includeStudentId) {
				createCell(sheet, row, columnCount++, enrollment.getStudent().getId(), style);
			}
			if (includeCourseId) {
				createCell(sheet, row, columnCount++, enrollment.getCourse().getCourseId(), style);
			}
			if (includeYear) {
				createCell(sheet, row, columnCount++, enrollment.getStudent().getYear(), style);
			}
			if (IncludeSection) {
				createCell(sheet, row, columnCount++, enrollment.getStudent().getFacultySection(), style);
			}
			if (includeCourseName) {
				createCell(sheet, row, columnCount++, enrollment.getCourse().getCourseName(), style);
			}
			if (includeStudentName) {
				createCell(sheet, row, columnCount++, enrollment.getStudent().getName(), style);
			}
			if (includeTeacher) {
				createCell(sheet, row, columnCount++, enrollment.getCourse().getTeacherName(), style);
			}
			if (includeStudentMail) {
				createCell(sheet, row, columnCount++, enrollment.getStudent().getEmail(), style);
			}
			if (includeGrade) {
				createCell(sheet, row, columnCount++, enrollment.getStudent().getGrade(), style);
			}
			if (includeCategory) {
				createCell(sheet, row, columnCount++, enrollment.getCourse().getCategory(), style);
			}
		}
	}
}
