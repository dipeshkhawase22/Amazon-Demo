package basePages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.By;
import com.relevantcodes.extentreports.ExtentTest;

import bindings.AmazonBind;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileDriver;
import basePages.BuyProductScreen;
import utilities.ExcelMaker;
import bindings.GeneralBind;

public class BuyProductScreen extends AmazonBind{

	private static final String SHOPPING_CART_NAME = "productNameShoppingCart";
	private static final String SHOPPING_CART_PRICE = "productPriceShoppingCart";
	private static final String SHOPPING_CART_IMAGE = "productImageShoppingCart";
	private static final String SHOPPING_CART_SUBTOTAL = "subTotalShoppingCart";
	private static final String SHOPPING_CART_QUANTITY = "quantityShoppingCart";
	private static final String BUY_PRODUCT = "proceedToBuy";
	private static final String SHIPPING_ADDRESS = "ShippingAddress";
	private static final String DELIVER_BUTTON = "deliverButton";
	private static final String CONTINUE_BUTTON = "continueButton";
	private static final String CVV = "cVVEnterField";
	private static final String CHECKOUT_PRICE_TOTAL = "checkoutTotalPrice";
	private static final String CHECKOUT_PRICE_BREAKDOWN = "checkoutBreakdownProductPrice";
	private static final String CHECKOUT_PRICE_DELIVERY = "checkOutDeliveryPrice";
	private static final String CHECKOUT_OFFER_TITLE = "ProductTitleCheckoutOfferProduct";
	private static final String CHECKOUT_OFFER_PRICE = "ProductPriceCheckoutOfferProduct";
	private static final String PRODUCT_CHECKOUT_TITLE = "ProductTitleCheckout";
	private static final String PRODUCT_CHECKOUT_PRICE = "ProductPriceCheckout";
	private static final String PRODUCT_CHECKOUT_QUANTITY = "ProductQuantityCheckout";
	private static final String PRODUCT_PRICE_SEARCH = "productPriceSearchResults";
	private static final String PRODUCT_NAME_SEARCH = "productNameSearchResults";
	private static final int PRODUCT_QUANTITY = productQuantity; 

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
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	//PRICE VALIDATION
	public BuyProductScreen validateShoppingCart() {
		verifyText(SHOPPING_CART_NAME, productNameShoppingCart);
		verifyElementIsDisplayed(productNameShoppingCart);
		verifyStep("Shopping cart page displayed", "PASS");
		String prodNameShopCart=getText(productNameShoppingCart);
		verifyStep("Product name shopping cart : "+prodNameShopCart, "INFO");
		verifyText(SHOPPING_CART_PRICE, productPriceShoppingCart);
		verifyElementIsDisplayed(productPriceShoppingCart);
		String[] prodPriceShopCartFull=getText(productPriceShoppingCart).split("\\.");
		prodPriceShopCartFull[0] = prodPriceShopCartFull[0].replaceAll("[^a-zA-Z0-9]", "");
		String prodPriceShopCart="₹"+(prodPriceShopCartFull[0].trim());
		verifyStep("Product price shopping cart : "+prodPriceShopCart, "INFO");
		verifyText(SHOPPING_CART_IMAGE, productImageShoppingCart );
		verifyElementIsDisplayed(productImageShoppingCart);
		verifyText(SHOPPING_CART_SUBTOTAL, subTotalShoppingCart);
		verifyElementIsDisplayed(subTotalShoppingCart);
		String[] subTotalprodPriceShopCartfull=getText(subTotalShoppingCart).split("\\.");
		subTotalprodPriceShopCartfull[0] = subTotalprodPriceShopCartfull[0].replaceAll("[^a-zA-Z0-9]", "");
		String subTotalprodPriceShopCart="₹"+subTotalprodPriceShopCartfull[0];
		verifyText(SHOPPING_CART_QUANTITY, quantityShoppingCart);
		verifyElementIsDisplayed(quantityShoppingCart);
		int prodQuant=Integer.parseInt(getText(quantityShoppingCart));
		verifyStep("Product quantity shopping cart : "+prodQuant, "INFO");
		verifyText(PRODUCT_PRICE_SEARCH, productPriceSearchResults);
		return this;
	}

	public BuyProductScreen proceedtoBuy() {
		verifyText(BUY_PRODUCT, proceedToBuy);
		verifyElementIsDisplayed(proceedToBuy);
		click(proceedToBuy);
		return this;
	}

	public BuyProductScreen selectShippingAddress() {
		verifyText(SHIPPING_ADDRESS, ShippingAddress);
		verifyElementIsDisplayed(ShippingAddress);
		String address=getText(ShippingAddress);
		System.out.println("address : "+address);
		verifyText(DELIVER_BUTTON, deliverButton);
		verifyElementIsDisplayed(deliverButton);
		click(deliverButton);
		verifyText(CONTINUE_BUTTON, continueButton);
		verifyElementIsDisplayed(continueButton);
		click(continueButton);
		return this;
	}

	public BuyProductScreen selectPayment() {
		ExcelMaker excelFetch = new ExcelMaker();
		Map<String, String> cvvData = excelFetch.getDataFromExcel("Login_And_Place_Order_TC01", "account1");
		swipeFullFromBottomToTop(p1);		
		if(!verifyIsDisplayed(cVVEnterField))
		{
			verifyElementIsDisplayed(paymentCardSelect);
			click(paymentCardSelect);
		}
		verifyText(CVV, cVVEnterField);
		verifyElementIsDisplayed(cVVEnterField);
		enterText(cVVEnterField, cvvData.get("Cvv"));
		swipeToElement(p1,continueButton);
		verifyElementIsDisplayed(continueButton);
		click(continueButton);
		return this;
	}

	//SKIP PRIME MEMBERSHIP PAGE
	public BuyProductScreen selectPrimeMember() throws InterruptedException {
		if(verifyIsDisplayed(NoThanksButton))
		{
			click(NoThanksButton);
		}
		return this;
	}

	//VALIDATE PRICE BREAKDOWN
	public BuyProductScreen validateCheckoutPage() throws InterruptedException {
		verifyStep("Checkout page displayed", "PASS");
		swipeFullFromBottomToTop(p1);
		verifyText(CHECKOUT_PRICE_TOTAL, checkoutTotalPrice);
		verifyElementIsDisplayed(checkoutTotalPrice);
		String totalPriceFull=getText(checkoutTotalPrice);
		totalPriceFull=totalPriceFull.replaceAll("[^a-zA-Z0-9]", "");
		int totalPrice=Integer.parseInt(totalPriceFull);
		verifyText(CHECKOUT_PRICE_BREAKDOWN, checkoutBreakdownProductPrice);
		verifyElementIsDisplayed(checkoutBreakdownProductPrice);
		String breaqkdownprodfullprice=getText(checkoutBreakdownProductPrice);
		breaqkdownprodfullprice=breaqkdownprodfullprice.replaceAll("[^a-zA-Z0-9]", "");
		int prodPrice=Integer.parseInt(breaqkdownprodfullprice);
		verifyText(CHECKOUT_PRICE_DELIVERY, checkOutDeliveryPrice);
		verifyElementIsDisplayed(checkOutDeliveryPrice);
		String deliveryFullPrice=getText(checkOutDeliveryPrice);
		deliveryFullPrice=deliveryFullPrice.replaceAll("[^a-zA-Z0-9]", "");
		int deliveryPrice=Integer.parseInt(deliveryFullPrice);
		int totalProdPriceCalc=deliveryPrice+prodPrice;
		String prodNameCheckout="";
		String prodPriceShopCart="";
		int prodQuant;
		if(GeneralBind.offerProduct)
		{
			swipeToElement(p1, ProductQuantityCheckout);
			System.out.println("offer product");
			swipeFullFromBottomToTop(p1);
			verifyText(CHECKOUT_OFFER_TITLE, ProductTitleCheckoutOfferProduct);
			verifyElementIsDisplayed(ProductTitleCheckoutOfferProduct);
			prodNameCheckout=getText(ProductTitleCheckoutOfferProduct);
			verifyText(CHECKOUT_OFFER_PRICE, ProductPriceCheckoutOfferProduct);
			verifyElementIsDisplayed(ProductPriceCheckoutOfferProduct);
			String[] prodPriceShopCartFull=getText(ProductPriceCheckoutOfferProduct).split("\\.");
			prodPriceShopCart="₹"+prodPriceShopCartFull[0];
		}
		else{
			swipeToElement(p1, ProductQuantityCheckout);
			System.out.println("non offer product : ");
			swipeFullFromBottomToTop(p1);
			verifyText(PRODUCT_CHECKOUT_TITLE, ProductTitleCheckout);
			verifyElementIsDisplayed(ProductTitleCheckout);
			prodNameCheckout=getText(ProductTitleCheckout);
			verifyText(PRODUCT_CHECKOUT_PRICE, ProductPriceCheckout);
			verifyElementIsDisplayed(ProductPriceCheckout);
			String[] prodPriceShopCartFull=getText(ProductPriceCheckout).split("\\.");
			prodPriceShopCart="₹"+prodPriceShopCartFull[0];
		}
		verifyText(PRODUCT_CHECKOUT_QUANTITY, ProductQuantityCheckout);
		verifyElementIsDisplayed(ProductQuantityCheckout);
		String quantFull=getText(ProductQuantityCheckout);
		prodQuant=Integer.parseInt(quantFull);
		verifyStep("Product name checkout page : "+prodNameCheckout, "INFO");
		verifyStep("Product price checkout page : "+prodPriceShopCart, "INFO");
		verifyStep("Product quantity checkout page : "+prodQuant, "INFO");
		verifyText(PRODUCT_PRICE_SEARCH, productPriceSearchResults);
		verifyText(PRODUCT_NAME_SEARCH, productNameSearchResults);
		return this;
	}

	private String productNameShoppingCart = "id===com.amazon.mShop.android.shopping:id/action_bar_cart_count\r\n" + "";
	private String productPriceShoppingCart = "xpath===//android.widget.ImageView[@content-desc=\"Cart\"]";
	private String proceedToBuy = "xpath===//*[@text='Proceed to Buy']";
	private String productImageShoppingCart = "xpath===/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.RelativeLayout/android.widget.RelativeLayout[2]/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.ViewAnimator/android.view.ViewGroup/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.RelativeLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.view.View/android.view.View[4]/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View[2]/android.widget.Image";
	private String subTotalShoppingCart = "xpath===(//*[@resource-id='sc-proceed-to-checkout-params-form']//android.view.View)[4]";
	private String quantityShoppingCart = "id===com.amazon.mShop.android.shopping:id/chrome_action_bar_cart_count";	
	private String paymentCardSelect = "xpath===//*[@text='Citi Bank****9085CITI Bank DIPESH S K Expires 11/2022']";	
	private String ShippingAddress = "xpath===/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.RelativeLayout/android.widget.RelativeLayout[2]/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.ViewAnimator/android.view.ViewGroup/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.RelativeLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View/android.view.View[2]/android.view.View[1]/android.widget.RadioButton";
	private String deliverButton = "xpath===//*[@text='Deliver to this address DS MAX Sigma, Electronic City']";
	private String continueButton = "xpath===//*[@text='Continue']";
	private String cVVEnterField = "xpath===//android.widget.EditText";
	private String NoThanksButton = "xpath===//*[@text='No Thanks']";
	private String giftOptions = "xpath===//*[@text='Add gift options']";
	private String checkoutBreakdownProductPrice = "xpath===(//*[@resource-id='subtotals-marketplace-table']//android.view.View)[3]";
	private String checkOutDeliveryPrice = "xpath===(//*[@resource-id='subtotals-marketplace-table']//android.view.View)[6]";
	private String checkoutTotalPrice= "xpath===(//*[@resource-id='subtotals-marketplace-table']//android.view.View)[9]";
	private String ProductTitleCheckoutOfferProduct = "xpath===(//*[@resource-id='spc-orders']//android.view.View)[5]";
	private String ProductPriceCheckoutOfferProduct = "xpath===(//*[@resource-id='spc-orders']//android.view.View)[10]";         
	private String ProductTitleCheckout = "xpath===(//*[@resource-id='spc-orders']//android.view.View)[5]";
	private String ProductPriceCheckout = "xpath===(//*[@resource-id='spc-orders']//android.view.View)[8]";
	private String ProductQuantityCheckout = "xpath===//*[@resource-id='spc-orders']//android.widget.Spinner";                                         

}

