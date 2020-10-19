package com.flipkart.qa.pages;

import org.openqa.selenium.By;

import com.flipkart.qa.util.CommonUtils;

/**
 * This class contains locators and methods for my cart page
 * 
 * @author Harish K
 *
 */
public class MyCartPage extends CommonUtils {
	public static By placeOrderButtonBy = By.xpath("//*[contains(@text,'Place Order')]");

	private static By totalAmountBy = By.xpath("//*[contains(@text,'Total Amount')]");

	private static By deliveryChargesBy = By.xpath("//*[contains(@text,'Delivery Charges')]");
	
	private static By deliveryChargesValueBy = By.xpath("//*[contains(@text,'Delivery Charges')]/following-sibling::*[contains(@text,'₹')]");

	private static By totalAmountValueBy = By.xpath("//*[contains(@text,'Total Amount')]/following-sibling::*[contains(@text,'₹')]");

	/**
	 * Returns total amount
	 * 
	 * @return
	 */
	public String getTotalAmout() {
		swipeIntoView(totalAmountBy, "Total Amount ", 0.5, 0.9, 0.5, 0.5, 2, true);
		String totalAmount = getAttribute(totalAmountValueBy, "text", "Total Amount label");
		return totalAmount.split("₹")[1].trim();
	}

	/**
	 * Returns delivery charges
	 * 
	 * @return
	 */
	public String getDeliveryCharges() {
		swipeIntoView(deliveryChargesBy, "Delivery Charges ", 0.5, 0.9, 0.5, 0.5, 2, true);
		String delCharge = getAttribute(deliveryChargesValueBy, "text", "Delivery charges label");
		return delCharge.split("₹")[1].trim();
	}
}
