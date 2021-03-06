package com.shopme.admin.user;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.common.entity.User;

public class UserCSVExporter {

	public void export(List<User> listUser, HttpServletResponse response) throws IOException {
		
		DateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String timestamp = dateformatter.format(new Date());		
		String fileName = "users_" + timestamp + ".csv";
		
		response.setContentType("text/csv");
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;
		
		response.setHeader(headerKey, headerValue);
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),	CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = {"User ID", "Email", "First Name", "Last Name", "Roles", "Enabled"};		
		csvWriter.writeHeader(csvHeader);
		
		
		String[] fielsMapping = {"id", "email", "firstName", "lastName", "roles", "enabled"};
		for(User user : listUser) {
			csvWriter.write(user, fielsMapping);
		}
		
		
		csvWriter.close();
		
	}
}
