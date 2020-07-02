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

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public LoginScreen signIn() throws InterruptedException {

		ExcelMaker excelFetch = new ExcelMaker();
		Map<String, String> signinDetailsMap = excelFetch.getDataFromExcel("Login_And_Place_Order_TC01", "account1");
		System.out.println("map" + signinDetailsMap);

		verifyStep("App launched", "PASS");


		verifyElementIsDisplayed(prop.getProperty("button.signInButtonNavigation"));
		click(prop.getProperty("button.signInButtonNavigation"));


		verifyElementIsDisplayed(prop.getProperty("edit.emailField"));
		enterText(prop.getProperty("edit.emailField"), signinDetailsMap.get("Username"));
		click(prop.getProperty("button.continueButton"));

		verifyStep("Login Page displayed", "PASS");


		verifyElementIsDisplayed(prop.getProperty("edit.passwordField"));
		enterText(prop.getProperty("edit.passwordField"), signinDetailsMap.get("Password"));
		click(prop.getProperty("button.logInButton"));

		return this;

	}
}