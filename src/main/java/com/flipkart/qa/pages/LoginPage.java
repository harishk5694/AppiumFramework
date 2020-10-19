package com.flipkart.qa.pages;

import java.util.List;

import org.openqa.selenium.By;
import com.flipkart.qa.util.CommonUtils;
import io.appium.java_client.MobileElement;

/**
 * This class contains locators and methods for login page
 * 
 * @author Harish K
 *
 */
public class LoginPage extends CommonUtils {

	private static By languagesButtonsBy = By.id("com.flipkart.android:id/tv_subtext");
	
	private static By continueButtonBy = By.id("com.flipkart.android:id/select_btn");
	
	private static By continuePhoneBy = By.xpath("//*[@text='Continue']");
	
	private static By noneOfAboveBy = By.id("com.google.android.gms:id/cancel");

	private static By phoneNumberFieldBy = By.id("com.flipkart.android:id/phone_input");
	
	private static By useEmailLinkBy = By.id("com.flipkart.android:id/tv_right_cta");
	
	
	
	

	/**
	 * Selects app language
	 * 
	 * @param language
	 * @throws Exception
	 */
	public void selectLanguage(String language) throws Exception {
		boolean found = false;
		String languageText;
		List<MobileElement> languageOptions = findElements(languagesButtonsBy);
		for (MobileElement option : languageOptions) {
			languageText = option.getAttribute("text");
			if (languageText.trim().equalsIgnoreCase(language)) {
				found = true;
				option.click();
				break;
			}
		}
		if (!found) {
			throw new Exception("City not found");
		}

		click(continueButtonBy, "Continue Button");
	}

	/**
	 * 
	 * @param appLanguage
	 * @param mobileNumber
	 * @return
	 * @throws Exception
	 */
	public HomePage login(String appLanguage, String email, String password) throws Exception {
		selectLanguage(appLanguage);
		if (isElementPresent(noneOfAboveBy, "None Of Above")) {
			click(noneOfAboveBy, "None Of Above button");
		}
		click(useEmailLinkBy, "Use EMail id link");
		setText(phoneNumberFieldBy, email, "Email Field");
		pressBackSpace();
		click(continuePhoneBy, "Continue Button");
		setText(phoneNumberFieldBy, password, "Pasword Field");
		pressBackSpace();
		click(continuePhoneBy, "Continue Button");
		return new HomePage();	
	}

}
