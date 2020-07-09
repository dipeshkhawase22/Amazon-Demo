package utilities;

import java.io.File;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public abstract class Reporter {

	public ExtentTest test;
	public static ExtentReports extent;
	public String testCaseName, testDescription, category, authors;
	public static String tcName="";

	public void verifyStep(String desc, String status){	
		long snapNumber = 100000l;
		String imageid=null;
		try {
			if(!status.toUpperCase().equals("INFO"))
			{
				snapNumber= takeSnap();
				imageid="./reports/images/"+snapNumber+".jpg";
				String fullPath=System.getProperty("user.dir")+"\\reports\\images\\"+snapNumber+".jpg";
				org.testng.Reporter.log("<br>"+desc+"</br><br><a href='"+ fullPath + "'> <img src='"+ fullPath + "' height='300' width='200'/> </a><br>");  
			}
			else
			{
				org.testng.Reporter.log("<br>"+desc+"</br>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(status.toUpperCase().equals("PASS")){
			this.test.log(LogStatus.PASS, desc+test.
					addScreenCapture("./../reports/images/"+snapNumber+".jpg"));
		}else if(status.toUpperCase().equals("FAIL")){
			this.test.log(LogStatus.FAIL, desc+test.addScreenCapture("./../reports/images/"+snapNumber+".jpg"));
			//throw new RuntimeException("FAILED");
		}else if(status.toUpperCase().equals("INFO")){
			this.test.log(LogStatus.INFO, desc);
		}
	}

	public abstract long takeSnap();

	public ExtentReports startResult(){
		try{
			extent = new ExtentReports("./reports/result.html", true);
			extent.loadConfig(new File("./src/main/resources/extent-config.xml"));
		}catch(Exception e){
			System.out.println("exception throwed in startResult method");
		}
		return extent;
	}

	//Start test execution
	public ExtentTest startTestCase(String testCaseName, String testDescription){
		try{
			System.out.println(testCaseName+" *** "+testDescription);
			test = extent.startTest(testCaseName, testDescription);
		}catch(Exception e){
			System.out.println("Exception throwed in startTestCase method");
		}
		return test;
	}

	//Result
	public void endResult(){
		try{
			extent.flush();
			System.out.println("result has been saved");
		}
		catch(Exception e){
			System.out.println("Exception throwed while endResult method");
		}
	}

	public void endTestcase(){
		try{
			extent.endTest(test);
		}catch(Exception e){
			System.out.println("Exception throwed while endTestcae method");
		}
	}
}
