package Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lib to contains all the constants which will be used during test execution
 */
public class Constant {

	public static final String Sourceloc = System.getProperty("user.dir");

	// Test location parameters for test suit and support files
	public static String chromedriverlocation = Sourceloc + "\\driver\\chromedriver.exe";
	public static String androidpath = Sourceloc + "\\driver\\selendroid-standalone-0.12.0-with-dependencies.jar";
	public static String IEDRIVER = Sourceloc + "\\driver\\IEDriverServer.exe";
	public static String Testdataloc = Sourceloc + "\\src\\DataEngine\\";
	public static String testSuitWorkbook = Testdataloc + "Testsuit.xlsx";

	// Test files configuration parameters
	public static String TestSuitsheet = "Testsuit";
	public static String reportsheet = "Reports";
	public static String Dreportsheet = "";
	public static String TestScriptSheet;
	public static String TestScriptWorkbook;
	public static int testcaserownum;

	// Testcase sheet configuration
	public static int Testcaseid = 0;
	public static int TestStepID = 1;
	public static int TeststepDescription = 2;
	public static int ElementFinderType = 3;
	public static int Elementlocation = 4;
	public static int ActionSupportValue = 5;
	public static int Action = 6;
	public static int Data;
	public static List<Integer> datacolumnnumber = new ArrayList<Integer>();

	public static HashMap<String, String> TestSuit = new HashMap<String, String>();
	public static HashMap<String, String> TestCaseRow = new HashMap<String, String>();

	// Test Configuration Parameters
	// Webdriver wait parameters Unit sec:
	public static boolean ThreadWait = false;
	public static int ExplicitWait = 50;
	public static int ExplicitWaitif = 5;
	public static int ImplicitWait = 0;
	public static int PageLoadWait = 120;
	public static boolean Fallbackmechinisum = true;
	// Ajax Wait Parameters
	public static boolean WaitforAjax = true;
	public static int AjaxWait = 04;
	// Alert wait parameters Unit sec:
	public static boolean WaitforAlert = true;
	public static int Alertwait = 10;
	public static boolean AdsHelpControl = true;

	// WindowSwitch wait parameter Unit sec:
	public static boolean WindowWait = true;
	public static int WindowwaitMaxDuration = 10;
	// DB wait parameters
	// DBLagTimeDuration should be in Mili Secs
	public static int DBLagTimeDuration = 25000;
	// SubmitDBCaptureLagTimeduration should be in Milli Secs
	public static int SubmitDBCaptureLagTimeduration = 8000;
	public static boolean DBLagTime = true;
	public static boolean SubmitDBCaptureLagTime = true;

	// Browsers Configuration
	public static boolean FirefoxTabprefrence = true;
	public static boolean ChromeExtEnable = false;
	public static boolean IECleanHistoryFlag = true;
	public static int ConfigNoOfRow = 19;
	public static boolean highlighter = true;
	public static boolean Temphighlighter = highlighter;
	public static int browsercount;
	public static String Browser[];

	// Jira Util Support
	public static String JiraBaseURL = "http://localhost:8090";
	public static String JiraUserName = "munish.gupta@puresoftware.com";
	public static String JiraPassword = "munish123";
	public static String JiraProjectName = "MBKEY";
	public static boolean defectcontrol = false;

	public static boolean commonsteps = false;

	// Logging Section
	public static String LogFileLocation = Sourceloc + "\\src\\Logs\\";
	public static int ExtReportloglevel = 1;
	public static boolean ExcelReport = true;
	public static int ExcelReportColumnNO = 7;
	public static String ExcelReportLocation = "";
	// Set 3 for runtime logging
	public static int loglevel = 1;
	public static boolean HTMLREPORTING = true;
	public static boolean ErrorScreenShotFlag = true;
	public static String dataformate = "dd/M/yyyy";
	public static Map<String, String> SystemDetails = new HashMap<String, String>();
	public static String testreport = Sourceloc + "\\src\\reports\\";
	public static String defaulterrorimage = Sourceloc + "\\src\\Configuration\\DefaultError.jpg";
	public static String screenshotloc = Sourceloc + "\\src\\Screenshots\\";
	// Video Recording Controls
	public static boolean VideoRecording = false;
	public static String videofilelocation = Sourceloc + "\\src\\Video\\";
	public static String videofilename;

	// Sikuli Controls:
	public static final String SikuliImageLocation = Sourceloc + "\\src\\Sikuli\\images\\";
	public static String SikuliIDE = Sourceloc + "\\SikuliX\\runsikulix.bat";
	public static boolean sikuliHighlighter = true;

	public static boolean test = false;

	// AutoitExeLocation
	public static final String autoit = Sourceloc + "\\src\\Autoit\\";

	// Object Repository Controls
	public static final String ObjRepName = Sourceloc + "\\src\\DataEngine\\ObjRep.xlsx";
	public static final String ObjRepSheetname = "ObjRep";
	public static final int ObjRepelement = 2;
	public static boolean BuildObjrep = true;
	public static ConcurrentHashMap<String, String> ObjrepLoc = new ConcurrentHashMap<String, String>();
	public static ConcurrentHashMap<String, String> ObjrepType = new ConcurrentHashMap<String, String>();

	// mail settings
	/*
	 * These setting is for sonar pdf attachment.
	 * 1. SEND_BATCH must be true to send an attachment(s) 
	 * 2. BATCH_LOCATION is the absolute path of the folder , which contains should be zipped.
	 * 3. ZIPPED_LOCATION denotes the zip file location and it should be absolute location on the system. 
	 * 4. ZIPPED_FILENAME denotes the zip file name.
	 */
	public static boolean SEND_BATCH = true;
	public static String BATCH_LOCATION = Sourceloc + "\\src\\Logs\\"; 
	public static String ZIPPED_LOCATION = Sourceloc + "\\Archive\\";
	public static String ZIPPED_FILENAME = "Batch";

	public static boolean EnableEmail = true;
	public static String EmailType = null;// can be select from Gamil/user
											// hosted smtp email.
	public static String EmailBody = "Hello All, " + "\r\n" + "\r\n"
			+ "Automation has been completed for the requested run FYI." + "\r\n" + "Folder : " + BATCH_LOCATION
			+ " has beed added to location :"+ ZIPPED_LOCATION;
	
	public static String EmailBody1 = "after zipping." + "\r\n"
			+ "Please refer attached report for detail." + "\r\n" + "\r\n" + "Thanks & Regards," + "\r\n"
			+ "Magic Bricks Automation Team" + "\r\n\r\n\r\n\r\n\r\n\r\n"
			+ "Note: This is an automated e-mail of Automation Test result reports. Hence, please do not reply to this e-mail. The alert has been sent to the registered e-mail ID. In case you wish to register an alternate e-mail ID, please Contact MagicBricks Autoamtion team: vikrant.chutani@timesgroup.com";
	public static String EmailBodyWithoutZipAttachment = "Hello All, " + "\r\n" + "\r\n"
			+ "Automation has been completed for the requested run." + "\r\n"
			+ "Please refer attached report for detail." + "\r\n" + "\r\n" + "Thanks & Regards," + "\r\n"
			+ "Magic Bricks Automation Team" + "\r\n\r\n\r\n\r\n\r\n\r\n"
			+ "Note: This is an automated e-mail of Automation Test result reports. Hence, please do not reply to this e-mail. The alert has been sent to the registered e-mail ID. In case you wish to register an alternate e-mail ID, please Contact MagicBricks Autoamtion team: vikrant.chutani@timesgroup.com";
	
	
	public static Map<String, String> EmailConfiguration = new HashMap<String, String>();
	public static String AttachmentReport;
	public static String AttachmentLog = null;
	public static String EmailSubject = "MagicBricks Automation Report";
	public static final String emailUsername = "ajaychadda@gmail.com";
	public static final String emailPassword = "mickeybunny";

	// Please use ";" as a separator for more then one recipient.
	public static String tolist = "vikrant.chutani@timesgroup.com;megha.sapra@timesgroup.com;sumit.tekriwal@timesgroup.com";
	public static String CClist = "";

	// Mobile Platform Configuration Parameters
	public static Map<String, String> MobilesetCapability = new HashMap<String, String>();

	// Database Connection Details:

	// Type DB2 connection details;
	public static String DB2IP = "115.112.206.218";
	public static String DB2Port = "60000";
	public static String DB2DatabaseName = "mbdeploy";
	public static String DB2UserName = "property";
	public static String DB2Password = "property";
	// If Schema is not present please remove the values and leave it as "";
	public static String Schema = "property";

	public static int rerun = 0;
}
