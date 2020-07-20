package basePages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.testng.Assert;

import com.relevantcodes.extentreports.ExtentTest;

import bindings.AmazonBind;
import io.appium.java_client.MobileDriver;
import basePages.ProductScreen;
import bindings.GeneralBind;

public class ProductScreen extends AmazonBind {

	private static final String PRODUCT_TITLE = "productTitle";
	private static final String PRODUCT_REVIEW = "productReview";
	private static final String PRODUCT_SHARE = "shareProduct";
	private static final String PRODUCT_IMAGE = "ProductImage";
	private static final String ADD_TO_CART = "addtoCartButton";
	private static final String CART_COUNT = "cartCount";
	private static final String CART_BUTTON = "cartButton";
	private static final String PRODUCT_NAME_SEARCH = "productNameSearchResults";

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
		swipeFullFromBottomToTop(p1);
		GeneralBind.productNameSearchResults=getText(prodSearchPageTitle);
		String[] fullPrice=getText(prodSearchPagePrice).split(" ");
		System.out.println("price full : "+fullPrice[0]);
		System.out.println("name full : "+GeneralBind.productNameSearchResults);
		if(!fullPrice[0].contains("₹"))
		{
			Assert.assertTrue(false,"₹ not present in search results page");
		}
		GeneralBind.productPriceSearchResults=fullPrice[0];
		verifyStep("Product Name search page :"+GeneralBind.productNameSearchResults, "INFO");
		verifyStep("Product Price search page :"+GeneralBind.productPriceSearchResults, "INFO");
		click(prodSearchPageTitle);
		return this;
	}

	public ProductScreen validateProductScreen() {		
		verifyText(PRODUCT_TITLE, productTitle);
		verifyElementIsDisplayed(productTitle);
		verifyText(PRODUCT_REVIEW, productReview);
		verifyElementIsDisplayed(productReview);
		verifyText(PRODUCT_SHARE, shareProduct);
		verifyElementIsDisplayed(shareProduct);
		verifyText(PRODUCT_IMAGE, ProductImage);
		verifyElementIsDisplayed(ProductImage);
		verifyStep("Product details page displayed", "PASS");
		String pScreenProductTitle=getText(productTitle);
		verifyStep("Product name PDP page : "+pScreenProductTitle, "INFO");
		verifyText(PRODUCT_NAME_SEARCH, productNameSearchResults);
		swipeFullFromBottomToTop(p1);
		if(verifyIsDisplayed(pdpPageSavingsPrice))
		{
			GeneralBind.offerProduct=true;
		}
		String pScreenPrice=getText(productPrice);
		pScreenPrice=pScreenPrice.replace("rupees ", "₹");
		verifyStep("Product price PDP page : "+pScreenPrice, "INFO");
		verifyText(PRODUCT_NAME_SEARCH, productNameSearchResults);
		swipeToElement(p1, wishListButton);
		if(!verifyIsDisplayed(addtoCartButton))
		{
			swipeToElementUpwards(p1, oneTimePurchase);
			click(oneTimePurchase);
		}
		swipeToElement(p1, addtoCartButton);
		GeneralBind.productQuantity=Integer.parseInt(getText(quantityDropdown));
		verifyStep("Product quantity : "+GeneralBind.productQuantity, "INFO");
		verifyText(ADD_TO_CART, addtoCartButton);
		verifyElementIsDisplayed(addtoCartButton);
		return this;
	}

	public ProductScreen addToCart() throws InterruptedException {
		verifyText(CART_COUNT, cartCount);
		verifyElementIsDisplayed(cartCount);
		int cartCountBefore=Integer.parseInt(getText(cartCount));
		click(addtoCartButton);
		verifyElementIsDisplayed(cartCount);
		int cartCountAfter=Integer.parseInt(getText(cartCount));
		if(cartCountBefore+GeneralBind.productQuantity==cartCountAfter)
		{
			System.out.println("Product added to cart");
		}
		return this;
	}

	public ProductScreen navigateToShoppingCart() {
		verifyText(CART_BUTTON, cartButton);
		verifyElementIsDisplayed(cartButton);
		click(cartButton);
		return this;
	}

	private String productTitle = "xpath===//*[@resource-id='title_feature_div']//android.view.View";
	private String productReview = "xpath===//*[@resource-id='acrCustomerReviewLink']";
	private String shareProduct = "xpath===//*[@resource-id='swf-share-icon-container']";
	private String ProductImage = "xpath===//*[@resource-id='image-block-row']";
	private String productPrice = "xpath===//*[@resource-id='atfRedesign_priceblock_priceToPay']//android.widget.EditText";
	private String OfferTableBox = "xpath===//*[@resource-id='soppInsidePrimary_feature_div']";
	private String quantityDropdown = "xpath===//*[@resource-id='mobileQuantityDropDown']";
	private String BuyNowButton = "xpath===//*[@resource-id='a-autoid-1']";
	private String addtoCartButton = "xpath===//*[@resource-id='add-to-cart-button']";
	private String wishListButton = "xpath===//*[@resource-id='add-to-wishlist-button-submit']";
	private String cartButton = "id===com.amazon.mShop.android.shopping:id/action_bar_cart_image";
	private String oneTimePurchase = "xpath===//*[@resource-id='oneTimeBuyBox']";
	private String pdpPageSavingsPrice = "xpath===//*[@resource-id='atfRedesign_buyingPrice_savings']";
	private String prodSearchPageTitle = "id===bylineInfo_feature_div";
	private String prodSearchPagePrice = "id===atfRedesign_priceblock_priceToPay";
	private String cartCount = "id===com.amazon.mShop.android.shopping:id/action_bar_cart_count";
	private String productSearchImage = "com.amazon.mShop.android.shopping:id/rs_results_image";
	private String productSearchTitle = "com.amazon.mShop.android.shopping:id/item_title";
	private String productSearchPrice = "//*[@resource-id='com.amazon.mShop.android.shopping:id/rs_results_styled_price_v2']//android.widget.TextView";
	private String searchBar = "id===com.amazon.mShop.android.shopping:id/rs_search_src_text";
	private String searchResult1 = "xpath===(//*[@resource-id='com.amazon.mShop.android.shopping:id/iss_search_dropdown_item_text'])[1]";
	private String searchListPageImage1 = "xpath===(//*[@resource-id='com.amazon.mShop.android.shopping:id/rs_results_image'])[1]";
}
	