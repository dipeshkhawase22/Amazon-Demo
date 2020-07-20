package bindings;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.ElementNotSelectableException;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.relevantcodes.extentreports.ExtentTest;

import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.remote.MobileCapabilityType;
import utilities.Reporter;

public class GeneralBind extends Reporter {

	public MobileDriver driver;
	protected Properties prop;
	public Map<String, String> capData = new HashMap<String, String>();
	public static String productNameSearchResults="";
	public static String productPriceSearchResults="";
	public static int productQuantity;
	public static int getNumber;
	public static boolean offerProduct=false;
	public String p1 = "android";
	//public String p2 = "iOS";

	public GeneralBind(MobileDriver driver, ExtentTest test) {
		this.driver = driver;
		this.test = test;
	}

	public GeneralBind () {} 

	//StartApp
	public void startTestReport() {
		test = startTestCase(testCaseName, testDescription);
		test.assignCategory(category);
		test.assignAuthor(authors);
	}

	//CloseApp
	public void closeApp() {
		try {
			driver.closeApp();
		}catch (Exception exp) {}
	}

	public void invokeApp() {
		System.out.println(" Launching Application... ");
		URL url = null;
		DesiredCapabilities caps = new DesiredCapabilities();
		try {		
			String dir = System.getProperty("user.dir");
			//url = new URL("http://" + "192.168.1.5" + ":" + capData.get("Port") + "/wd/hub");
			url = new URL("http://192.168.1.5:4723/wd/hub");
			switch (capData.get("PlatformName").toLowerCase()) {
			case "android":
				caps.setCapability(MobileCapabilityType.PLATFORM_NAME, capData.get("PlatformName"));
				caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, capData.get("PlatformVersion"));
				caps.setCapability(MobileCapabilityType.DEVICE_NAME, capData.get("DeviceName"));
				caps.setCapability(MobileCapabilityType.UDID, capData.get("udid"));
				caps.setCapability("automationName", "UiAutomator2");
				caps.setCapability("systemPort", capData.get("systemPort"));
				caps.setCapability("newCommandTimeout", 9000);
				caps.setCapability(MobileCapabilityType.APP, dir + "/app/Amazon_shopping.apk");
				caps.setCapability("appPackage", "com.amazon.mShop.android.shopping");
				caps.setCapability("appActivity","com.amazon.mShop.home.HomeActivity");
				driver = new AndroidDriver<MobileElement>(url, caps);
				break;

			default:
				break;
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void click(String property) {
		By byProperty = getLocator(property);
		MobileElement element = null;
		try {
			element = (MobileElement) driver.findElement(byProperty);
			driver.findElement(byProperty).click();
			System.out.println("Element is Clicked");
		}  
		catch (Exception e) {
			Assert.assertTrue(false, e.getMessage());
		}
	}

	public void enterText(String property, String data) {
		MobileElement element = null;
		MobileElement element2 = null;
		try {
			element = (MobileElement) driver.findElement(getLocator(property));
			element.clear();
			element2 = (MobileElement) driver.findElement(getLocator(property));
			element2.sendKeys(data);
			System.out.println("Data entered in the required field");
		}
		catch (ElementNotFoundException e) {
			Assert.assertTrue(false, element + "Element not found\n" + e.getMessage());
		} catch (ElementNotSelectableException e) {
			Assert.assertTrue(false, element + "Element not Selectable\n" + e.getMessage());
		} catch (ElementNotVisibleException e) {
			Assert.assertTrue(false, element + "Element not Visible\n" + e.getMessage());
		} catch (ElementNotInteractableException e) {
			Assert.assertTrue(false, element + "Element not Interatable\n" + e.getMessage());
		} catch (TimeoutException e) {
			Assert.assertTrue(false, element + "Time out error\n" + e.getMessage());
		} catch (Exception e) {
			Assert.assertTrue(false, e.getMessage());
		}
	}

	public boolean verifyText(String text, String property) {
		MobileElement element = null;
		String sText = "";
		boolean val=false;
		try {
			element = (MobileElement) driver.findElement(getLocator(property));
			sText = element.getText();
			if (sText.equalsIgnoreCase(text)) {
				val= true;
			}
		}
		catch (Exception e) {
			SoftAssert softAssert = new SoftAssert();
			softAssert.assertTrue(false, e.getMessage());
		}
		return val;
	}

	public void verifyElementIsDisplayed(String property) {
		MobileElement element=null;
		try {
			driver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
			element = (MobileElement) driver.findElement(getLocator(property));
			element.isDisplayed();
		}
		catch (Exception e) {
			Assert.assertTrue(false, "Element not displayed\n" + e.getMessage());
		} 
	}

	public boolean verifyIsDisplayed(String property) {
		boolean present=false;
		MobileElement element=null;
		try {
			element = (MobileElement) driver.findElement(getLocator(property));
			element.isDisplayed();
			present=true;
		}
		catch (Exception e) {
			present=false;		} 
		return present;
	}

	public void verifyElementIsPresent(String property, long timeoutInSecs){
		try{
			long startTime = System.currentTimeMillis();
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			while (System.currentTimeMillis() < (startTime + (timeoutInSecs * 1000))) {
				if (verifyIsDisplayed(property)){
					driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
					return;
				}
			}
			Assert.assertTrue(false, property + " is displayed or not validation");
		}
		catch (ElementNotFoundException e)
		{
			Assert.assertTrue(false, property + "Element not found\n" + e.getMessage());
		}
		catch (TimeoutException e) {
			Assert.assertTrue(false, property + "Time out error\n" + e.getMessage());
		}
		catch (ElementNotSelectableException e){
			Assert.assertTrue(false, getLocator(property) + "Element not Selectable\n" + e.getMessage());
		}
		catch (ElementNotVisibleException e) {
			Assert.assertTrue(false, property + "Element not Visible\n" + e.getMessage());
		}
		catch (ElementNotInteractableException e) {
			Assert.assertTrue(false, property + "Element not Interatable\n" + e.getMessage());
		}
		catch (Exception e) {
			Assert.assertTrue(false, e.getMessage());
		}
	}

	public void verifyElementIsNotPresent(String property, long timeoutInSecs) {
		try{
			long startTime = System.currentTimeMillis();
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			while (System.currentTimeMillis() < (startTime + (timeoutInSecs * 1000))) {
				if (verifyIsDisplayed(property)) {
					driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
					return;
				}
			}
			Assert.assertTrue(true, property + " is displayed or not validation");
		} 
		catch (ElementNotFoundException e) {
			Assert.assertTrue(true, property + "Element not found\n" + e.getMessage());
		} 
		catch (TimeoutException e) {
			Assert.assertTrue(false, property + "Time out error\n" + e.getMessage());
		} 
		catch (ElementNotSelectableException e) {
			Assert.assertTrue(false, getLocator(property) + "Element not Selectable\n" + e.getMessage());
		} 
		catch (ElementNotVisibleException e) {
			Assert.assertTrue(false, property + "Element not Visible\n" + e.getMessage());
		} 
		catch (ElementNotInteractableException e) {
			Assert.assertTrue(false, property + "Element not Interatable\n" + e.getMessage());
		}
		catch (Exception e) {
			Assert.assertTrue(false, e.getMessage());
		}
	}

	public String getText(String property) {
		String bReturn = "";
		MobileElement element = null;
		try {
			element = (MobileElement) driver.findElement(getLocator(property));
			return element.getText();
		} 
		catch (ElementNotFoundException e) {
			Assert.assertTrue(false, element + "Element not found\n" + e.getMessage());
		} 
		catch (TimeoutException e) {
			Assert.assertTrue(false, element + "Time out error\n" + e.getMessage());
		} 
		catch (ElementNotSelectableException e) {
			Assert.assertTrue(false, element + "Element not Selectable\n" + e.getMessage());
		} 
		catch (ElementNotVisibleException e) {
			Assert.assertTrue(false, element + "Element not Visible\n" + e.getMessage());
		} 
		catch (ElementNotInteractableException e) {
			Assert.assertTrue(false, element + "Element not Interatable\n" + e.getMessage());
		}
		catch (Exception e) {
			Assert.assertTrue(false, e.getMessage());
		}
		return bReturn;
	}

	public String getAttribute(String property, String attribute) {
		String bReturn = "";
		MobileElement element = null;
		try {
			element = (MobileElement) driver.findElement(getLocator(property));
			return element.getAttribute(attribute);
		} 
		catch (ElementNotFoundException e) {
			Assert.assertTrue(false, element + "Element not found\n" + e.getMessage());
		} 
		catch (TimeoutException e) {
			Assert.assertTrue(false, element + "Time out error\n" + e.getMessage());
		} 
		catch (ElementNotSelectableException e) {
			Assert.assertTrue(false, element + "Element not Selectable\n" + e.getMessage());
		} 
		catch (ElementNotVisibleException e) {
			Assert.assertTrue(false, element + "Element not Visible\n" + e.getMessage());
		} 
		catch (ElementNotInteractableException e) {
			Assert.assertTrue(false, element + "Element not Interatable\n" + e.getMessage());
		}
		catch (Exception e) {
			Assert.assertTrue(false, e.getMessage());
		}
		return bReturn;
	}

	//TAKING SCREENSHOT
	public long takeSnap() {
		long number = (long) Math.floor(Math.random() * 900000000L) + 10000000L;
		try {
			FileUtils.copyFile(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE),
					new File("./reports/images/" + number + ".jpg"));
		} 
		catch (IOException e) {
		} 
		catch (Exception e) {
			System.out.println("The app has been closed.");
		}
		return number;
	}

	//Locator
	public By getLocator(String property) {
		String locator = property;
		String locatorType = locator.split("===")[0];
		String locatorValue = locator.split("===")[1];

		if (locatorType.toLowerCase().equals("id"))
			return By.id(locatorValue);
		else if (locatorType.toLowerCase().equals("name"))
			return By.name(locatorValue);
		else if ((locatorType.toLowerCase().equals("classname")) || (locatorType.toLowerCase().equals("class")))
			return By.className(locatorValue);
		else if ((locatorType.toLowerCase().equals("tagname")) || (locatorType.toLowerCase().equals("tag")))
			return By.className(locatorValue);
		else if (locatorType.toLowerCase().equals("xpath"))
			return By.xpath(locatorValue);
		else
			return null;
	}

	//CHECK SELECTION 
	public boolean isSelected(String property) {
		boolean value = false;
		MobileElement element = null;
		try {
			element = (MobileElement) driver.findElement(getLocator(property));
			value = driver.findElement(getLocator(property)).isSelected();
		} 
		catch (ElementNotFoundException e) {
			Assert.assertTrue(false, element + "Element not found\n" + e.getMessage());
		} 
		catch (TimeoutException e) {
			Assert.assertTrue(false, element + "Time out error\n" + e.getMessage());
		} 
		catch (ElementNotSelectableException e) {
			Assert.assertTrue(false, element + "Element not Selectable\n" + e.getMessage());
		} 
		catch (ElementNotVisibleException e) {
			Assert.assertTrue(false, element + "Element not Visible\n" + e.getMessage());
		} 
		catch (ElementNotInteractableException e) {
			Assert.assertTrue(false, element + "Element not Interatable\n" + e.getMessage());
		}
		catch (Exception e) {
			Assert.assertTrue(false, e.getMessage());
		}
		return value;
	}

	public void keypadDown() {
		driver.hideKeyboard();
	}

	public void clearElement(String property) {
		MobileElement element = null;
		try {
			element = (MobileElement) driver.findElement(getLocator(property));
			element.clear();
		}
		catch (ElementNotFoundException e){
			Assert.assertTrue(false,element + "Element not found\n" + e.getMessage());
		}
		catch (TimeoutException e) {
			Assert.assertTrue(false,element + "Time out error\n" + e.getMessage());
		}
		catch (ElementNotSelectableException e)	{
			Assert.assertTrue(false,element + "Element not Selectable\n" + e.getMessage());
		}
		catch (ElementNotVisibleException e) {
			Assert.assertTrue(false,element + "Element not Visible\n" + e.getMessage());
		}
		catch (ElementNotInteractableException e) {
			Assert.assertTrue(false,element + "Element not Interatable\n" + e.getMessage());
		}
		catch (Exception e) {
			Assert.assertTrue(false,e.getMessage());
		}
	}

	public void swipeFullFromBottomToTop(String pfName) {
		System.out.println("Swiping......");
		try {
			Thread.sleep(2000);
			Dimension scrnSize = driver.manage().window().getSize();
			int startx = (int) (scrnSize.width / 2);
			int starty = (int) (scrnSize.height*0.3);
			int endy = (int) (scrnSize.height*0.8);
			if (pfName.equalsIgnoreCase(p1)) {
				((AndroidDriver<WebElement>) driver).swipe(startx, endy, startx, starty, 1000);
			} 
		} 
		catch (InterruptedException e) {
			Assert.assertTrue(false,e.getMessage());
		}
	}

	public boolean verifyElement(String property) {
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		boolean present = true;
		try {
			driver.findElement(getLocator(property));
			return present;
		} 
		catch (Exception e) {
			present = false;
			return present;
		}
	}

	public void swipeToElement(String pfName, String property) {
		while (true) {
			if (verifyElement(property)) {
				break;
			}
			swipeFullFromBottomToTop(pfName);
		}
	}

	public void swipeFullFromTopToBottom(String pfName) {
		try {
			Thread.sleep(2000);
			Dimension scrnSize = driver.manage().window().getSize();
			int startx = (int) (scrnSize.width / 2);
			int endy = (int) (scrnSize.height - 1);
			int starty = (int) (scrnSize.height * 0.2);
			// int endx = (int) (scrnSize.width /2);
			if (pfName.equalsIgnoreCase(p1)) {
				((AndroidDriver<WebElement>) driver).swipe(startx, starty, startx, endy, 3000);
			} 
		} 
		catch (InterruptedException e) {
			Assert.assertTrue(false,e.getMessage());
		}
	}

	public void swipeToElementUpwards(String pfName, String property) {
		while (true) {
			if (verifyElement(property)) {
				break;
			}
			swipeFullFromTopToBottom(pfName);
		}
	}

	//LAUNCH APP
	public void launchApp() {
		System.out.println("Launching the app");
		driver.launchApp();
	}

	public void clickAndroidBack() {
		((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);
	}
}
