package com.flipkart.qa.pages;

import java.util.List;

import org.openqa.selenium.By;

import com.flipkart.qa.util.CommonUtils;

import io.appium.java_client.MobileElement;

/**
 * This class contains locators and methods for product details page
 * 
 * @author Harish K
 *
 */
public class ProductDetailsPage extends CommonUtils {

	public static By addToCartButtonBy = By.xpath("//*[@text='ADD TO CART']");
	
	private static By priceLabelBy = By.xpath("(//*[contains(@text,'₹')])[1]");
	
	private static By goToCartButtonBy = By.xpath("//*[contains(@text,'GO TO CART')]");

	private static By allSizesBy = By.xpath("//android.widget.TextView");

	private static By continueSizeBy = By.xpath("//*[@text='CONTINUE']");

	public void addProductToCart() throws InterruptedException {
		click(addToCartButtonBy, "Add To Cart");
		Thread.sleep(2000);
	}

	/**
	 * Selects size for product
	 * 
	 * @param size
	 * @throws Exception
	 */
	public boolean selectSize(String size) throws Exception {
		boolean found = false;
		String sizeText;
		List<MobileElement> sizes = findElements(allSizesBy);
		for (MobileElement option : sizes) {
			sizeText = option.getAttribute("text");
			if (sizeText.equals(size)) {
				found=true;
				option.click();
				List<MobileElement> sizeMsgs = findElements(allSizesBy);
				for (MobileElement msg : sizeMsgs) {
					if (msg.getText().contains("out of stock")) {
						found = false;
						break;
					}
				}
				break;
			}
		}
		return found;	
	}
	
	/**
	 * Go to cart
	 * 
	 * @return
	 */
	public MyCartPage goToCart() {
		click(goToCartButtonBy,"Go To Cart");
		return new MyCartPage();
	}
	
	/**
	 * Click on continue
	 */
	public void clickOnContinue() {
		click(continueSizeBy, "Continue Button");
	}
	
	/**
	 * Fetch product price
	 * 
	 */
	public String getProductPrice() {
		String totalAmount = getAttribute(priceLabelBy, "text", "Price label");
		return totalAmount.split("₹")[1].trim();
	}
}
