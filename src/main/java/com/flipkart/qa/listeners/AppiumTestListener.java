package com.flipkart.qa.listeners;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.flipkart.qa.base.Connection;
import com.flipkart.qa.reports.ExtentReport;

/**
 * Listener class for tests
 * 
 * @author Harish K
 *
 */
public class AppiumTestListener extends Connection implements ITestListener {
	protected final Logger logger = LogManager.getLogger(getClass());

	public void onTestFailure(ITestResult result) {
		if (result.getThrowable() != null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			result.getThrowable().printStackTrace(pw);
			logger.error(sw.toString());
		}

		File file = driver.getScreenshotAs(OutputType.FILE);

		byte[] encoded = null;
		try {
			encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String imagePath = "" + result.getTestClass().getRealClass().getSimpleName() + "_" + result.getName() + ".png";

		File fle = new File("Reports" + File.separator + "Screenshots" + File.separator + imagePath);

		File destination = new File(fle.getAbsolutePath());
		try {
			FileHandler.copy(file, destination);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ExtentReport.getTest().fail("Test Failed", MediaEntityBuilder.createScreenCaptureFromPath(fle.getAbsolutePath()).build());
		ExtentReport.getTest().fail("Test Failed", MediaEntityBuilder
				.createScreenCaptureFromBase64String(new String(encoded, StandardCharsets.US_ASCII)).build());
		ExtentReport.getTest().fail(result.getThrowable());
	}

	@Override
	public void onTestStart(ITestResult result) {
		ExtentReport.startTest(result.getName(), result.getMethod().getDescription());
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		ExtentReport.getTest().log(Status.PASS, "Test Passed");
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		ExtentReport.getTest().log(Status.SKIP, "Test Skipped");

	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFinish(ITestContext context) {
		ExtentReport.getReporter().flush();
	}

	public String dateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static void logStepAndCaptureScreenshot(String status, String description, String screenshotName){
		File file = driver.getScreenshotAs(OutputType.FILE);

		byte[] encoded = null;
		try {
			encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String imagePath = "" + screenshotName+ ".png";

		File fle = new File("Reports" + File.separator + "Screenshots" + File.separator + imagePath);

		File destination = new File(fle.getAbsolutePath());
		try {
			FileHandler.copy(file, destination);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		switch(status){
		case "PASS":
			ExtentReport.getTest().log(Status.PASS, description , MediaEntityBuilder
					.createScreenCaptureFromBase64String(new String(encoded, StandardCharsets.US_ASCII)).build());
			break;
		}	
	}

}
