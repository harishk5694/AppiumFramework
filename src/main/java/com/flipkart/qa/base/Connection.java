package com.flipkart.qa.base;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

/**
 * Class to start appium server and instantiate driver
 * 
 * @author Harish K
 *
 */
public class Connection {
	private static java.lang.Process process;
	protected final Logger logger = LogManager.getLogger(getClass());
	public static URL url;
	public static DesiredCapabilities capabilities;
	public static AppiumDriver<MobileElement> driver;
	private AppiumDriverLocalService service;
	private AppiumServiceBuilder builder;
	private static final String PROPERTIES_FILE = "Config.properties";
	public static InputStream inputStream;
	public static Properties props;
	

	@BeforeSuite
	public void startAppiumService() throws Exception {
		props = new Properties();
		inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE);
		props.load(inputStream);
		
		HashMap<String, String> environment = new HashMap<String, String>();

		// Build the Appium service
		builder = new AppiumServiceBuilder();
		builder.withIPAddress(props.getProperty("HostIp"));
		builder.usingPort(Integer.valueOf(props.getProperty("Port")));
		builder.withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"));
		builder.usingDriverExecutable(new File("/usr/local/bin/node"));
		builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
		builder.withArgument(GeneralServerFlag.LOG_LEVEL, "error");
		builder.withEnvironment(environment);
		builder.withLogFile(new File("ServerLogs/server.log"));

		// Start the server with the builder
		service = AppiumDriverLocalService.buildService(builder);

		if (!checkIfServerIsRunnning(Integer.valueOf(props.getProperty("Port")))) {
			try{
				service.start();
				service.clearOutPutStreams();
				logger.info("Appium server started on port:"+Integer.valueOf(props.getProperty("Port")));
			}
			catch(Exception e) {
				logger.error("Exception occured while starting appium server");
				e.printStackTrace();
				throw new Exception();
			}
			
		} else {
			service.stop();
			logger.info("Appium server is already running on port:"+Integer.valueOf(props.getProperty("Port")));
		}
	}

	@BeforeMethod
	public void initializeDriver() throws Exception {
		final String URL_STRING = "http://"+props.get("HostIp")+":"+props.get("Port")+"/wd/hub";
		url = new URL(URL_STRING);	
		capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.UDID, getDeviceID());
		capabilities.setCapability("appPackage", props.get("AppPackage"));
		capabilities.setCapability("appActivity", props.get("AppActivity"));
		capabilities.setCapability("autoGrantPermission", true);
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, props.get("PlatformName"));
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, props.get("AutomationName"));

		try{
			driver = new AppiumDriver<MobileElement>(url, capabilities);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			logger.info("Appium driver instantiated successfully");
		}
		catch(Exception e) {
			stopServer();
			logger.error("Exception occured while instantiating appium driver");
			e.printStackTrace();
			throw new Exception();
		}	
	}

	@AfterMethod
	public void afterTest() {
		driver.quit();
	}

	@AfterSuite
	public void afterSuite() {
		stopServer();
		logger.info("Appium server stopped");
	}

	public void stopServer() {
		service.stop();
	}

	public boolean checkIfServerIsRunnning(int port) {

		boolean isServerRunning = false;
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(port);
			serverSocket.close();
		} catch (IOException e) {
			// If control comes here, then it means that the port is in use
			isServerRunning = true;
		} finally {
			serverSocket = null;
		}
		return isServerRunning;
	}

	/**
	 * getDeviceID
	 */
	private String getDeviceID() {
		String deviceID = "";
		try {
			if (System.getProperty("os.name").contains("Windows")) {
				java.lang.Runtime runTime = java.lang.Runtime.getRuntime();
				process = runTime.exec("adb devices");
			} else if (System.getProperty("os.name").contains("Mac")) {
				process = Runtime.getRuntime().exec(new String[] { "bash", "-l", "-c", "adb devices" });
			}

			// Get process output: its InputStream
			java.io.InputStream is = process.getInputStream();
			java.io.BufferedReader reader = new java.io.BufferedReader(new InputStreamReader(is));

			// dummy read to skip the unwanted string
			reader.readLine();

			deviceID = reader.readLine().split("\t")[0];
		} catch (IOException e) {
			e.printStackTrace();
		}

		return deviceID;
	}
}
