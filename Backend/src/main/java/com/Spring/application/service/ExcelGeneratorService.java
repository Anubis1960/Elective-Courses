package com.Spring.application.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;


public interface ExcelGeneratorService {
	void exportStudentsToExcel(OutputStream out, Optional<String> facultySection, Optional<Integer> year, boolean includeId, boolean includeName, boolean includeEmail, boolean includeGrade, boolean includeSection, boolean includeYear) throws IOException;
	void exportEnrollmentsToExcel(OutputStream out, Optional<String> facultySection, Optional<Integer> year, boolean includeEnrollmentId, boolean includeStudentId, boolean includeCourseId, boolean includeYear, boolean IncludeSection, boolean includeCourseName, boolean includeStudentName, boolean includeTeacher, boolean includeStudentMail, boolean includeGrade, boolean includeCategory) throws IOException;
}
