package com.Spring.application.utils;

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
import java.util.ArrayList;
import java.util.List;


public class GeneratorMethods {

	public static <T> void writePDF(List<T> data, OutputStream out) throws IllegalAccessException, IOException {
		Document document = setUpDocument(out);
		writeTitlePDF(data.get(0).getClass().getSimpleName(), document);
		document.add(writeTable(data));
		document.close();
		out.flush();
		out.close();
	}

	public static <T> PdfPTable writeTable(List<T> data) throws IllegalAccessException {
		PdfPTable table = new PdfPTable(getHeaders(data.get(0)).size());
		table.setWidthPercentage(100);
		table.setSpacingBefore(10);
		table.setSpacingAfter(10);

		for(String header : getHeaders(data.get(0))) {
			writeHeadersPDF(header, table);
		}

		for(T obj : data) {
			for(Field field : obj.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				if(field.get(obj) != null)
				{
					writeDataPDF(field.get(obj).toString(), table);
				}
			}

			for (Field field : obj.getClass().getSuperclass().getDeclaredFields()) {
				field.setAccessible(true);
				if(field.get(obj) != null)
				{
					writeDataPDF(field.get(obj).toString(), table);
				}
			}
		}
		return table;
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
		out.write(csvData.toString().getBytes());
		out.flush();
		out.close();
	}

	public static <T> void writeXLSX(List<T> data, OutputStream out) throws IllegalAccessException, IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(data.get(0).getClass().getSimpleName());

		writeHeadersXLSX(sheet, workbook, getHeaders(data.get(0)));

		for(T obj : data) {
			writeDataXLSX(sheet, workbook, obj);
		}

		workbook.write(out);
		workbook.close();

		out.flush();
		out.close();
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
			cell.setCellValue((Float) value);

		} else if (value instanceof String) {
			cell.setCellValue((String) value);

		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);

		} else if (value instanceof Long){
			cell.setCellValue((Long) value);

		} else if(value instanceof Double) {
			cell.setCellValue((Double) value);

		} else {
			cell.setCellValue(value.toString());
		}
		cell.setCellStyle(style);
	}

	public static <T> void writeDataXLSX(XSSFSheet sheet, XSSFWorkbook workbook, T obj) throws IllegalAccessException {
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);

		Row row = sheet.createRow(sheet.getPhysicalNumberOfRows());
		int columnCount = 0;
		for(Field field : obj.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			if(field.get(obj) != null)
			{
				createCell(sheet, row, columnCount++, field.get(obj), style);
			}
		}

		for (Field field : obj.getClass().getSuperclass().getDeclaredFields()) {
			field.setAccessible(true);
			if(field.get(obj) != null)
			{
				createCell(sheet, row, columnCount++, field.get(obj), style);
			}
		}
	}

}
