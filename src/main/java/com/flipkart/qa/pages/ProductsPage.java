package com.flipkart.qa.pages;

import java.util.List;

import org.openqa.selenium.By;

import com.flipkart.qa.util.CommonUtils;

import io.appium.java_client.MobileElement;

/**
 * This class contains locators and methods for products page
 * 
 * @author Harish K
 *
 */
public class ProductsPage extends CommonUtils {
	public static By filterButtonBy = By.xpath("//*[@text='Filter']");

	private static By sortButtonBy = By.xpath("//*[@text='Sort']");

	private static By applyButtonBy = By.xpath("//*[@text='Apply']");

	private static By allFilterTypesBy = By.xpath("//android.widget.TextView");

	/**
	 * Applies filter based on filter criteria
	 * 
	 * @param filterType
	 * 
	 * @param filterValues
	 * @throws InterruptedException
	 */
	public void appyFilter(String filterType, List<String> filterValues) {
		String filterText;
		click(filterButtonBy, "Filter Button");
		List<MobileElement> filterTypes = findElements(allFilterTypesBy);
		for (MobileElement option : filterTypes) {
			filterText = option.getAttribute("text");
			if (filterText.equalsIgnoreCase(filterType)) {
				option.click();
				break;
			}
		}

		for (String val : filterValues) {
			swipeIntoView(allFilterTypesBy, val, 0.5, 0.9, 0.5, 0.5, 2, true);
		}

		click(applyButtonBy, "Apply Button");
	}

	/**
	 * Applies sorting based on criteria
	 * 
	 * @param sortCriteria
	 * @throws InterruptedException
	 */
	public void applySorting(String sortCriteria) {
		String sortText;
		click(sortButtonBy, "Sort Button");
		List<MobileElement> filterTypes = findElements(allFilterTypesBy);
		for (MobileElement option : filterTypes) {
			sortText = option.getAttribute("text");
			if (sortText.equalsIgnoreCase(sortCriteria)) {
				option.click();
				break;
			}
		}
	}

	/**
	 * Selects the first product from search results
	 * 
	 * @throws InterruptedException
	 * 
	 */
	public ProductDetailsPage selectNthProductFromSearchResults(int n) {
		List<MobileElement> products = driver.findElements(By.xpath("//android.widget.ImageView"));
		for (int i = 0; i < products.size(); i++) {
			if (i == n - 1) {
				products.get(i).click();
				break;
			}
		}
		return new ProductDetailsPage();
	}

}
