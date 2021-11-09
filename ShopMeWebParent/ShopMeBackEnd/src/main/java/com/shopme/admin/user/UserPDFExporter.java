package com.shopme.admin.user;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.shopme.common.entity.User;

public class UserPDFExporter {

	public void export(List<User> userList, HttpServletResponse response) throws DocumentException, IOException {
		setResponseHeader(response);
		
		Document doc = new Document(PageSize.A4);
		PdfWriter.getInstance(doc, response.getOutputStream());
		
		doc.open();
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
			
		Paragraph paragraph = new Paragraph("List of Users", font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		
		writeTableHeader(table);
		writeTableDate(table, userList);
		
		doc.add(paragraph);
		doc.add(table);
		
		doc.close();
		
	}

	private void writeTableDate(PdfPTable table, List<User> userList) {
		
		for(User user : userList) {
			
			table.addCell(String.valueOf(user.getId()));
			table.addCell(user.getEmail());
			table.addCell(user.getFirstName());
			table.addCell(user.getLastName());
			table.addCell(user.getRoles().toString());
			table.addCell(String.valueOf(user.isEnabled()));
		}
		
	}

	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setPadding(5);
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setSize(14);
			
		cell.setPhrase(new Phrase("User ID", font));
		cell.setHorizontalAlignment(cell.ALIGN_CENTER);
		table.addCell(cell);

		cell.setPhrase(new Phrase("Email", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("First Name", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Last Name", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Roles", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Enabled", font));
		table.addCell(cell);
		
		
	}

	private void setResponseHeader(HttpServletResponse response) {
		DateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String timestamp = dateformatter.format(new Date());		
		String fileName = "users_" + timestamp + ".pdf";

		response.setContentType("application/pdf");

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;		
		response.setHeader(headerKey, headerValue);
	}
}
