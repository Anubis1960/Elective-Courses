package com.Spring.application.utils;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.*;
import java.io.OutputStream;

public class GeneratorMethods {

	public static Document setUpDocument(OutputStream out) {
		Document document = new Document();
		PdfWriter.getInstance(document, out);
		document.open();
		return document;
	}

	public static void addTitleToPDF(String string, Document document) {
		// Define title font
		Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.BLACK);

		// Set up title position
		Paragraph title = new Paragraph(string, titleFont);
		title.setAlignment(Element.ALIGN_CENTER);
		document.add(title);
		document.add(new Paragraph(" "));
	}

	public static void addDataForPDF(String data, PdfPTable table) {
		// Define cell font
		Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);

		PdfPCell cell = new PdfPCell(new Paragraph(data, cellFont));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
	}

	public static void addHeadersToPDF(String name, PdfPTable table) {
		// Define header font
		Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);

		PdfPCell header = new PdfPCell(new Paragraph(name, headerFont));
		header.setBackgroundColor(Color.GRAY);
		header.setHorizontalAlignment(Element.ALIGN_CENTER);
		header.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(header);
	}
}
