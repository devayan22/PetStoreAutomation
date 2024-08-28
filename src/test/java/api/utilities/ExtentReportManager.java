package api.utilities;

import java.awt.Desktop;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;


public class ExtentReportManager implements ITestListener

{

	public ExtentSparkReporter sparkReporter; // UI of the Report
	public ExtentReports extent; // Populate common info on the report.
	public ExtentTest test; // creating test case entries in the report & update status of the test methods.

	String repName;  // report name
	
	public void onStart(ITestContext testContext) {
		
		/*SimpleDateFormat df= new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
		Date dt=new Date();
		String currentdatetimestamp =df.format(dt);*/
		
		String timeStamp= new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		repName="Test-Report-" + timeStamp +".html";
		
		//sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "/reports/myReport.html"); // specific  location
		sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "/reports/"+ repName); // specific  location

		sparkReporter.config().setDocumentTitle("Rest-Assured Automation Report"); // Title
		sparkReporter.config().setReportName("Pet Store Users API Testing"); // Name
		sparkReporter.config().setTheme(Theme.DARK);

		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);

		extent.setSystemInfo("Application", "Pet Store Users API");
		extent.setSystemInfo("Module", "Admin");
		extent.setSystemInfo("SubModule", "customers");
		extent.setSystemInfo("User Name", System.getProperty("user.name"));
		extent.setSystemInfo("Environment", "QA");
		
		String os = testContext.getCurrentXmlTest().getParameter("os");
		extent.setSystemInfo("Operating System", os);
		
		String browser = testContext.getCurrentXmlTest().getParameter("browser");
		extent.setSystemInfo("Operating System", browser);
		
		List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups();
		if(!includedGroups.isEmpty()){
			extent.setSystemInfo("Groups", includedGroups.toString());
		}
		
		/*extent.setSystemInfo("ComputerName", "localhost");
		extent.setSystemInfo("Tester Name", "Devayan");
		*/

	}

	public void onTestSuccess(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName()); // create a new entry in the report
		test.assignCategory(result.getMethod().getGroups()); //to display groups in report
		test.log(Status.PASS, "Test case Passed is: " + result.getName()); // update status p/f/s
	}

	public void onTestFailure(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName()); // create a new entry in the report
		test.assignCategory(result.getMethod().getGroups());
		
		test.log(Status.FAIL,result.getName()+ "got failed"); // update status p/f/s
		test.log(Status.INFO,result.getThrowable().getMessage()); // show error message in reports.
	    
		/*try
		{
	     String imgPath = new BaseClass().captureScreen(result.getName());
	     test.addScreenCaptureFromPath(imgPath);
		} catch(Exception e1) {
			e1.printStackTrace();
		}*/
	     
	  }

	public void onTestSkipped(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName()); // create a new entry in the report
		test.assignCategory(result.getMethod().getGroups());

		test.log(Status.SKIP,result.getName()+" got skipped"); // update status p/f/s
		test.log(Status.INFO,result.getThrowable().getMessage());
	}

	public void onFinish(ITestContext context) {
		extent.flush();
		
		//Automatically open reports after execution
		String pathOfExtentReport = System.getProperty("user.dir")+"\\reports\\" + repName;
		File extentReport = new File(pathOfExtentReport);
		
		try {
			Desktop.getDesktop().browse(extentReport.toURI());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		/*
		
		try {
			URL url= new URL("file://"+System.getProperty("user.dir")+"\\reports\\"+repName);
		
			//Create email message only for Gmail
			
			ImageHtmlEmail email = new ImageHtmlEmail();
			
			email.setDataSourceResolver(new DataSourceUrlResolver(url));
			email.setHostName("smtp.googlemail.com");
			email.setSmtpPort(465);
			email.setAuthenticator(new DefaultAuthenticator("@gmail.com","password"));
			email.setSSLOnConnect(true); 
			email.setFrom("devayan@gmail.com"); //Sender
			
			email.setSubject("TestResults");
			email.setMsg("Please find Attached Report....");
			email.addTo("devayan.busyqa@gmail.com");  //Receiver
			email.attach(url,"extent report","please check report..."); 
			email.send(); //send the email
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		*/
		
		
		
		
		
	}

}
