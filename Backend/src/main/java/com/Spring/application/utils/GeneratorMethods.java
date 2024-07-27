package com.Spring.application.utils;

import com.Spring.application.dto.EnrollmentExporter;
import com.Spring.application.dto.StudentExporter;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.awt.Color;
import org.apache.poi.ss.usermodel.Row;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class GeneratorMethods {

	private static final DecimalFormat df = new DecimalFormat("#.00");

	public static <T> void writePDF(List<T> data, OutputStream out) throws IllegalAccessException, IOException, NoSuchMethodException, InvocationTargetException {
		PdfPTable table = new PdfPTable(getLenght(data.get(0)));
		table.setWidthPercentage(100);
		table.setSpacingBefore(10);
		table.setSpacingAfter(10);

		if (data.get(0) instanceof EnrollmentExporter){
			getEnrollmentsHeader((EnrollmentExporter) data.get(0)).forEach(header -> writeHeadersPDF(header, table));
			for (T obj : data) {
				writeEnrollmentsTable((EnrollmentExporter) obj, table);
			}
		}else if(data.get(0)  instanceof StudentExporter){
			getStudentsHeaderPDF((StudentExporter) data.get(0)).forEach(header -> writeHeadersPDF(header, table));
			for (T obj : data) {
				writeStudentsTable((StudentExporter) obj, table);
			}
		}
		else{
			writeTable(data, table);
		}

		Document document = setUpDocument(out);
		document.add(table);
		document.close();
		out.flush();
		out.close();
	}

	public static <T> int getLenght(T obj) throws IllegalAccessException {
		int lenght = 0;
		if (obj instanceof EnrollmentExporter) {

			if (((EnrollmentExporter) obj).getCourseName() != null) {
				lenght++;
			}

			if (((EnrollmentExporter) obj).getCategory() != null) {
				lenght++;
			}

			if (((EnrollmentExporter) obj).getTeacher() != null) {
				lenght++;
			}

			if (((EnrollmentExporter) obj).getNumberOfStudents() != null) {
				lenght++;
			}

			if (((EnrollmentExporter) obj).getAvgGrade() != null) {
				lenght++;
			}

			if (((EnrollmentExporter) obj).getStudentName() != null) {
				lenght++;
			}

			if (((EnrollmentExporter) obj).getEmail() != null) {
				lenght++;
			}

			if (((EnrollmentExporter) obj).getGrade() != null) {
				lenght++;
			}

			if (((EnrollmentExporter) obj).getFacultySection() != null) {
				lenght++;
			}

			if (((EnrollmentExporter) obj).getYear() != null) {
				lenght++;
			}

		} else if (obj instanceof StudentExporter) {

			if (((StudentExporter) obj).getStudentName() != null) {
				lenght++;
			}

			if (((StudentExporter) obj).getEmail() != null) {
				lenght++;
			}

			if (((StudentExporter) obj).getGrade() != null) {
				lenght++;
			}

			if (((StudentExporter) obj).getFacultySection() != null) {
				lenght++;
			}

			if (((StudentExporter) obj).getYear() != null) {
				lenght++;
			}

		} else {
			for (Field field : obj.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				if (field.get(obj) != null) {
					lenght++;
				}
			}

			for (Field field : obj.getClass().getSuperclass().getDeclaredFields()) {
				field.setAccessible(true);
				if (field.get(obj) != null) {
					lenght++;
				}
			}
		}

		return lenght;
	}

	public static void writeStudentsTable(StudentExporter student, PdfPTable table) {
			if (student.getStudentName() != null) {
				writeDataPDF(student.getStudentName(), table);
			}

			if (student.getEmail() != null) {
				writeDataPDF(student.getEmail(), table);
			}

			if (student.getGrade() != null) {
				writeDataPDF(df.format(student.getGrade()), table);
			}

			if (student.getFacultySection() != null) {
				writeDataPDF(student.getFacultySection(), table);
			}

			if (student.getYear() != null) {
				writeDataPDF(student.getYear().toString(), table);
			}
	}

	public static List<String> getStudentsHeaderPDF(StudentExporter student) {
		List<String> headers = new ArrayList<>();
		if (student.getStudentName() != null) {
			headers.add("Student Name");
		}

		if (student.getEmail() != null) {
			headers.add("Email");
		}

		if (student.getGrade() != null) {
			headers.add("Grade");
		}

		if (student.getFacultySection() != null) {
			headers.add("Faculty Section");
		}

		if (student.getYear() != null) {
			headers.add("Year");
		}
		return headers;
	}

	public static void writeEnrollmentsTable(EnrollmentExporter enrollment, PdfPTable table) {
			if (enrollment.getCourseName() != null) {
				writeDataPDF(enrollment.getCourseName(), table);
			}

			if (enrollment.getCategory() != null) {
				writeDataPDF(enrollment.getCategory(), table);
			}

			if (enrollment.getTeacher() != null) {
				writeDataPDF(enrollment.getTeacher(), table);
			}

			if (enrollment.getNumberOfStudents() != null) {
				writeDataPDF(enrollment.getNumberOfStudents().toString(), table);
			}

			if (enrollment.getAvgGrade() != null) {
				writeDataPDF(df.format(enrollment.getAvgGrade()), table);
			}

			writeStudentsTable(enrollment, table);
	}

	public static List<String> getEnrollmentsHeader(EnrollmentExporter enrollment) {
		List<String> headers = new ArrayList<>();
		if (enrollment.getCourseName() != null) {
			headers.add("Course Name");
		}

		if (enrollment.getCategory() != null) {
			headers.add("Category");
		}

		if (enrollment.getTeacher() != null) {
			headers.add("Teacher");
		}

		if (enrollment.getNumberOfStudents() != null) {
			headers.add("Number of Students");
		}

		if (enrollment.getAvgGrade() != null) {
			headers.add("Average Grade");
		}

		headers.addAll(getStudentsHeaderPDF(enrollment));
		return headers;
	}

	public static <T> void writeTable(List<T> data, PdfPTable table) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

		for(String header : getHeaders(data.get(0))) {
			writeHeadersPDF(header, table);
		}

		for(T obj : data) {
			for(Field field : obj.getClass().getDeclaredFields()) {
				Method method = obj.getClass().getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
				if(method.invoke(obj) != null)
				{
					writeDataPDF(method.invoke(obj).toString(), table);
				}
			}

			for (Field field : obj.getClass().getSuperclass().getDeclaredFields()) {
				Method method = obj.getClass().getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
				if(method.invoke(obj) != null)
				{
					writeDataPDF(method.invoke(obj).toString(), table);
				}
			}
		}
	}

	public static Document setUpDocument(OutputStream out) {
		Document document = new Document();
		PdfWriter.getInstance(document, out);
		document.open();
		return document;
	}

	public static List<String> getHeaders(Object obj) throws IllegalAccessException {
		List<String> headers = new ArrayList<>();

		for(Field field : obj.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			if(field.get(obj) != null)
            {
				headers.add(field.getName());
			}
		}

		for(Field field : obj.getClass().getSuperclass().getDeclaredFields()) {
			field.setAccessible(true);
			if(field.get(obj) != null)
			{
				headers.add(field.getName());
			}
		}
		return headers;
	}

	public static void writeTitlePDF(String string, Document document) {
		// Define title font
		Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.BLACK);

		// Set up title position
		Paragraph title = new Paragraph(string, titleFont);
		title.setAlignment(Element.ALIGN_CENTER);
		document.add(title);
		document.add(new Paragraph(" "));
	}

	public static void writeHeadersPDF(String name, PdfPTable table) {
		// Define header font
		Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);

		PdfPCell header = new PdfPCell(new Paragraph(name, headerFont));
		header.setBackgroundColor(Color.GRAY);
		header.setHorizontalAlignment(Element.ALIGN_CENTER);
		header.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(header);
	}

	public static void writeDataPDF(String data, PdfPTable table) {
		// Define cell font
		Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);

		PdfPCell cell = new PdfPCell(new Paragraph(data, cellFont));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
	}

	public static <T> void writeCSV(List<T> data, OutputStream out) throws IllegalAccessException, IOException {
		StringBuilder csvData = new StringBuilder();
		if (data.get(0) instanceof EnrollmentExporter){
			csvData.append(getEnrollmentsHeader((EnrollmentExporter) data.get(0)).toString().replace("[", "").replace("]", "").replace(", ", ",")).append("\n");
			for(T obj : data) {
				if (((EnrollmentExporter) obj).getCourseName() != null) {
					csvData.append(((EnrollmentExporter) obj).getCourseName()).append(",");
				}

				if (((EnrollmentExporter) obj).getCategory() != null) {
					csvData.append(((EnrollmentExporter) obj).getCategory()).append(",");
				}

				if (((EnrollmentExporter) obj).getTeacher() != null) {
					csvData.append(((EnrollmentExporter) obj).getTeacher()).append(",");
				}

				if (((EnrollmentExporter) obj).getNumberOfStudents() != null) {
					csvData.append(((EnrollmentExporter) obj).getNumberOfStudents()).append(",");
				}

				if (((EnrollmentExporter) obj).getAvgGrade() != null) {
					csvData.append(df.format(((EnrollmentExporter) obj).getAvgGrade())).append(",");
				}

				if (((EnrollmentExporter) obj).getStudentName() != null) {
					csvData.append(((EnrollmentExporter) obj).getStudentName()).append(",");
				}

				if (((EnrollmentExporter) obj).getEmail() != null) {
					csvData.append(((EnrollmentExporter) obj).getEmail()).append(",");
				}

				if (((EnrollmentExporter) obj).getGrade() != null) {
					csvData.append(df.format(((EnrollmentExporter) obj).getGrade())).append(",");
				}

				if (((EnrollmentExporter) obj).getFacultySection() != null) {
					csvData.append(((EnrollmentExporter) obj).getFacultySection()).append(",");
				}

				if (((EnrollmentExporter) obj).getYear() != null) {
					csvData.append(((EnrollmentExporter) obj).getYear()).append(",");
				}

				if (((EnrollmentExporter) obj).getStudentName() != null ) {
					csvData.append(((EnrollmentExporter) obj).getStudentName()).append(",");
				}

				if (((EnrollmentExporter) obj).getEmail() != null) {
					csvData.append(((EnrollmentExporter) obj).getEmail()).append(",");
				}

				if (((EnrollmentExporter) obj).getGrade() != null) {
					csvData.append(df.format(((EnrollmentExporter) obj).getGrade())).append(",");
				}

				if (((EnrollmentExporter) obj).getFacultySection() != null) {
					csvData.append(((EnrollmentExporter) obj).getFacultySection()).append(",");
				}

				if (((EnrollmentExporter) obj).getYear() != null) {
					csvData.append(((EnrollmentExporter) obj).getYear()).append(",");
				}

				csvData.setLength(csvData.length() - 1);

				csvData.append("\n");

			}
		}
		else if(data.get(0) instanceof StudentExporter){
			csvData.append(getStudentsHeaderPDF((StudentExporter) data.get(0)).toString().replace("[", "").replace("]", "").replace(", ", ",")).append("\n");
			for(T obj : data) {
				if (((StudentExporter) obj).getStudentName() != null) {
					csvData.append(((StudentExporter) obj).getStudentName()).append(",");
				}

				if (((StudentExporter) obj).getEmail() != null) {
					csvData.append(((StudentExporter) obj).getEmail()).append(",");
				}

				if (((StudentExporter) obj).getGrade() != null) {
					csvData.append(df.format(((StudentExporter) obj).getGrade())).append(",");
				}

				if (((StudentExporter) obj).getFacultySection() != null) {
					csvData.append(((StudentExporter) obj).getFacultySection()).append(",");
				}

				if (((StudentExporter) obj).getYear() != null) {
					csvData.append(((StudentExporter) obj).getYear()).append(",");
				}

				csvData.setLength(csvData.length() - 1);

				csvData.append("\n");
			}
		}
		else{
			csvData.append(getHeaders(data.get(0)).toString().replace("[", "").replace("]", "").replace(", ", ",")).append("\n");
			for(T obj : data) {
				for(Field field : obj.getClass().getDeclaredFields()) {
					field.setAccessible(true);
					if(field.get(obj) != null)
					{
						csvData.append(field.get(obj)).append(",");
					}
				}
				for (Field field : obj.getClass().getSuperclass().getDeclaredFields()) {
					field.setAccessible(true);
					if(field.get(obj) != null)
					{
						csvData.append(field.get(obj)).append(",");
					}
				}
				csvData.setLength(csvData.length() - 1);
				csvData.append("\n");
			}

		}
		out.write(csvData.toString().getBytes());
		out.flush();
		out.close();
	}

	public static <T> void writeXLSX(List<T> data, OutputStream out) throws IllegalAccessException, IOException, InvocationTargetException, NoSuchMethodException {
		XSSFWorkbook workbook = new XSSFWorkbook();


		if (data.get(0) instanceof EnrollmentExporter){
			XSSFSheet sheet = workbook.createSheet("Enrollments");
			writeHeadersXLSX(sheet, workbook, getEnrollmentsHeader((EnrollmentExporter) data.get(0)));
			for(T obj : data) {
				writeEnrollmentXLSX((EnrollmentExporter) obj, sheet, workbook, sheet.getPhysicalNumberOfRows(), 0);
			}
		}
		else if(data.get(0) instanceof StudentExporter){
			XSSFSheet sheet = workbook.createSheet("Students");
			writeHeadersXLSX(sheet, workbook, getStudentsHeaderPDF((StudentExporter) data.get(0)));
			for(T obj : data) {
				writeStudentsXLSX((StudentExporter) obj, sheet, workbook, sheet.getPhysicalNumberOfRows(), 0);
			}
		}
		else{
			XSSFSheet sheet = workbook.createSheet(data.get(0).getClass().getSimpleName());
			writeHeadersXLSX(sheet, workbook, getHeaders(data.get(0)));
			for(T obj : data) {
				writeDataXLSX(sheet, workbook, obj);
			}
		}

		workbook.write(out);
		workbook.close();

		out.flush();
		out.close();
	}

	public static void writeStudentsXLSX(StudentExporter student, XSSFSheet sheet, XSSFWorkbook workbook, int rowNumber, int columnNumber) {
		Row row = sheet.createRow(rowNumber);

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);

		if (student.getStudentName() != null) {
			createCell(sheet, row, columnNumber++, student.getStudentName(), style);
		}

		if (student.getEmail() != null) {
			createCell(sheet, row, columnNumber++, student.getEmail(), style);
		}

		if (student.getGrade() != null) {
			createCell(sheet, row, columnNumber++, df.format(student.getGrade()), style);
		}

		if (student.getFacultySection() != null) {
			createCell(sheet, row, columnNumber++, student.getFacultySection(), style);
		}

		if (student.getYear() != null) {
			createCell(sheet, row, columnNumber, student.getYear(), style);
		}
	}

	public static void writeEnrollmentXLSX(EnrollmentExporter enrollment, XSSFSheet sheet, XSSFWorkbook workbook, int rowNumber, int columnNumber) {
		Row row = sheet.createRow(rowNumber);

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);

		if (enrollment.getCourseName() != null) {
			createCell(sheet, row, columnNumber++, enrollment.getCourseName(), style);
		}

		if (enrollment.getCategory() != null) {
			createCell(sheet, row, columnNumber++, enrollment.getCategory(), style);
		}

		if (enrollment.getTeacher() != null) {
			createCell(sheet, row, columnNumber++, enrollment.getTeacher(), style);
		}

		if (enrollment.getNumberOfStudents() != null) {
			createCell(sheet, row, columnNumber++, enrollment.getNumberOfStudents(), style);
		}

		if (enrollment.getAvgGrade() != null) {
			createCell(sheet, row, columnNumber++, df.format(enrollment.getAvgGrade()), style);
		}

		if (enrollment.getStudentName() != null) {
			createCell(sheet, row, columnNumber++, enrollment.getStudentName(), style);
		}

		if (enrollment.getEmail() != null) {
			createCell(sheet, row, columnNumber++, enrollment.getEmail(), style);
		}

		if (enrollment.getGrade() != null) {
			createCell(sheet, row, columnNumber++, df.format(enrollment.getGrade()), style);
		}

		if (enrollment.getFacultySection() != null) {
			createCell(sheet, row, columnNumber++, enrollment.getFacultySection(), style);
		}

		if (enrollment.getYear() != null) {
			createCell(sheet, row, columnNumber, enrollment.getYear(), style);
		}
	}

	public static void writeHeadersXLSX(XSSFSheet sheet, XSSFWorkbook workbook, List<String> headers) {
		Row row = sheet.createRow(0);

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);

		for(int i = 0; i < headers.size(); i++) {
			createCell(sheet, row, i, headers.get(i), style);
		}
	}

	public static <V> void createCell(XSSFSheet sheet, Row row, int columnCount, V value, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);

		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);

		} else if (value instanceof Float) {
			cell.setCellValue(BigDecimal.valueOf((Float) value).setScale(2, RoundingMode.HALF_UP).floatValue());

		} else if (value instanceof String) {
			cell.setCellValue((String) value);

		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);

		} else if (value instanceof Long){
			cell.setCellValue((Long) value);

		} else if(value instanceof Double) {
			cell.setCellValue(BigDecimal.valueOf((Double) value).setScale(2, RoundingMode.HALF_UP).doubleValue());
		} else {
			cell.setCellValue(value.toString());
		}
		cell.setCellStyle(style);
	}

	public static <T> void writeDataXLSX(XSSFSheet sheet, XSSFWorkbook workbook, T obj) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);

		Row row = sheet.createRow(sheet.getPhysicalNumberOfRows());
		int columnCount = 0;
		for(Field field : obj.getClass().getDeclaredFields()) {
			Method method = obj.getClass().getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
			if(method.invoke(obj) != null)
			{
				createCell(sheet, row, columnCount++, method.invoke(obj), style);
			}

		}

		for (Field field : obj.getClass().getSuperclass().getDeclaredFields()) {
			Method method = obj.getClass().getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
			if(method.invoke(obj) != null)
			{
				createCell(sheet, row, columnCount++, method.invoke(obj), style);
			}
		}
	}

}
