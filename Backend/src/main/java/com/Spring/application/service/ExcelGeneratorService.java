package com.Spring.application.service;

import com.Spring.application.entity.Student;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


public interface ExcelGeneratorService {
	void exportStudentsToExcel(HttpServletResponse response, List<Student> users) throws IOException;
}
