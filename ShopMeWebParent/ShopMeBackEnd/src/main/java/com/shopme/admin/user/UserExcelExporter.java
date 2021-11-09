package com.shopme.admin.user;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.common.entity.User;

public class UserExcelExporter {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	
	public UserExcelExporter() {
		workbook = new XSSFWorkbook();		
	}

	public void export(List<User> userList, HttpServletResponse response) throws IOException {	

		setResponseHeader(response);		

		writeHeader();
		writeDataContent(userList);

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);

		workbook.close();
		outputStream.close();	
	}

	private void writeDataContent(List<User> userList) {
		int rowIndex = 1;		
		
		XSSFFont font = workbook.createFont();		
		font.setBold(false);
		font.setFontHeight(12);

		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFont(font);
		
		for(User user : userList) {
			XSSFRow row = sheet.createRow(rowIndex++);
			
			int columnIndex = 0;
			createCell(row, columnIndex++, user.getId(), cellStyle);
			createCell(row, columnIndex++, user.getEmail(), cellStyle);
			createCell(row, columnIndex++, user.getFirstName(), cellStyle);
			createCell(row, columnIndex++, user.getLastName(), cellStyle);
			createCell(row, columnIndex++, user.getRoles().toString(), cellStyle);
			createCell(row, columnIndex++, user.isEnabled(), cellStyle);
		}
		
	}

	private void writeHeader() {
		sheet = workbook.createSheet("Users");
		XSSFRow row = sheet.createRow(0);
		
		XSSFFont font = workbook.createFont();		
		font.setBold(true);
		font.setFontHeight(14);

		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFont(font);
		
		createCell(row, 0, "User Id", cellStyle);
		createCell(row, 1, "Email", cellStyle);
		createCell(row, 2, "First Name", cellStyle);
		createCell(row, 3, "Last Name", cellStyle);
		createCell(row, 4, "Roles", cellStyle);
		createCell(row, 5, "Enabled", cellStyle);
	}

	private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style) {

		XSSFCell cell = row.createCell(columnIndex);
		sheet.autoSizeColumn(columnIndex);
		
		if(value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue((String) value);
		}
		
		cell.setCellStyle(style);
	}
	
	
	private void setResponseHeader(HttpServletResponse response) {
		DateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String timestamp = dateformatter.format(new Date());		
		String fileName = "users_" + timestamp + ".xlsx";

		response.setContentType("application/octet-stream");

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;		
		response.setHeader(headerKey, headerValue);
	}

}
