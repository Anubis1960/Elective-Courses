package com.Spring.application.service;

import com.Spring.application.entity.Student;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


public interface ExcelGeneratorService {
	void exportStudentsToExcel(OutputStream outputStream) throws IOException;
}
