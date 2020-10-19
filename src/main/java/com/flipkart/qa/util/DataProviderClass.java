package com.flipkart.qa.util;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.DataProvider;

import com.flipkart.qa.base.Connection;


/**
 * Data provider class for tests
 * 
 * @author Harish K
 *
 */
public class DataProviderClass {
	private static final Logger logger = LogManager.getLogger(DataProviderClass.class);

	private DataProviderClass() {}

	/**
	 * Method invoked when using the TestNG @Test annotation <br/>
	 * dataProvider = "xml", dataProviderClass = DataProviderClass.class
	 * 
	 * @param m
	 * @return
	 * @throws Exception 
	 */
	@DataProvider
	public static Object[][] xml(Method m) throws Exception {
		List<String> data = extractDataAnnotationInfo(m);
		String path = data.get(0);
		String set = data.get(1);
		logger.trace(String.format("Data Annotation(path:'%s' set:'%s')", path, set));
		ClassLoader classLoader = Connection.class.getClassLoader();
		URL fileUrl = classLoader.getResource(path);
		path = fileUrl.getPath();
		// Parse xml data to TestParams
		List<TestParams> parameters = XmlParmParser.parse(path, set);
		logger.trace("Completed XML Parameter Parse");
		return convertToObjectArrayOfArrays(parameters);
	}

	private static Object[][] convertToObjectArrayOfArrays(List<TestParams> parameters) {
		Object[][] arr = new Object[parameters.size()][];
		for (int i = 0; i < parameters.size(); i++) {
			arr[i] = new Object[] { parameters.get(i) };
		}

		logger.trace("Converted Parameters to Parameter Array");
		return arr;
	}

	 /**
     * Extract values out of the annotation<br />
     * Allows for filePath and set to be overriden with System Properties
     * 
     * @param m
     * @return
     */
    private static List<String> extractDataAnnotationInfo(Method m) {
        Data annotation = m.getAnnotation(Data.class);
        String filePath = System.getProperty("data.filePath", annotation.filePath());
        String set = System.getProperty("data.set", annotation.set());
        return Arrays.asList(filePath, set);
    }
}
