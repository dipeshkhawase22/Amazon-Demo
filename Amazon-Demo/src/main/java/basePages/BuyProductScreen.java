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
import basePages.BuyProductScreen;
import utilities.ExcelMaker;
import bindings.GeneralBind;

public class BuyProductScreen extends AmazonBind{
	
	private static Properties prop;
	public Map<String, String> capData1 = new HashMap<String, String>();

	
	public BuyProductScreen(MobileDriver driver, ExtentTest test, Map<String, String> capData1) {
		this.driver = driver;
		this.test = test;
		this.capData1 = capData1;
		prop = new Properties();
		try {
			if (capData1.get("PlatformName").equalsIgnoreCase("Android")) {
				prop.load(new FileInputStream(new File("./Locators/Android/checkoutPage.properties")));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//PRICE VALIDATION
	public BuyProductScreen validateShoppingCart() {
		
		verifyElementIsDisplayed(prop.getProperty("text.productNameShoppingCart"));
		verifyStep("Shopping cart page displayed", "PASS");
		
		String prodNameShopCart=getText(prop.getProperty("text.productNameShoppingCart"));
		verifyStep("Product name shopping cart : "+prodNameShopCart, "INFO");

		verifyElementIsDisplayed(prop.getProperty("text.productPriceShoppingCart"));
		String[] prodPriceShopCartFull=getText(prop.getProperty("text.productPriceShoppingCart")).split("\\.");
		prodPriceShopCartFull[0] = prodPriceShopCartFull[0].replaceAll("[^a-zA-Z0-9]", "");
		String prodPriceShopCart="₹"+(prodPriceShopCartFull[0].trim());
		
		verifyStep("Product price shopping cart : "+prodPriceShopCart, "INFO");
		
		verifyElementIsDisplayed(prop.getProperty("image.productImageShoppingCart"));
		verifyElementIsDisplayed(prop.getProperty("text.subTotalShoppingCart"));
		String[] subTotalprodPriceShopCartfull=getText(prop.getProperty("text.subTotalShoppingCart")).split("\\.");
		subTotalprodPriceShopCartfull[0] = subTotalprodPriceShopCartfull[0].replaceAll("[^a-zA-Z0-9]", "");
		String subTotalprodPriceShopCart="₹"+subTotalprodPriceShopCartfull[0];
		verifyElementIsDisplayed(prop.getProperty("text.quantityShoppingCart"));
		int prodQuant=Integer.parseInt(getText(prop.getProperty("text.quantityShoppingCart")));
		
		verifyStep("Product quantity shopping cart : "+prodQuant, "INFO");
		
		org.testng.Assert.assertEquals(prodPriceShopCart, GeneralBind.productPriceSearchResults);

		org.testng.Assert.assertEquals(subTotalprodPriceShopCart, GeneralBind.productPriceSearchResults);

		org.testng.Assert.assertEquals(prodQuant, GeneralBind.productQuantity);

		return this;
	}
	
	
	public BuyProductScreen proceedtoBuy() {
		
		verifyElementIsDisplayed(prop.getProperty("button.proceedToBuy"));
		click(prop.getProperty("button.proceedToBuy"));
	
		return this;
	}
	
	
	public BuyProductScreen selectShippingAddress() {
		
		verifyElementIsDisplayed(prop.getProperty("text.ShippingAddress"));
		String address=getText(prop.getProperty("text.ShippingAddress"));
		System.out.println("address : "+address);
		
		verifyElementIsDisplayed(prop.getProperty("button.deliverButton"));
		click(prop.getProperty("button.deliverButton"));

		verifyElementIsDisplayed(prop.getProperty("button.continueButton"));
		click(prop.getProperty("button.continueButton"));

		

		return this;
	}
	
	public BuyProductScreen selectPayment() {
		ExcelMaker excelFetch = new ExcelMaker();
		Map<String, String> cvvData = excelFetch.getDataFromExcel("Login_And_Place_Order_TC01", "account1");
		
		
		swipeFullFromBottomToTop("android");		

		if(!verifyIsDisplayed(prop.getProperty("edit.cVVEnterField")))
		{
			verifyElementIsDisplayed(prop.getProperty("text.paymentCardSelect"));
			click(prop.getProperty("text.paymentCardSelect"));
			
		}
		
		verifyElementIsDisplayed(prop.getProperty("edit.cVVEnterField"));
		enterText(prop.getProperty("edit.cVVEnterField"), cvvData.get("Cvv"));
		
		swipeToElement("android",prop.getProperty("button.continueButton"));
		
		verifyElementIsDisplayed(prop.getProperty("button.continueButton"));
		click(prop.getProperty("button.continueButton"));


		return this;
	}

	//SKIP PRIME MEMBERSHIP PAGE
	public BuyProductScreen selectPrimeMember() throws InterruptedException {
		
		if(verifyIsDisplayed(prop.getProperty("button.NoThanksButton")))
		{
			click(prop.getProperty("button.NoThanksButton"));
		}
	
		return this;
	}
	
	//VALIDATE PRICE BREAKDOWN
	public BuyProductScreen validateCheckoutPage() throws InterruptedException {
		
		verifyStep("Checkout page displayed", "PASS");
		swipeFullFromBottomToTop("android");

		verifyElementIsDisplayed(prop.getProperty("price.checkoutTotalPrice"));
		String totalPriceFull=getText(prop.getProperty("price.checkoutTotalPrice"));
		totalPriceFull=totalPriceFull.replaceAll("[^a-zA-Z0-9]", "");
		int totalPrice=Integer.parseInt(totalPriceFull);

		verifyElementIsDisplayed(prop.getProperty("price.checkoutBreakdownProductPrice"));
		String breaqkdownprodfullprice=getText(prop.getProperty("price.checkoutBreakdownProductPrice"));
		breaqkdownprodfullprice=breaqkdownprodfullprice.replaceAll("[^a-zA-Z0-9]", "");
		int prodPrice=Integer.parseInt(breaqkdownprodfullprice);
		verifyElementIsDisplayed(prop.getProperty("price.checkOutDeliveryPrice"));
		String deliveryFullPrice=getText(prop.getProperty("price.checkOutDeliveryPrice"));
		deliveryFullPrice=deliveryFullPrice.replaceAll("[^a-zA-Z0-9]", "");
		int deliveryPrice=Integer.parseInt(deliveryFullPrice);

		int totalProdPriceCalc=deliveryPrice+prodPrice;
		org.testng.Assert.assertEquals(totalProdPriceCalc, totalPrice);


		
	   String prodNameCheckout="";
	   String prodPriceShopCart="";
	   int prodQuant;
	   
	   if(GeneralBind.offerProduct)
	   {
		   swipeToElement("android", prop.getProperty("text.ProductQuantityCheckout"));

		   System.out.println("offer product");
		   swipeFullFromBottomToTop("android");
		   verifyElementIsDisplayed(prop.getProperty("text.ProductTitleCheckoutOfferProduct"));
			prodNameCheckout=getText(prop.getProperty("text.ProductTitleCheckoutOfferProduct"));
			
			verifyElementIsDisplayed(prop.getProperty("text.ProductPriceCheckoutOfferProduct"));
			String[] prodPriceShopCartFull=getText(prop.getProperty("text.ProductPriceCheckoutOfferProduct")).split("\\.");
			prodPriceShopCart="₹"+prodPriceShopCartFull[0];
	   }

	   else{
		   swipeToElement("android", prop.getProperty("text.ProductQuantityCheckout"));
		   System.out.println("non offer product : ");

		   swipeFullFromBottomToTop("android");

		   verifyElementIsDisplayed(prop.getProperty("text.ProductTitleCheckout"));
			prodNameCheckout=getText(prop.getProperty("text.ProductTitleCheckout"));
			
			verifyElementIsDisplayed(prop.getProperty("text.ProductPriceCheckout"));
			String[] prodPriceShopCartFull=getText(prop.getProperty("text.ProductPriceCheckout")).split("\\.");
			prodPriceShopCart="₹"+prodPriceShopCartFull[0];
			   
	   }
	  
	    verifyElementIsDisplayed(prop.getProperty("text.ProductQuantityCheckout"));
		String quantFull=getText(prop.getProperty("text.ProductQuantityCheckout"));
		prodQuant=Integer.parseInt(quantFull);
		
		verifyStep("Product name checkout page : "+prodNameCheckout, "INFO");

		verifyStep("Product price checkout page : "+prodPriceShopCart, "INFO");

		verifyStep("Product quantity checkout page : "+prodQuant, "INFO");


		org.testng.Assert.assertEquals(prodPriceShopCart, GeneralBind.productPriceSearchResults);

		org.testng.Assert.assertEquals(prodNameCheckout, GeneralBind.productNameSearchResults);

		org.testng.Assert.assertEquals(prodQuant, GeneralBind.productQuantity);
		
		return this;
	}

}
