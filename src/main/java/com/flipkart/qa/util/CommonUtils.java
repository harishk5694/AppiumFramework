package com.flipkart.qa.util;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.flipkart.qa.base.Connection;
import com.google.common.collect.ImmutableMap;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;

/**
 * This class common utility methods to perform actions on GUI objects
 * 
 * @author Harish K
 *
 */
public class CommonUtils extends Connection {
	protected final Logger logger = LogManager.getLogger(getClass());
	public static final long LONGPRESS_DURATION = 3;
	public static final long WAIT_FOR_ELEMENT = 20;
	public static final long WAIT_FOR_ELEMENT_INVISIBLE = 60;

	/**
	 * Perform long press on an element with duration
	 * 
	 * @param element
	 */
	public void longPress(MobileElement ele) {
		logger.info("Performing long press action");
		WebDriverWait wait = new WebDriverWait(driver, WAIT_FOR_ELEMENT);
		MobileElement element = (MobileElement) wait.until(ExpectedConditions.visibilityOf(ele));
		TouchAction<?> ta = new TouchAction<>(driver);
		ta.longPress(LongPressOptions.longPressOptions().withElement(ElementOption.element(element)).withDuration(Duration.ofSeconds(LONGPRESS_DURATION))).perform();
	}

	/**
	 * Scroll vertically down until the element is visible
	 * 
	 * @param elementText
	 */
	public void scrollIntoView(String elementText) {
		logger.info("Scrolling to view of the element with text: " + elementText);
		driver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector()).scrollIntoView(text(\"" + elementText + "\"))"));
	}

	/**
	 * Drags the source element to target element's position
	 * 
	 * @param source
	 * 
	 * @param target
	 */
	public void dragAndDraop(MobileElement source1, MobileElement target1) {
		logger.info("Performing drap and drop action");
		WebDriverWait wait = new WebDriverWait(driver, WAIT_FOR_ELEMENT);
		MobileElement source = (MobileElement) wait.until(ExpectedConditions.visibilityOf(source1));
		MobileElement target = (MobileElement) wait.until(ExpectedConditions.visibilityOf(target1));
		TouchAction<?> ta = new TouchAction<>(driver);
		ta.longPress(LongPressOptions.longPressOptions().withElement(ElementOption.element(source))).perform();
		ta.moveTo(new ElementOption().withElement(target)).perform();
	}

	/**
	 * Waits and clicks on element
	 * 
	 * @param By
	 * 
	 * @param name of element
	 */
	public void click(By by, String elementName) {
		WebDriverWait wait = new WebDriverWait(driver, WAIT_FOR_ELEMENT);
		MobileElement element = (MobileElement) wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(by)));		
		logger.info("Clicking on element: " + elementName);
		element.click();
	}
	
	/**
	 * Waits and enters text to the element
	 * 
	 * @param by
	 * 		-locator
	 * 
	 * @param text
	 * 		-text to enter
	 */
	public void setText(By by, String text, String elementName) {
		WebDriverWait wait = new WebDriverWait(driver, WAIT_FOR_ELEMENT);
		MobileElement element = (MobileElement) wait.until(ExpectedConditions.visibilityOf(driver.findElement(by)));
		logger.info("Entering text: '" + text + "' to : " + elementName);
		element.clear();
		element.sendKeys(text+" ");
	}
	
	/**
	 * Waits and fetches text from the element
	 * 
	 * @param by
	 * 		-locator
	 * 
	 * @param attribute
	 * 		-attribute value to be returned
	 * 
	 * @param elementName
	 * 		- name of element
	 */
	public String getAttribute(By by,String attribute, String elementName) {
		WebDriverWait wait = new WebDriverWait(driver, WAIT_FOR_ELEMENT);
		MobileElement element = (MobileElement) wait.until(ExpectedConditions.visibilityOf(driver.findElement(by)));
		logger.info("Fetching "+attribute+" from:" + elementName);
		return element.getAttribute(attribute);
	}

	/**
	 * Waits for element and clears text
	 * 
	 * @param ele
	 */
	public void clearText(MobileElement ele) {
		WebDriverWait wait = new WebDriverWait(driver, WAIT_FOR_ELEMENT);
		MobileElement element = (MobileElement) wait.until(ExpectedConditions.visibilityOf(ele));
		logger.info("Clearing text on element: " + ele.getAttribute("text"));
		element.clear();
	}


	/**
	 * Return to previous screen
	 */
	public void goBack() {
		driver.navigate().back();
	}

	
	public void pressTab() throws InterruptedException {

		((AndroidDriver<MobileElement>) driver).pressKey(new KeyEvent(AndroidKey.TAB));
		Thread.sleep(2000);
	}
	
	public void pressBackSpace() throws InterruptedException {
		driver.getKeyboard().pressKey(Keys.BACK_SPACE);
		Thread.sleep(2000);
	}

	public void pressEnter() throws InterruptedException {
		((AndroidDriver<MobileElement>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
		Thread.sleep(2000);
	}
	
	public void pressSearch() {
		driver.executeScript("mobile: performEditorAction", ImmutableMap.of("action", "search"));
	}
	
	/**
	 * Swipe until the element with given text is found
	 * 
	 * @param by -locator of element
	 * @param text - text of element till which swipe must be done
	 * @param start_x - Start X Position
	 * @param start_y - Start Y Position
	 * @param end_x   - End X Position
	 * @param end_y   - End Y Position
	 * @param duration - Duration In Seconds To Wait Before Performing Action
	 * @return - Boolean (True/False)
	 * @throws 
	 */
	public boolean swipeIntoView(By by,String text, double start_x, double start_y, double end_x, double end_y, int duration, boolean click)
	{
		int counter = 0;
		boolean flag = false;
		do
		{
			List<MobileElement> lists = driver.findElements(by);
			for(MobileElement e : lists)
			{
				if(e.getAttribute("text").equalsIgnoreCase(text))
				{
					flag = true;
					if(click) {
						e.click();
					}
					break;
				}
			}
			if (!flag)
				swipeVertically(start_x, start_y, end_x, end_y, duration);
			counter++;
		}
		while(!flag || counter==10);
		return flag;
	}
	
	/**
	 * Swipe vertically based on dimensions
	 * 
	 * @param start_xd - Start X Position
	 * @param start_yd - Start Y Position
	 * @param end_xd   - End X Position
	 * @param end_yd   - End Y Position
	 * @param duration - Duration In Seconds To Wait Before Performing Action
	 * @example - example : swipe up : 0.5, 0.8, 0.5, 0.2 and swipe down: 0.5, 0.2, 0.5, 0.2
	 */
	@SuppressWarnings("rawtypes")
	public void swipeVertically(double start_xd, double start_yd, double end_xd, double end_yd, int duration) 
	{
		Dimension size = driver.manage().window().getSize();
		int start_x = (int)(size.width*start_xd);
		int start_y = (int)(size.height*start_yd);
		int end_x = (int)(size.width*end_xd);
		int end_y = (int)(size.height*end_yd);
		new TouchAction(driver)
		.press(PointOption.point(start_x, start_y))
		.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(duration)))
		.moveTo(PointOption.point(end_x, end_y)).release().perform();
	}
	
	
	public boolean isPageDisplayed(By by, String pageName) throws Exception{
		boolean found=false;
		try {
			
			WebDriverWait wait = new WebDriverWait(driver, WAIT_FOR_ELEMENT);
			MobileElement element = (MobileElement) wait.until(ExpectedConditions.visibilityOf(driver.findElement(by)));
			if(element.isDisplayed()) {
				found=true;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return found;	
	}
	
	public boolean isElementDisplayed(By by, String elementName) throws Exception{
		boolean found=false;
		try {
			
			WebDriverWait wait = new WebDriverWait(driver, WAIT_FOR_ELEMENT);
			MobileElement element = (MobileElement) wait.until(ExpectedConditions.visibilityOf(driver.findElement(by)));
			if(element.isDisplayed()) {
				found=true;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return found;	
	}
	
	/**
	 * Hides the keyboard which is displayed
	 */
	public void hideKeyBoard() {
		driver.hideKeyboard();
	}
	
	/**
	 * Brings keyboard to forefront
	 */
	public void showKeyBoard() {
		driver.getKeyboard();
	}
	
	
	/**
	 * Returns list of elements for a given locator
	 * @param by
	 * @return
	 */
	public List<MobileElement> findElements(By by){
		WebDriverWait wait = new WebDriverWait(driver, WAIT_FOR_ELEMENT);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(by)));		
		List<MobileElement> elementList=driver.findElements(by);
		return elementList;
	}
	
	/**
	 * Get the coordinates of element, add and tap on drop down element coordinates
	 * 
	 * @param x
	 * @param y
	 */
	@SuppressWarnings("rawtypes")
	public void tapOnFirstDropDown(By by) {
		Point point=driver.findElement(by).getLocation();
		int x=point.x+200;
		int y=point.y+200;
		(new TouchAction(driver)).tap(TapOptions.tapOptions().withPosition(PointOption.point(x, y))).perform();
	}
	
	/**
	 * Wait till element is no more visible
	 * 
	 * @param by-Locator of element
	 */
	public void waitTillElementBecomesInvisible(By by) {
		WebDriverWait wait = new WebDriverWait(driver, WAIT_FOR_ELEMENT_INVISIBLE);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(by));	
	}
	
	/**
	 * Returns true if element is present
	 * 
	 * @param by-Locator of element under test
	 * 
	 * @return
	 */
	public boolean isElementPresent(By by, String elementName) {
		boolean exists=false;
		try {
			exists =driver.findElements(by).size()!=0;
			return exists;
		}
		catch(Exception e) {
			logger.info("Element not found: "+elementName);
		}
		return exists;
	}

}
