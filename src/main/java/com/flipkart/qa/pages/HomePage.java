package com.flipkart.qa.pages;

import org.openqa.selenium.By;

import com.flipkart.qa.util.CommonUtils;

/**
 * This class contains locators and methods for home page
 * 
 * @author Harish K
 *
 */
public class HomePage extends CommonUtils{
	public static By cartButtonBy = By.id("com.flipkart.android:id/cart_bg_icon");
	
	private static By searchBoxBy = By.id("com.flipkart.android:id/search_widget_textbox");
	
	private static By notNowButtonBy = By.id("com.flipkart.android:id/not_now_button");
	
	private static By searchDialogFieldBy = By.id("com.flipkart.android:id/search_autoCompleteTextView");
	
	
	public ProductsPage searchForProduct(String productName) throws InterruptedException {
		click(searchBoxBy,"Home Page Search Box");	
		setText(searchDialogFieldBy,productName,"Seach Field");
		pressSearch();
		if (isElementPresent(notNowButtonBy, "Not Now button")) {
			click(notNowButtonBy, "Not Now button");
		}
		return new ProductsPage();
	}
}
