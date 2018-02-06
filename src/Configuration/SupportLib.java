package Configuration;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.basics.Settings;

import com.google.common.collect.Ordering;

import TestRunner.TestCaseRunner;
import Util.ExtentLogs;
import Util.Log;

public class SupportLib {
	static String CHAR_LIST = "abcdefg123450hijklmn1567890opqrstuvw12390xyzABCDEFGHI1234567890JKLMNO12390PQRSTUV1234567890WXYZ67890";
	static String STRLIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static String NUM_LIST = "1234234567890234567890234567890789023456";
	static String SPL_LIST = "abcde!@#$@%fghijklmnopqrst!@#$!@uvyzAL&^%$EMNUV&*$%^WXYZ";
	static ChromeOptions options = new ChromeOptions();

	/**
	 * Function to extract double value from string page test Fields required:
	 */
	public static double ExtractDouble(String value) {
		Double f = Double.valueOf(value.replaceAll("[^\\d.]+|\\.(?!\\d)", ""));
		return f;
	}

	/**
	 * Function to extract Int value from string page test Fields required:
	 */
	public static int ExtractInt(String frompage) {
		String fetchfrompage = frompage;
		fetchfrompage = fetchfrompage.replaceAll("[^\\d.]+|\\.(?!\\d)", "");
		int rangefromwebpage;
		if (fetchfrompage.isEmpty()) {
			rangefromwebpage = 0;
		} else {
			rangefromwebpage = Integer.valueOf(fetchfrompage);
		}
		return rangefromwebpage;
	}

	/**
	 * Function to extract number from Price range value from string page test
	 * Fields required:
	 */
	public static List<Integer> ExtractRange(String frompage) {
		Log.debug("SupportLib:ExtractRange for String" + frompage);

		List<Integer> Range = new ArrayList<Integer>();
		List<String> a1 = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(frompage, "-");
		while (st.hasMoreTokens()) {
			a1.add(st.nextToken());
			Log.debug("SupportLib:ExtractRange Value has been added in the range" + st);
		}
		for (String basevalue : a1) {
			Log.debug("SupportLib:ExtractRange Processing values from the range calling process num function"
					+ basevalue);
			int i = processnumbers(basevalue);
			Log.debug("SupportLib:ExtractRange number is added in the range to retunr" + i);
			Range.add(i);
		}
		return Range;
	}

	/**
	 * Function to Highlight the element on the page
	 * 
	 */
	public static void ElementHighlighter(WebElement we) {
		if (we == null) {
			return;
		}

		if (Constant.highlighter == true) {
			try {
				JavascriptExecutor js = (JavascriptExecutor) ActionKeyword.driver;
				js.executeScript("arguments[0].style.backgroundColor = '#F2E898';", we);
				js.executeScript("arguments[0].style.border = '10px DarkOrange';", we);
			} catch (Exception e) {
				return;
			}
		}
	}

	/**
	 * Function is not a Lib Function used for error screenshots automatically
	 * for logs Fields required: N/A Test data: N/A
	 */
	public static String createScreenshot() {
		Log.debug("SupportLib>Create Screenshot:Initialing image capture function for action");
		String action = Constant.TestCaseRow.get("Action");
		Log.debug("SupportLib>Create Screenshot:Initialing image capture function for action" + action);
		String imageLocation = null;
		if (action.contains("Sclick") || action.contains("SDclick") || action.contains("Sfind")
				|| action.contains("Stype") || action.contains("OpenBrowser")) {
			Log.debug("SupportLib>Create Screenshot:IN the block for sikuli Sclick SDclick Sfind Stype OpenBrowser");
			imageLocation = Constant.screenshotloc + Constant.TestCaseRow.get("Testcaseid") + "_"
					+ Constant.TestCaseRow.get("TestStepID") + ".png";
			imageLocation = imageLocation.replace("#", "_");
			Log.debug("SupportLib>Create Screenshot:Image location string is created" + imageLocation);
			Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			try {
				BufferedImage capture = new Robot().createScreenCapture(screenRect);
				ImageIO.write(capture, "png", new File(imageLocation));
			} catch (Exception e) {
				Log.debug("SupportLib>Create Screenshot:Error during capture images for sikuli function");
				Log.fatal("Error in screenshot capture" + e.toString());

			}
		} else {
			Log.debug("SupportLib>Create Screenshot:Block for every other screenshots");
			UUID uuid = UUID.randomUUID();
			try {
				Log.debug("SupportLib>Create Screenshot:Creating src file");
				File scrFile = ((TakesScreenshot) ActionKeyword.driver).getScreenshotAs(OutputType.FILE);

				imageLocation = Constant.screenshotloc + Constant.TestCaseRow.get("Testcaseid") + "_"
						+ Constant.TestCaseRow.get("TestStepID") + ".png";

				imageLocation = imageLocation.replace("#", "_");
				Log.debug(
						"SupportLib>Create Screenshot:Preparing image location for all other function except sikuli function and openbrowser"
								+ imageLocation);
				FileUtils.copyFile(scrFile, new File(imageLocation));

			} catch (IOException e) {
				Log.debug("SupportLib>Create Screenshot:IO error during creating file" + e.toString());
				return Constant.defaulterrorimage;

			} catch (Exception e) {
				Log.debug("SupportLib>Create Screenshot:All other exception during image capture using driver"
						+ e.toString());
				Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
				try {
					BufferedImage capture = new Robot().createScreenCapture(screenRect);
					ImageIO.write(capture, "png", new File(Constant.screenshotloc + "\\" + uuid + ".png"));
				} catch (Exception f) {
					Log.fatal("Error in capturing image" + f.toString());
					return Constant.defaulterrorimage;
				}
			} catch (Error e) {
				imageLocation = Constant.screenshotloc + Constant.TestCaseRow.get("Testcaseid") + "_"
						+ Constant.TestCaseRow.get("TestStepID") + ".png";
				imageLocation = imageLocation.replace("#", "_");
				Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

				try {
					BufferedImage capture = new Robot().createScreenCapture(screenRect);
					ImageIO.write(capture, "png", new File(imageLocation));
				} catch (Exception g) {
					Log.fatal("Error in capturing image" + g.toString());
					return Constant.defaulterrorimage;
				}
			}
		}

		if (imageLocation == null) {
			imageLocation = Constant.defaulterrorimage;
		}
		return imageLocation;
	}

	public static String RandomGenerator(int RANDOM_STRING_LENGTH, int mod) {
		StringBuffer randStr = new StringBuffer();
		
		for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {

			int number = getRandomNumber(mod);
			if (mod == 1) {
				char ch = STRLIST.charAt(number);
				randStr.append(ch);
				
			} else if (mod == 2) {
				char ch = NUM_LIST.charAt(number);
				randStr.append(ch);
				
			} else if (mod == 3) {
				char ch = CHAR_LIST.charAt(number);
				randStr.append(ch);
				
			} else if (mod == 4) {
				char ch = SPL_LIST.charAt(number);
				randStr.append(ch);
			}
		}
		return randStr.toString();
	}

	/**
	 * This method generates random numbers
	 */
	public static int getRandomNumber(int mod) {
		int randomInt = 0;
		Random randomGenerator = new Random();

		if (mod == 1) {
			randomInt = randomGenerator.nextInt(STRLIST.length());
		} else if (mod == 2) {
			randomInt = randomGenerator.nextInt(NUM_LIST.length());
		} else if (mod == 3) {
			randomInt = randomGenerator.nextInt(CHAR_LIST.length());
		} else if (mod == 4) {
			randomInt = randomGenerator.nextInt(SPL_LIST.length());
		}

		if (randomInt - 1 == -1) {
			return randomInt;
		} else {
			return randomInt - 1;
		}
	}

	public static void CaliberateIE() {
		Log.debug("SupportLib>CaliberateIE: Calibrating IE screen size for specific mouse hover function");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Log.debug("SupportLib>CaliberateIE: Following screen size is capture" + screenSize);
		JavascriptExecutor js = (JavascriptExecutor) ActionKeyword.driver;
		String jsString = "document.getElementById('main').innerHTML = Math.max(document.documentElement.clientHeight, window.innerHeight || 0)";
		js.executeScript(jsString);
		String BrowserHight = ActionKeyword.driver.findElement(By.xpath(".//p[@id='main']")).getText();
		Log.debug("SupportLib>CaliberateIE: Java script is executed to edit main element for browser hight"
				+ BrowserHight);
		jsString = "document.getElementById('main').innerHTML = Math.max(document.documentElement.clientWidth, window.innerWidth || 0)";
		js.executeScript(jsString);
		String BrowserWidth = ActionKeyword.driver.findElement(By.xpath(".//p[@id='main']")).getText();
		Log.debug("SupportLib>CaliberateIE: Java script is executed to edit main element for browser widht"
				+ BrowserWidth);
		int browserw = Integer.parseInt(BrowserWidth);
		int browserh = Integer.parseInt(BrowserHight);
		int screenw = (int) screenSize.getWidth();
		int screenh = (int) screenSize.getHeight();
		ActionKeyword.IEOffsetx = screenw - browserw;
		ActionKeyword.IEOffsety = screenh - browserh;
		Log.debug("SupportLib>CaliberateIE:Real Screen size" + screenSize + "Browser Hight" + BrowserHight
				+ "Browser Widht " + BrowserWidth);
	}

	public static void browserconfig(String Browser) {
		try {
			if (Browser.contains("FireFox")) {
				Robot robot = new Robot();
				robot.delay(40);
				robot.keyPress(KeyEvent.VK_ALT);
				robot.keyPress(KeyEvent.VK_T);
				robot.keyRelease(KeyEvent.VK_ALT);
				robot.keyRelease(KeyEvent.VK_T);
				robot.delay(1000);
				robot.keyPress(KeyEvent.VK_O);
				robot.keyRelease(KeyEvent.VK_O);
				robot.delay(1000);
				robot.keyPress(KeyEvent.VK_RIGHT);
				robot.keyRelease(KeyEvent.VK_RIGHT);
				robot.delay(1000);
				robot.keyPress(KeyEvent.VK_TAB);
				robot.keyRelease(KeyEvent.VK_TAB);
				robot.delay(1000);
				robot.keyPress(KeyEvent.VK_SPACE);
				robot.keyRelease(KeyEvent.VK_SPACE);
				robot.delay(1000);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				robot.delay(1000);
			}
		} catch (AWTException e) {
			Log.error(e.getStackTrace().toString());
			ExtentLogs.error(e.getStackTrace().toString());
		}

		options.addArguments("--always-authorize-plugins=true");
		options.setExperimentalOption("excludeSwitches", Arrays.asList("test-type"));
		System.setProperty("webdriver.chrome.driver", Constant.chromedriverlocation);
	}

	public static String CopyClipboard() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = toolkit.getSystemClipboard();
		String result = "";
		if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor))
			try {
				result = (String) clipboard.getData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException | IOException e) {
				Log.error(e.getStackTrace().toString());
				ExtentLogs.error(e.getStackTrace().toString());
			}
		if (result == null || result == "") {
			result = "Blank";
		}
		return result;
	}

	public static void clearClipboard() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = toolkit.getSystemClipboard();
		clipboard.setContents(new Transferable() {

			@Override
			public boolean isDataFlavorSupported(DataFlavor flavor) {
				return false;
			}

			@Override
			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[0];
			}

			@Override
			public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
				throw new UnsupportedFlavorException(flavor);
			}
		}, null);
	}

	public static void SelectAll() {
		try {
			Robot robot = new Robot();
			robot.delay(40);
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_A);
			robot.delay(1000);
			robot.keyRelease(KeyEvent.VK_A);
			robot.keyRelease(KeyEvent.VK_CONTROL);
		} catch (AWTException e) {
			Log.error(e.getStackTrace().toString());
			ExtentLogs.error(e.getStackTrace().toString());
		}

	}

	public static void SikuliShowActions() {
		if (Constant.sikuliHighlighter == true) {
			Settings.setShowActions(Constant.sikuliHighlighter);
		} else {
		}
	}

	public static void KeyboardCopy() {
		Robot robot;
		try {
			robot = new Robot();
			robot.delay(40);
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_C);
			robot.delay(1000);
			robot.keyRelease(KeyEvent.VK_C);
			robot.keyRelease(KeyEvent.VK_CONTROL);
		} catch (AWTException e) {
			Log.error(e.getStackTrace().toString());
			ExtentLogs.error(e.getStackTrace().toString());
		}
	}

	public static WebElement SearchObjRep() {

		WebElement we = null;
		String Key = Constant.TestCaseRow.get("Elementlocation");
		String Loc = "", Type = "";
		try {
			Loc = Constant.ObjrepLoc.get(Key);
			Type = Constant.ObjrepType.get(Key);
		} catch (Exception e) {

		}
		if (Loc == null || Type == null) {
			String fatalMessage = "Framework is not able to find Object defination in ObjRep for the Key" + Key;
			Log.fatal(fatalMessage);
			TestCaseRunner.testScriptResult = false;

		}
		if (Type.equalsIgnoreCase("Xpath")) {
			we = SupportLib.FindElement(1, Loc);
		} else if (Type.equalsIgnoreCase("ID")) {
			we = SupportLib.FindElement(2, Loc);
		} else if (Type.equalsIgnoreCase("CSS")) {
			we = SupportLib.FindElement(3, Loc);
		} else if (Type.equalsIgnoreCase("Link")) {
			we = SupportLib.FindElement(4, Loc);
		} else if (Type.equalsIgnoreCase("Name")) {
			we = SupportLib.FindElement(5, Loc);
		} else if (Type.equalsIgnoreCase("Class")) {
			we = SupportLib.FindElement(6, Loc);
		} else if (Type.equalsIgnoreCase("partialLink")) {
			we = SupportLib.FindElement(7, Loc);
		} else if (Type.equalsIgnoreCase("tagName")) {
			we = SupportLib.FindElement(8, Loc);
		}
		return we;

	}

	public static List<WebElement> SearchObjReps() {

		List<WebElement> we = null;
		String Key = Constant.TestCaseRow.get("Elementlocation");
		;
		String Loc = Constant.ObjrepLoc.get(Key);
		String Type = Constant.ObjrepType.get(Key);

		if (Type.equalsIgnoreCase("Xpath")) {
			we = SupportLib.FindElements(1, Loc);
		} else if (Type.equalsIgnoreCase("ID")) {
			we = SupportLib.FindElements(2, Loc);
		} else if (Type.equalsIgnoreCase("CSS")) {
			we = SupportLib.FindElements(3, Loc);
		} else if (Type.equalsIgnoreCase("Link")) {
			we = SupportLib.FindElements(4, Loc);
		} else if (Type.equalsIgnoreCase("Name")) {
			we = SupportLib.FindElements(5, Loc);
		} else if (Type.equalsIgnoreCase("Class")) {
			we = SupportLib.FindElements(6, Loc);
		} else if (Type.equalsIgnoreCase("partialLink")) {
			we = SupportLib.FindElements(7, Loc);
		} else if (Type.equalsIgnoreCase("tagName")) {
			we = SupportLib.FindElements(8, Loc);
		}
		return we;

	}

	public static WebElement FindElement(int i, String Loc) {
		WebDriverWait wait = null;

		Log.debug("SupportLib>FindElement: Starting of FindElement function Defining wait for element condition is:"
				+ Constant.ExplicitWait);
		wait = new WebDriverWait(ActionKeyword.driver, Constant.ExplicitWait);
		WebElement we = null;

		try {
			switch (i) {
			case 1:
				Log.debug("SupportLib>FindElement: Selected Case 1 search By Xpath for the element");
				we = (wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(Loc))));

				break;
			case 2:
				Log.debug("SupportLib>FindElement: Selected Case 2 search By ID for the element");
				we = (wait.until(ExpectedConditions.presenceOfElementLocated(By.id(Loc))));
				break;
			case 3:
				Log.debug("SupportLib>FindElement: Selected Case 3 search By CSS for the element");
				we = (wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(Loc))));
				break;
			case 4:
				Log.debug("SupportLib>FindElement: Selected Case 4 search By LinkText for the element");
				we = (wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(Loc))));
				break;
			case 5:
				Log.debug("SupportLib>FindElement: Selected Case 5 search By Name for the element");
				we = (wait.until(ExpectedConditions.presenceOfElementLocated(By.name(Loc))));
				break;
			case 6:
				Log.debug("SupportLib>FindElement: Selected Case 6 search By ClassName for the element");
				we = (wait.until(ExpectedConditions.presenceOfElementLocated(By.className(Loc))));
				break;
			case 7:
				Log.debug("SupportLib>FindElement: Selected Case 7 search By PartialLink Text for the element");
				we = (wait.until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(Loc))));
				break;
			case 8:
				Log.debug("SupportLib>FindElement: Selected Case 4 search By TagName for the element");
				we = (wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName(Loc))));
				break;

			}
		} catch (Exception e) {
			Log.debug("SupportLib>FindElement: Web Element is not found returning Null for the webelement");
			return null;
		}
		Log.debug("SupportLib>FindElement: Web Element found returning value for the webelement");
		return we;

	}

	public static void countbrowser() {
		try {
			String browser = Constant.TestSuit.get("Browser");
			if (browser == "Blank") {
				Constant.Browser = null;
				return;
			}

			Constant.Browser = browser.split(",");
			Constant.browsercount = Constant.Browser.length;
		} catch (Exception e) {
			Log.fatal("Error in counting browser" + e.toString());
		}
	}

	public static void updateSystemDetails() {

		Constant.SystemDetails.put("ExtentReportTitle", "Automation report");
		Constant.SystemDetails.put("ExtentReportName", "Functional Automation");
		Constant.SystemDetails.put("ExtentReportHeadline", "Report");
		Constant.SystemDetails.put("Selenium Version", "2.45");
		Constant.SystemDetails.put("Environment", "Automation");
		Constant.SystemDetails.put("ReportVersion", "2.04");
		Constant.SystemDetails.put("Sikuli Version", "2.0");
		if (Constant.defectcontrol == true) {
			Constant.SystemDetails.put("Jira Base URL", Constant.JiraBaseURL);
			Constant.SystemDetails.put("Jira Project Name", Constant.JiraProjectName);
		}
		if (Constant.VideoRecording == true) {
			Constant.SystemDetails.put("VideoBase URL", Constant.videofilelocation);
		}
		Constant.SystemDetails.put("Automation Base Location", Constant.Sourceloc);
	}

	public static boolean checknumbersort(List<Integer> range, String Order) {
		Log.debug("SupportLib:CheckNumberSort started");
		boolean sorted = false;

		for (int i : range) {
			Log.debug("SupportLib:CheckNumberSort for range" + i);
		}
		if (Order.equalsIgnoreCase("Asc")) {
			Log.debug("SupportLib:CheckNumberSort Order is ASC");
			sorted = Ordering.natural().isOrdered(range);
			return sorted;
		}

		else if (Order.equalsIgnoreCase("Dsc")) {
			sorted = Ordering.natural().reverse().isOrdered(range);
		}
		return sorted;
	}

	public static boolean checkstringsort(List<String> range, String Order) {

		boolean result = false;

		String strObj1;
		String strObj2;

		int retval;
		for (int i = 0; i <= range.size(); i++) {

			strObj1 = range.get(i);
			strObj2 = range.get(i + 1);
			retval = strObj1.compareToIgnoreCase(strObj2);
			// System.out.println(strObj1+"***********"+strObj2+"^^^^^^^^^^^^^^"+retval);

			if (Order.equalsIgnoreCase("Asc") && retval <= 0) {
				result = true;
			} else if (Order.equalsIgnoreCase("Dsc") && retval >= 0) {
				result = true;
			} else {
				result = false;
				break;
			}
			if (i == range.size() - 2) {
				break;
			}
		}
		return result;

	}

	public static boolean checkDatesort(List<String> range, String Order) {

		boolean result = false;

		List<Date> date = new ArrayList<Date>();
		if (range.size() == 0) {
			return false;
		}

		try {
			for (int i = 0; i < range.size(); i++) {
				if (range.get(i).isEmpty()) {
					continue;
				}
				DateFormat df = new SimpleDateFormat("ddMMM");
				String split = range.get(i).replace("Posted: ", "");

				if (split.contains("Today")) {

					Date today = new Date();
					split = df.format(today);
					date.add(df.parse(split));
				} else if (split.contains("Yesterday")) {

					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DATE, -1);
					split = df.format(cal.getTime());
					date.add(df.parse(split));
				} else if (split.contains("st") || split.contains("nd") || split.contains("rd")
						|| split.contains("th")) {
					split = split.replace("st", "");
					split = split.replace("th", "");
					split = split.replace("nd", "");
					split = split.replace("rd", "");
					split = split.replace(" ", "");
					// split= df.format(split);
					date.add(df.parse(split));
				} else {
					System.out.println("Not able to convert date");
				}
			}

		} catch (ParseException e) {
			Log.error("Unable to Parse : " + e.getStackTrace());
			ExtentLogs.error("Unable to Parse : " + e.getStackTrace());
		}
		if (Order.equalsIgnoreCase("Asc")) {
			for (int j = 0; j < date.size(); j++) {
				if (j + 1 == date.size()) {
					break;
				}
				Date date1 = date.get(j);
				Date date2 = date.get(j + 1);
				if (date1.after(date2) || date1.equals(date2)) {
					result = true;
				} else {
					result = false;
				}
			}

		}

		else if (Order.equalsIgnoreCase("Dsc")) {
			for (int j = 0; j < date.size(); j++) {
				if (j + 1 == date.size()) {
					break;
				}
				Date date1 = date.get(j);
				Date date2 = date.get(j + 1);
				if (date1.before(date2) || date1.equals(date2)) {
					result = true;
				} else {
					result = false;
				}
			}
		}
		return result;
	}

	public static int Extracttooltip(String value) {
		int i = 0;
		String fetchfrompage = value;

		if (value.contains("Increased")) {

			value = spliter(fetchfrompage, "//%", "Increased");
			i = ExtractInt(value);
		} else if (value.contains("Decrease")) {

			value = spliter(fetchfrompage, "//%", "Decrease");
			i = ExtractInt(value);
			i = i * -1;
		} else if (value.contains("Ranked")) {
			value = spliter(fetchfrompage, ",", "Ranked");
			value = spliter(value, "out", "Ranked");
			i = ExtractInt(value);
		}
		return i;
	}

	private static String spliter(String mainString, String regx, String logic) {
		StringTokenizer st = new StringTokenizer(mainString, regx);
		String retvalue = null;
		List<String> splitervalue = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			splitervalue.add(st.nextToken());
		}

		for (String lo : splitervalue) {

			if (lo.contains(logic)) {
				retvalue = lo;
			} else
				continue;
		}

		return retvalue;
	}

	public static List<WebElement> FindElements(int i, String loc) {

		Log.debug("SupportLib>FindElements: Starting of FindElements function Defining wait for element condition is:"
				+ Constant.ExplicitWait);
		WebDriverWait wait = new WebDriverWait(ActionKeyword.driver, Constant.ExplicitWait);

		List<WebElement> we = new ArrayList<WebElement>();
		we = null;
		try {
			switch (i) {
			case 1:
				Log.debug("SupportLib>FindElements: Selected Case 1 search By Xpath for the elements");
				we = (wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(loc))));
				break;
			case 2:
				Log.debug("SupportLib>FindElements: Selected Case 2 search By ID for the elements");
				we = (wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id(loc))));
				break;
			case 3:
				Log.debug("SupportLib>FindElements: Selected Case 3 search By CSS for the elements");
				we = (wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(loc))));
				break;
			case 4:
				Log.debug("SupportLib>FindElements: Selected Case 4 search By Link Text for the elements");
				we = (wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.linkText(loc))));
				break;
			case 5:
				Log.debug("SupportLib>FindElements: Selected Case 5 search By Name for the elements");
				we = (wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.name(loc))));
				break;
			case 6:
				Log.debug("SupportLib>FindElements: Selected Case 6 search By Class Name for the elements");
				we = (wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className(loc))));
				break;
			case 7:
				Log.debug("SupportLib>FindElements: Selected Case 7 search By Partial Link for the elements");
				we = (wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.partialLinkText(loc))));
				break;
			case 8:
				Log.debug("SupportLib>FindElements: Selected Case 8 search By tag name for the elements");
				we = (wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName(loc))));
				break;
			}
		} catch (Exception e) {
			Log.debug("SupportLib>FindElements: Web Element is not found returning Null for the webelement");
			return we;
		}
		Log.debug("SupportLib>FindElements: Web Element is not found returning List of the webelement having # "
				+ we.size() + " # Elements in it");
		return we;

	}

	public static void waitForAjax() {

		if (Constant.AdsHelpControl) {
			AdsHelpControl();
		}
		if (Constant.WaitforAjax) {
			try {
				if (ActionKeyword.driver instanceof JavascriptExecutor) {
					JavascriptExecutor jsDriver = (JavascriptExecutor) ActionKeyword.driver;
					for (int i = 0; i < Constant.AjaxWait; i++) {
						Object numberOfAjaxConnections = jsDriver.executeScript("return jQuery.active");
						if (numberOfAjaxConnections instanceof Long) {
							Long n = (Long) numberOfAjaxConnections;
							if (n.longValue() == 0L)
								break;
						}
						Thread.sleep(1000);
					}
				} else {
				}
			} catch (InterruptedException e) {
				System.out.println(e);
			} catch (Exception e) {
				return;
			}
		} else {

		}
	}

	public static void AdsHelpControl() {

		if (ActionKeyword.activeframe.size() <= 0 || ActionKeyword.ActiveFrameElement.size() <= 0) {
			ActionKeyword.driver.switchTo().defaultContent();
		}

		try {

			if (ActionKeyword.driver.findElements(By.xpath(".//div[@class='coachMarkLayer']")).size() != 0
					&& ActionKeyword.driver.findElement(By.xpath(".//div[@class='coachMarkLayer']")).isDisplayed()) {
				Thread.sleep(8000);
				ActionKeyword.driver.navigate().refresh();

			}

		} catch (Exception e) {
			Log.debug("SupportLib>AdsHelpControl:#Ads Error in finding coachMarkLayer" + e.toString());
		}

		try {
			if (ActionKeyword.driver.findElements(By.xpath(".//div[@id='fred']")).size() != 0
					&& ActionKeyword.driver.findElement(By.xpath(".//div[@id='fred']")).isDisplayed()) {

				ActionKeyword.driver.findElement(By.xpath(".//div[@id='fred']//div[@id='mininize']/img")).click();
			}
		} catch (Exception e) {
			Log.debug("SupportLib>AdsHelpControl:#Ads Error in finding fred ads" + e.toString());
		}

		try {
			if (ActionKeyword.driver.findElements(By.xpath("//a[@id='survey-close-div']")).size() != 0
					&& ActionKeyword.driver.findElement(By.xpath("//a[@id='survey-close-div']")).isDisplayed()) {
				ActionKeyword.driver.findElement(By.xpath("//a[@id='survey-close-div']")).click();
			}
		} catch (Exception e) {
			Log.debug("SupportLib>AdsHelpControl:#Ads Error in finding survey block" + e.toString());
		}

		try {
			if (ActionKeyword.driver.findElements(By.xpath("//div[@id='intermidPage']")).size() != 0
					&& ActionKeyword.driver.findElement(By.xpath("//div[@id='intermidPage']")).isDisplayed()) {
				ActionKeyword.driver.findElement(By.xpath("//div[@id='minimize']")).click();
			}
		} catch (Exception e) {
			Log.debug("SupportLib>AdsHelpControl:#Ads Error in finding intermidPage" + e.toString());
		}

		try {

			if (ActionKeyword.driver.findElements(By.xpath(".//div[@id='flslider']//div[@class='agentSliderInner']"))
					.size() != 0
					&& ActionKeyword.driver
							.findElement(By.xpath(".//div[@id='flslider']//div[@class='agentSliderInner']"))
							.isDisplayed()) {

				ActionKeyword.driver.findElement(By.cssSelector("a.closeImg")).click();
			}
		} catch (Exception e) {
			Log.debug("SupportLib>AdsHelpControl:#Ads Error in finding agentSliderInner" + e.toString());
		}

		try {
			if (ActionKeyword.activeframe.size() > 0) {
				// System.out.println(ActionKeyword.driver.getPageSource());
				for (int i : ActionKeyword.activeframe) {
					ActionKeyword.driver.switchTo().frame(i);
				}
			}

		} catch (Exception e) {
			Log.debug("SupportLib>AdsHelpControl:#Ads Error in switching back frame frame number are "
					+ ActionKeyword.activeframe.size() + e.toString());
		}
		try {
			if (ActionKeyword.ActiveFrameElement.size() > 0) {
				for (WebElement i : ActionKeyword.ActiveFrameElement) {
					ActionKeyword.driver.switchTo().frame(i);
				}

			}
		} catch (Exception e) {
			Log.debug("SupportLib>AdsHelpControl:#Ads Error in switching back frame frame number are "
					+ ActionKeyword.ActiveFrameElement.size() + e.toString());
		}
	}

	public static WebElement SearhElement(List<WebElement> webElementCollection) {
		for (WebElement sr : webElementCollection) {

			String actionSupportValue = Constant.TestCaseRow.get("ActionSupportValue");
			String data = Constant.TestCaseRow.get("Data").toLowerCase();
			if (!actionSupportValue.contains("Blank")) {
				if (sr.getAttribute(actionSupportValue).toLowerCase().contains(data)) {
					return sr;
				} else {
					continue;
				}
			} else if (actionSupportValue.contains("Blank")) {
				if (sr.getText().toLowerCase().equals(data)) {
					return sr;

				} else {
					continue;
				}
			} else {
				break;
			}
		}
		return null;
	}

	public static List<String> GetElementText(List<WebElement> webElementCollection) {
		Log.debug("SupportLib>GetElement Text: Statring of function for list of element: #"
				+ webElementCollection.size());
		List<String> TextFromPageList = new ArrayList<String>();
		for (WebElement we : webElementCollection) {
			Log.debug("SupportLib>GetElement Text: Loop Started for the lost size: " + webElementCollection.size());
			if (we.getText() == "") {
				TextFromPageList.add("Blank:");
			} else {
				TextFromPageList.add(we.getText());
			}
		}

		return TextFromPageList;
	}

	public static List<String> GetElementAttribute(List<WebElement> webElementCollection, String Attribute) {
		Log.debug("SupportLib>GetElement Attrubute: Starting of the function ");
		List<String> TextFromPageList = new ArrayList<String>();
		for (WebElement we : webElementCollection) {
			Log.debug(
					"SupportLib>GetElement Attrubute: Loop Started for the lost size: " + webElementCollection.size());
			if (we.getAttribute(Attribute) == null) {
				Log.debug("SupportLib>GetElement Attrubute: No Text has been found for attribute"
						+ webElementCollection.toString());
				TextFromPageList.add("NoAttributeValueFound");
			} else {
				String Value = we.getAttribute(Attribute);
				Log.debug(Value + " SupportLib>GetElement Attrubute: Text has been found for attribute"
						+ webElementCollection.toString());
				if (Value == "") {
					Value = "Blank:";
				}
				TextFromPageList.add(Value);

			}

		}
		Log.debug("SupportLib>GetElement Attrubute: Returning a list of attribute have size of : #"
				+ TextFromPageList.size() + "# againest size of provied webelement collection: #"
				+ webElementCollection.size());
		return TextFromPageList;
	}

	public static List<String> ProcessData(String verification) {

		List<String> VerificationList = new ArrayList<String>();
		if (verification.contains(":")) {
			StringTokenizer st = new StringTokenizer(verification, ":");

			while (st.hasMoreTokens()) {
				VerificationList.add(st.nextToken());
			}

		}

		return VerificationList;
	}

	public static List<Date> getFormattedDateFromPage(List<String> ValuesCaptured) {
		List<Date> date = new ArrayList<Date>();

		try {

			for (int i = 0; i < ValuesCaptured.size(); i++) {
				String postDateFromPg = ValuesCaptured.get(i);

				DateFormat df = new SimpleDateFormat("ddMMM");
				String split = postDateFromPg.replace("Posted: ", "");

				if (split.contains("Today")) {

					Date today = new Date();
					split = df.format(today);
					date.add(df.parse(split));
				} else if (split.contains("Yesterday")) {

					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DATE, -1);
					split = df.format(cal.getTime());

					date.add(df.parse(split));

				} else if (split.contains("st") || split.contains("nd") || split.contains("rd")
						|| split.contains("th")) {
					split = split.replace("st", "");
					split = split.replace("th", "");
					split = split.replace("nd", "");
					split = split.replace("rd", "");
					split = split.replace(" ", "");

					date.add(df.parse(split));
				} else {
					System.out.println("Not able to convert date");
				}
			}

		} catch (ParseException e) {
			Log.error("Unable to Parse : " + e.getStackTrace());
			ExtentLogs.error("Unable to Parse : " + e.getStackTrace());
		}
		return date;
	}

		public static int processnumbers(String frompage) {
		Log.debug("SupportLib:processnumbers number is started for the string"
				+ frompage);

		if (frompage.contains("Cr")) {
			Log.debug("SupportLib:processnumbers number when string contains cr block"
					+ frompage);
			Double d = ExtractDouble(frompage) * 10000000;
			Log.debug("SupportLib:processnumbers double vlaue when string contains cr block*10000000"
					+ d);
			int i = (int) d.doubleValue();
			Log.debug("SupportLib:processnumbers int vlaue when string contains cr "
					+ i);
			return i;
		} else if (frompage.contains("Lac") || frompage.contains("L")) {
			Log.debug("SupportLib:processnumbers number when string contains Lac or L block"
					+ frompage);
			Double d = ExtractDouble(frompage) * 100000;
			Log.debug("SupportLib:processnumbers double vlaue when string contains Lac or L block*100000"
					+ d);
			int i = (int) d.doubleValue();
			Log.debug("SupportLib:processnumbers int vlaue when string contains Lac or L "
					+ i);
			return i;
		} else if (frompage.contains("per")||frompage.contains("Per")) {
			if (frompage.contains("acre")) {
				Log.debug("SupportLib:processnumbers number when string contains acre block"
						+ frompage);
				Double d = ExtractDouble(frompage) / 43560;
				Log.debug("SupportLib:processnumbers double vlaue when string contains acre block/ 43560"
						+ d);
				int i = (int) (d.doubleValue() * 10000);
				Log.debug("SupportLib:processnumbers int vlaue when string contains acre * 10000 "
						+ i);
				return i;
			} else if (frompage.contains("sqm")) {
				Log.debug("SupportLib:processnumbers number when string contains sqm block"
						+ frompage);
				Double d = ExtractDouble(frompage) / 10.76;
				Log.debug("SupportLib:processnumbers double vlaue when string contains sqm block/ 10.76"
						+ d);
				int i = (int) (d.doubleValue() * 10000);
				Log.debug("SupportLib:processnumbers double vlaue when string contains sqm block*10000"
						+ d);
				return i;
			} else if (frompage.contains("sqyrd")) {
				Log.debug("SupportLib:processnumbers number when string contains sqyrd block"
						+ frompage);
				Double d = ExtractDouble(frompage) / 9;
				int i = (int) (d.doubleValue() * 10000);
				Log.debug("SupportLib:processnumbers double vlaue when string contains sqyrd block/ 9"
						+ d);
				Log.debug("SupportLib:processnumbers int vlaue when string contains sqyrd * 10000 "
						+ i);
				return i;
			} else if (frompage.contains("hectare")) {
				Log.debug("SupportLib:processnumbers number when string contains hectare block"
						+ frompage);
				Double d = ExtractDouble(frompage) / 107642.62;
				int i = (int) (d.doubleValue() * 10000);
				Log.debug("SupportLib:processnumbers double vlaue when string contains hectare block / 107642.62"
						+ d);
				Log.debug("SupportLib:processnumbers int vlaue when string contains hectare * 10000 "
						+ i);
				return i;
			} else if (frompage.contains("kanal")) {
				Log.debug("SupportLib:processnumbers number when string contains kanal block"
						+ frompage);
				Double d = ExtractDouble(frompage) / 5445.20;
				int i = (int) (d.doubleValue() * 10000);
				Log.debug("SupportLib:processnumbers double vlaue when string contains kanal block/ 5445.20"
						+ d);
				Log.debug("SupportLib:processnumbers int vlaue when string contains kanal * 10000 "
						+ i);
				return i;
			}

			else if (frompage.contains("rood")) {
				Log.debug("SupportLib:processnumbers number when string contains rood block"
						+ frompage);
				Double d = ExtractDouble(frompage) / 117244.34;
				int i = (int) (d.doubleValue() * 10000);
				Log.debug("SupportLib:processnumbers double vlaue when string contains rood block/ 117244.34"
						+ d);
				Log.debug("SupportLib:processnumbers int vlaue when string contains rood * 10000 "
						+ i);
				return i;
			}

			else if (frompage.contains("are")) {
				Log.debug("SupportLib:processnumbers number when string contains are block"
						+ frompage);
				Double d = ExtractDouble(frompage) / 11582.34;
				int i = (int) (d.doubleValue() * 10000);
				Log.debug("SupportLib:processnumbers double vlaue when string contains are block/ 11582.34"
						+ d);
				Log.debug("SupportLib:processnumbers int vlaue when string contains are * 10000 "
						+ i);
				return i;
			}

			else if (frompage.contains("bigha")) {
				Log.debug("SupportLib:processnumbers number when string contains bigha block"
						+ frompage);
				Double d = ExtractDouble(frompage) / 26910.65;
				int i = (int) (d.doubleValue() * 10000);
				Log.debug("SupportLib:processnumbers double vlaue when string contains bigha block / 26910.65"
						+ d);
				Log.debug("SupportLib:processnumbers int vlaue when string contains bigha * 10000 "
						+ i);
				return i;

			}
			else if (frompage.contains("sqft")) {

	            Double d = ExtractDouble(frompage);
	            // System.out.println(i);
	            int i = (int) (d.doubleValue() * 10000);
	            // System.out.println(i);
	            return i;
	        }
			else {
				Log.debug("SupportLib:processnumbers number when string dose not contain any logic"
						+ frompage);
				frompage = frompage.replaceAll("\\D+", "");
				Log.debug("SupportLib:processnumbers number when string dose not contain any logic"
						+ frompage);
				int i = Integer.parseInt(frompage);
				Log.debug("SupportLib:processnumbers int vlaue when string dose not contain any logical values "
						+ i);
				return i;
			}
			
			
		}

		else if (frompage.contains("acre")) {
			Double d = ExtractDouble(frompage) * 43560;

			int i = (int) (d.doubleValue());
			return i;

		} else if (frompage.contains("sqm")) {

			Double d = ExtractDouble(frompage) * 10.76;

			int i = (int) (d.doubleValue());

			return i;
		}

		else if (frompage.contains("sqyrd")) {

			Double d = ExtractDouble(frompage) * 9;

			int i = (int) (d.doubleValue());

			return i;
		}

		else if (frompage.contains("hectare")) {

			Double d = ExtractDouble(frompage) * 107642.62;

			int i = (int) (d.doubleValue());

			return i;
		}

		else if (frompage.contains("kanal")) {

			Double d = ExtractDouble(frompage) * 5445.20;

			int i = (int) (d.doubleValue());

			return i;
		}

		else if (frompage.contains("rood")) {

			Double d = ExtractDouble(frompage) * 117244.34;

			int i = (int) (d.doubleValue());

			return i;
		}

		else if (frompage.contains("are")) {

			Double d = ExtractDouble(frompage) * 11582.34;

			int i = (int) (d.doubleValue());

			return i;
		}

		else if (frompage.contains("bigha")) {

			Double d = ExtractDouble(frompage) * 26910.65;

			int i = (int) (d.doubleValue());

			return i;
		}

		else if (frompage.contains("sqft")) {

			Double d = ExtractDouble(frompage);

			int i = (int) (d.doubleValue());

			return i;
		}

		else {
			Log.debug("SupportLib:processnumbers number when string dose not contain any logic"
					+ frompage);
			frompage = frompage.replaceAll("\\D+", "");
			Log.debug("SupportLib:processnumbers number when string dose not contain any logic"
					+ frompage);
			int i = Integer.parseInt(frompage);
			Log.debug("SupportLib:processnumbers int vlaue when string dose not contain any logical values "
					+ i);
			return i;

		}

	}
	public static void Mobileinfoupdate() {
		Constant.MobilesetCapability.put("browserName", "");
		Constant.MobilesetCapability.put("VERSION", "4.4");
		Constant.MobilesetCapability.put("platformName", "Android");
		Constant.MobilesetCapability.put("platformVersion", "4.4");
		Constant.MobilesetCapability.put("deviceName", "LS1437840422");
		Constant.MobilesetCapability.put("appPackage", "com.google.android.googlequicksearchbox");
		Constant.MobilesetCapability.put("AppiumURL", "http://127.0.0.1:4723/wd/hub");
		Constant.MobilesetCapability.put("appActivity", "activityName");
	}
}
