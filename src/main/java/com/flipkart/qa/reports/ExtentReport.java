package com.flipkart.qa.reports;

import java.util.HashMap;
import java.util.Map;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * Class needed for extent report creation
 * 
 * @author Harish K
 *
 */
public class ExtentReport {
	static ExtentReports extent;
	final static String filePath = "Reports/Extent.html";

	static Map<Integer, ExtentTest> extentTestMap = new HashMap<Integer, ExtentTest>();

	public synchronized static ExtentReports getReporter() {
		if (extent == null) {
			ExtentSparkReporter html = new ExtentSparkReporter(filePath);
			html.config().setDocumentTitle("NoBrokerTestSuite");
			html.config().setReportName("NoBroker");
			html.config().setTheme(Theme.DARK);
			extent = new ExtentReports();
			extent.attachReporter(html);
		}

		return extent;
	}

	public static synchronized ExtentTest getTest() {
		return (ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId()));
	}

	public static synchronized ExtentTest startTest(String testName, String desc) {
		ExtentTest test = getReporter().createTest(testName, desc);
		extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);
		return test;
	}
}
