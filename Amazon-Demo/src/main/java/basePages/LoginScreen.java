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
import basePages.LoginScreen;
import utilities.ExcelMaker;

public class LoginScreen extends AmazonBind {

	private static final String SIGNIN_BUTTON = "signInButtonNavigation";
	private static final String EMAIL = "emailField";
	private static final String PASSWORD = "passwordField";

	private static Properties prop;
	public Map<String, String> capData1 = new HashMap<String, String>();

	public LoginScreen(MobileDriver driver, ExtentTest test, Map<String, String> capData1) {
		this.driver = driver;
		this.test = test;
		this.capData1 = capData1;
		prop = new Properties();
		try {
			if (capData1.get("PlatformName").equalsIgnoreCase("Android")) {
				prop.load(new FileInputStream(new File("./Locators/Android/logIn.properties")));
			}
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public LoginScreen signIn() throws InterruptedException {
		ExcelMaker excelFetch = new ExcelMaker();
		Map<String, String> signinDetailsMap = excelFetch.getDataFromExcel("Login_And_Place_Order_TC01", "account1");
		System.out.println("map" + signinDetailsMap);
		verifyStep("App launched", "PASS");
		verifyText(SIGNIN_BUTTON, signInButtonNavigation);
		verifyElementIsDisplayed(signInButtonNavigation);
		click(signInButtonNavigation);
		verifyText(EMAIL, emailField);
		verifyElementIsDisplayed(emailField);
		enterText(emailField, signinDetailsMap.get("Username"));
		click(continueButton);
		verifyStep("Login Page displayed", "PASS");
		verifyText(PASSWORD, passwordField);
		verifyElementIsDisplayed(passwordField);
		enterText(passwordField, signinDetailsMap.get("Password"));
		click(logInButton);
		return this;
	}

	private String signInButtonNavigation = "id===com.amazon.mShop.android.shopping:id/sign_in_button";
	private String emailField = "xpath===//*[@resource-id='ap_email_login']";
	private String continueButton = "xpath===(//*[@resource-id='continue'])[1]";
	private String passwordField = "xpath===//*[@resource-id='ap_password']";
	private String logInButton = "xpath===//*[@resource-id='signInSubmit']";

}