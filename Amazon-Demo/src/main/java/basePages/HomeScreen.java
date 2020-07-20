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
import basePages.HomeScreen;
import utilities.ExcelMaker;

public class HomeScreen extends AmazonBind {

	private static final String LANGUAGE_SELECTION = "languageSelectionPopUp";
	private static final String ENGLISH_SETTINGS = "languageEnglishSettings";
	private static final String SAVE_CHANGES = "saveChangesButton";
	private static final String AMAZON_LOGO = "amazonHomeLogo";
	private static final String MIC_BUTTON = "microphoneButton";
	private static final String LOCATION_SELECTION = "locationSelection";
	private static final String MENU_BUTTON = "menuButton";
	private static final String TEXT_HELLO = "helloText";
	private static final String HOME_BUTTON = "homeButton";
	private static final String SEARCH_BAR = "searchBar";
	private static final String SEARCH_RESULT = "searchResult1";
	private static final String SEARCH_LIST = "searchListPageImage1";
	private static final String CART_BUTTON = "cartButton";
	private static final String CART_COUNT = "cartCount";
	private static final String DELETE_BUTTON = "deleteButton";
	private static final String NAME_ACTUAL = "actualName";
	private static final String NAME_EXPECTED = "expectedName";

	private static Properties prop;
	public Map<String, String> capData1 = new HashMap<String, String>();

	public HomeScreen(MobileDriver<?> driver, ExtentTest test, Map<String, String> capData1) {
		this.driver = driver;
		this.test = test;
		this.capData1 = capData1;
		prop = new Properties();
		try {
			if (capData1.get("PlatformName").equalsIgnoreCase("Android")) {
				prop.load(new FileInputStream(new File("./Locators/Android/homePage.properties")));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HomeScreen selectLanguage() {
		verifyText(LANGUAGE_SELECTION, languageSelectionPopUp);
		verifyElementIsDisplayed(languageSelectionPopUp);
		verifyText(ENGLISH_SETTINGS, languageEnglishSettings);
		verifyElementIsDisplayed(languageEnglishSettings);
		click(languageEnglishSettings);
		verifyText(SAVE_CHANGES, saveChangesButton);
		verifyElementIsDisplayed(saveChangesButton);
		click(saveChangesButton);
		return this;
	}

	public HomeScreen validateHomePage() {
		verifyText(AMAZON_LOGO, amazonHomeLogo);
		verifyElementIsDisplayed(amazonHomeLogo);
		verifyText(MIC_BUTTON, microphoneButton);
		verifyElementIsDisplayed(microphoneButton);
		verifyText(LOCATION_SELECTION, locationSelection);
		verifyElementIsDisplayed(locationSelection);   
		verifyStep("Home Page Validated", "PASS");
		return this;
	}

	public HomeScreen validateLogin() {
		ExcelMaker excelFetch = new ExcelMaker();
		Map<String, String> signinDetailsMap = excelFetch.getDataFromExcel("Login_And_Place_Order_TC01", "account1");
		verifyText(MENU_BUTTON, menuButton);
		verifyElementIsDisplayed(menuButton);
		click(menuButton);
		verifyText(TEXT_HELLO, helloText);
		verifyElementIsDisplayed(helloText);
		verifyStep("Logged in successfully", "PASS");
		String actualName=getText(helloText);
		actualName=actualName.replace("Hello, ", "");
		String expectedName=signinDetailsMap.get("Name");
		verifyText(NAME_ACTUAL, actualName);
		verifyText(NAME_EXPECTED, expectedName);
		verifyText(HOME_BUTTON, homeButton);
		verifyElementIsDisplayed(homeButton);
		click(homeButton);
		return this;
	}

	public HomeScreen searchProduct() throws InterruptedException {
		ExcelMaker excelFetch = new ExcelMaker();
		Map<String, String> searchData = excelFetch.getDataFromExcel("Login_And_Place_Order_TC01", "account1");
		verifyText(SEARCH_BAR, searchBar);
		verifyElementIsDisplayed(searchBar);
		click(searchBar);
		verifyElementIsDisplayed(searchBar);
		enterText(searchBar, searchData.get("SearchTerm"));
		verifyStep("Product searched", "PASS");
		verifyText(SEARCH_RESULT, searchResult1);
		verifyElementIsDisplayed(searchResult1);
		click(searchResult1);
		verifyText(SEARCH_LIST, searchListPageImage1);
		verifyElementIsDisplayed(searchListPageImage1);
		return this;
	}

	public HomeScreen clearCart() throws InterruptedException {
		verifyText(CART_COUNT, cartCount);
		verifyElementIsDisplayed(cartCount);
		int cartCount=Integer.parseInt(getText("cartCount"));
		if(cartCount>0)
		{
			System.out.println("Deleting cart items");
			verifyText(CART_BUTTON, cartButton);
			verifyElementIsDisplayed(cartButton);
			click(cartButton);
			verifyText(DELETE_BUTTON, deleteButton);
			verifyElementIsDisplayed(deleteButton);
			click(deleteButton);
			//verifyElementIsDisplayed(ProductRemovedConfirmation);
			int cartCountUpdated=Integer.parseInt(getText("cartCount"));
			if(cartCountUpdated==0)
			{
				System.out.println("cart items deleted successfully");
				verifyStep("Cart items deleted", "PASS");
			}		
		}
		verifyText(AMAZON_LOGO, amazonHomeLogo);
		verifyElementIsDisplayed(amazonHomeLogo);
		click(amazonHomeLogo);
		return this;
	}

	private String amazonHomeLogo = "id===com.amazon.mShop.android.shopping:id/action_bar_home_logo";
	private String microphoneButton = "id===com.amazon.mShop.android.shopping:id/voice_btn_icon";
	private String locationSelection = "id===com.amazon.mShop.android.shopping:id/glow_subnav_label";
	private String languageSelectionPopUp = "xpath===//*[@resource-id='lop-select']";
	private String languageEnglishSettings = "xpath===//*[@text='English - EN']";
	private String saveChangesButton = "xpath===//*[@text='Save Changes']";
	private String menuButton = "id===com.amazon.mShop.android.shopping:id/action_bar_burger_icon";
	private String helloText = "id===com.amazon.mShop.android.shopping:id/gno_greeting_text_view";
	private String homeButton = "id===com.amazon.mShop.android.shopping:id/web_home_shop_by_department_label";
	private String cartButton = "id===com.amazon.mShop.android.shopping:id/action_bar_cart_image";
	private String deleteButton = "xpath===(//*[@text='Delete'])[2]";
	private String cartCount = "id===com.amazon.mShop.android.shopping:id/action_bar_cart_count";
	private String searchBar = "id===com.amazon.mShop.android.shopping:id/rs_search_src_text";
	private String searchResult1 = "xpath===(//*[@resource-id='com.amazon.mShop.android.shopping:id/iss_search_dropdown_item_text'])[1]";
	private String searchListPageImage1 = "xpath===(//*[@resource-id='com.amazon.mShop.android.shopping:id/rs_results_image'])[1]";

}
