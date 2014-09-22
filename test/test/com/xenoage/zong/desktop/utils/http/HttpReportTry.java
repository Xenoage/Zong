package com.xenoage.zong.desktop.utils.http;

import java.io.IOException;

/**
 * Test cases for the {@link HttpReport} class.
 * 
 * @author Andreas Wenger
 */
public class HttpReportTry {

	public static void main(String... args) {
		//send report with two strings
		HttpReport report = new HttpReport();
		report.registerData("info.txt", "First text");
		report.registerData("data.txt", "Second text");
		try {
			report.send();
			System.out.println("OK");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
