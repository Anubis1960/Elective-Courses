package com.Spring.application.service.impl;

import com.Spring.application.entity.Student;
import com.Spring.application.entity.User;
import com.Spring.application.enums.FacultySection;
import com.Spring.application.enums.Role;
import com.Spring.application.service.ExcelGeneratorService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ExcelGeneratorServiceImpl implements ExcelGeneratorService {
	@Override
	public void exportStudentsToExcel(HttpServletResponse response, List<Student> students) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Students");

		writeHeaderLine(sheet, workbook);
		writeDataLines(sheet, workbook, students);

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();

		outputStream.close();
	}

	private void writeHeaderLine(XSSFSheet sheet, XSSFWorkbook workbook) {
		Row row = sheet.createRow(0);

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);

		createCell(sheet, row, 0, "ID", style);
		createCell(sheet, row, 1, "Name", style);
		createCell(sheet, row, 2, "Email", style);
		createCell(sheet, row, 3, "Role", style);
		createCell(sheet, row, 4, "Faculty Section", style);
		createCell(sheet, row, 5, "Study Year", style);
		createCell(sheet, row, 6, "Grade", style);
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

	private void writeDataLines(XSSFSheet sheet, XSSFWorkbook workbook, List<Student> students) {
		int rowCount = 1;

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);

		for (Student student : students) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;

			createCell(sheet, row, columnCount++, student.getId(), style);
			createCell(sheet, row, columnCount++, student.getName(), style);
			createCell(sheet, row, columnCount++, student.getEmail(), style);
			createCell(sheet, row, columnCount++, student.getRole(), style);
			createCell(sheet, row, columnCount++, student.getFacultySection(), style);
			createCell(sheet, row, columnCount++, student.getYear(), style);
			createCell(sheet, row, columnCount++, student.getGrade(), style);
		}
	}
}
