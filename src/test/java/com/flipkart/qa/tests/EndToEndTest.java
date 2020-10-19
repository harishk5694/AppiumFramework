package com.flipkart.qa.tests;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.flipkart.qa.util.Data;
import com.flipkart.qa.util.DataProviderClass;
import com.flipkart.qa.util.TestParams;
import com.flipkart.qa.base.Connection;
import com.flipkart.qa.pages.HomePage;
import com.flipkart.qa.pages.LoginPage;
import com.flipkart.qa.pages.MyCartPage;
import com.flipkart.qa.pages.ProductDetailsPage;
import com.flipkart.qa.pages.ProductsPage;


/**
 * This class contains end to end test for flipkart application
 * 
 * @author Harish K
 *
 */
@Listeners(com.flipkart.qa.listeners.AppiumTestListener.class)  
public class EndToEndTest extends Connection{
	protected final Logger logger = LogManager.getLogger(getClass());
	private static final String FILE_PATH="testdata/EndToEndTest.xml";
	private static final String SET="EndToEnd";
	private static final String TEST="EndToEnd";
	public static String userName;
	public static String password;
	
	@BeforeClass
	public void loadProperties() {
		userName=(String) props.get("UserName");
		password=(String) props.get("Password");	
	}
	
	@Data(filePath = FILE_PATH, set = SET)
	@Test(enabled = true, testName = TEST, dataProvider = "xml", dataProviderClass = DataProviderClass.class)
	public void testEndToEndScenario(TestParams param) throws Exception {
		List<String> filterValues=param.get("FilterValues");
		
		logger.info("About to login and navigate to home page");
		LoginPage logPage=new LoginPage();
		HomePage homPage=logPage.login(param.get("AppLanguage"), userName, password);
		Assert.assertEquals(homPage.isElementDisplayed(HomePage.cartButtonBy,"Home Page"), true,"Home Page Not Displayed");
		
		logger.info("About to search for product and apply filter and sort criterias");
		ProductsPage prodsPage=homPage.searchForProduct(param.get("ProductName"));
		Assert.assertEquals(prodsPage.isElementDisplayed(ProductsPage.filterButtonBy,"Products Page"), true,"Search results Not Displayed");
		prodsPage.appyFilter(param.get("FilterType"), filterValues);	
		prodsPage.applySorting(param.get("SortingCriteria"));
		
		logger.info("About to select 1st product and select size");
		ProductDetailsPage prodDetsPage=prodsPage.selectNthProductFromSearchResults(1);
		Assert.assertEquals(prodDetsPage.isElementDisplayed(ProductDetailsPage.addToCartButtonBy,"Product Details Page"), true,"Product Details Page Not Displayed");
		String prodPrice=prodDetsPage.getProductPrice();
		prodDetsPage.addProductToCart();
		if(!prodDetsPage.selectSize(param.get("ProductSize"))) {
			Assert.assertFalse(true,"Desired size not available!!");
		}
		prodDetsPage.clickOnContinue();
		
		logger.info("About to navigate to my cart page and validate price");
		MyCartPage myCart=prodDetsPage.goToCart();
		Assert.assertEquals(myCart.isElementDisplayed(MyCartPage.placeOrderButtonBy,"My Cart Page"), true,"My Cart Page Not Displayed");
		String deliveryCharges=myCart.getDeliveryCharges();
		Integer totlAmt=Integer.valueOf(prodPrice)+Integer.valueOf(deliveryCharges);
		Assert.assertEquals(myCart.getTotalAmout(), String.valueOf(totlAmt),"Expected and actual total amount doesn't match");	
	}
}
