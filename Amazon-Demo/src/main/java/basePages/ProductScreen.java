package basePages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.relevantcodes.extentreports.ExtentTest;

import bindings.AmazonBind;
import io.appium.java_client.MobileDriver;
import basePages.ProductScreen;
import bindings.GeneralBind;

public class ProductScreen extends AmazonBind {

	private static Properties prop;
	public Map<String, String> capData1 = new HashMap<String, String>();

	public ProductScreen(MobileDriver driver, ExtentTest test, Map<String, String> capData1) {
		this.driver = driver;
		this.test = test;
		this.capData1 = capData1;
		prop = new Properties();
		try {
			if (capData1.get("PlatformName").equalsIgnoreCase("Android")) {
				prop.load(new FileInputStream(new File("./Locators/Android/pdp.properties")));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//SEARCH PRODUCT
	public ProductScreen validateSearchResultPage() throws InterruptedException {

		swipeFullFromBottomToTop("android");

		GeneralBind.productNameSearchResults=getText(prop.getProperty("text.prodSearchPageTitle"));
		String[] fullPrice=getText(prop.getProperty("text.prodSearchPagePrice")).split(" ");
		System.out.println("price full : "+fullPrice[0]);
		System.out.println("name full : "+GeneralBind.productNameSearchResults);

		if(!fullPrice[0].contains("₹"))
		{
			org.testng.Assert.assertTrue(false,"₹ not present in search results page");
		}
		GeneralBind.productPriceSearchResults=fullPrice[0];

		verifyStep("Product Name search page :"+GeneralBind.productNameSearchResults, "INFO");
		verifyStep("Product Price search page :"+GeneralBind.productPriceSearchResults, "INFO");

		click(prop.getProperty("text.prodSearchPageTitle"));
		return this;
	}

	public ProductScreen validateProductScreen() {		
		verifyElementIsDisplayed(prop.getProperty("text.productTitle"));
		verifyElementIsDisplayed(prop.getProperty("image.productReview"));
		verifyElementIsDisplayed(prop.getProperty("button.shareProduct"));
		verifyElementIsDisplayed(prop.getProperty("button.ProductImage"));

		verifyStep("Product details page displayed", "PASS");

		String pScreenProductTitle=getText(prop.getProperty("text.productTitle"));
		verifyStep("Product name PDP page : "+pScreenProductTitle, "INFO");

		org.testng.Assert.assertEquals(pScreenProductTitle, GeneralBind.productNameSearchResults);

		swipeFullFromBottomToTop("android");

		if(verifyIsDisplayed(prop.getProperty("text.pdpPageSavingsPrice")))
		{
			GeneralBind.offerProduct=true;
		}

		String pScreenPrice=getText(prop.getProperty("text.productPrice"));
		pScreenPrice=pScreenPrice.replace("rupees ", "₹");
		verifyStep("Product price PDP page : "+pScreenPrice, "INFO");

		org.testng.Assert.assertEquals(pScreenPrice, GeneralBind.productPriceSearchResults);

		swipeToElement("android", prop.getProperty("button.wishListButton"));

		if(!verifyIsDisplayed(prop.getProperty("button.addtoCartButton")))
		{
			swipeToElementUpwards("android", prop.getProperty("button.oneTimePurchase"));
			click(prop.getProperty("button.oneTimePurchase"));
		}

		swipeToElement("android", prop.getProperty("button.addtoCartButton"));

		GeneralBind.productQuantity=Integer.parseInt(getText(prop.getProperty("text.quantityDropdown")));

		verifyStep("Product quantity : "+GeneralBind.productQuantity, "INFO");

		verifyElementIsDisplayed(prop.getProperty("button.addtoCartButton"));

		return this;
	}

	public ProductScreen addToCart() throws InterruptedException {

		verifyElementIsDisplayed(prop.getProperty("text.cartCount"));
		int cartCountBefore=Integer.parseInt(getText(prop.getProperty("text.cartCount")));

		click(prop.getProperty("button.addtoCartButton"));
		
		verifyElementIsDisplayed(prop.getProperty("text.cartCount"));
		int cartCountAfter=Integer.parseInt(getText(prop.getProperty("text.cartCount")));

		if(cartCountBefore+GeneralBind.productQuantity==cartCountAfter)
		{
			System.out.println("Product added to cart");
		}

		return this;
	}

	public ProductScreen navigateToShoppingCart() {

		verifyElementIsDisplayed(prop.getProperty("button.cartButton"));
		click(prop.getProperty("button.cartButton"));

		return this;
	}

}
