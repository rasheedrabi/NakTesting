package TestRunner;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;

import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.UnreachableBrowserException;

import Configuration.ActionKeyword;
import Configuration.Constant;
import Configuration.SupportLib;
import Util.ExcelReporting;
import Util.ExcelUtils;
import Util.ExtentLogs;
import Util.Log;
import Util.Mail_Util;
import Util.VideoRecorder;

public class TestCaseRunner {
	public static int testSuitindex, i, browsercount, testScriptIndex;
	public static boolean testSuitResult, testScriptResult;
	public static ExcelReporting rep = new ExcelReporting();
	public static File lDir = new File("");
	public static String absolutePath = lDir.getAbsolutePath();
	public static File folder1 = new File(Constant.testreport);
	public static String Endmessage, sActionKeyword, destpath;
	public static Method method[];
	public static ActionKeyword ActionKeywords;

	private static String infoMessage = "", warningMessage = "",
			errorMessage = "", fatalMessage = "", infoTestStartDetail = "";

	public TestCaseRunner() {
		ActionKeywords = new ActionKeyword();
		method = ActionKeywords.getClass().getMethods();
	}

	public static void main(String[] args) {

		// JiraFunction.JsonBuilder();

		// MainPage.callUI();

		DOMConfigurator.configure("src//Util//Log4j.xml");
		Log.debug("TestCaserunner>main: DOM has been configured at src//Util//Log4j.xml location");
		Log.info("Starting Automation framework");
		Log.info("Cleaning Logs");
		Log.LogClean();
		Log.debug("TestCaserunner>main: Log.LogClean have been called.");
		Log.info("Test Scenario Configuration started");
		TestCaseRunner startEngine = new TestCaseRunner();
		Log.debug("TestCaserunner>main: startEngine object has been created.");
		Log.info("Initialing HTML reporting");
		ExtentLogs.HTMLINIT();
		Log.debug("TestCaserunner>main: HTML report has been initiated.");
		Log.info("Starting Test Suit Execution");
		if (Constant.ExcelReport) {
			Log.debug("TestCaserunner>main: Creating Test suit report.");
			Log.info("Creating Test suit report");
			// #################################################
			ExcelReporting.CreateReport();
		}
		startEngine.testsuitrun();
		Log.debug("TestCaserunner>main: startEngine object has been called.");
		Log.info("Creating Logs backup and storing for further reference");
		ExtentLogs.LogBackup();
		Log.debug("TestCaserunner>main: Extent log backup has been called.");
		Log.LogBackup();
		Log.debug("TestCaserunner>main: log backup has been called.");
		// ExtentLogs.close();
		if (!Constant.EnableEmail == true) {
			Mail_Util.Sendmail();
			Log.debug("TestCaserunner>main:Mail_Util.Sendmail has been called when Constant.EnableEmail is true.");
		}

		// ExcelReporting.UpdateRunStatus("Close");

		if (Constant.rerun > 0)
			for (int i = 0; i < Constant.rerun; i++) {
				setuprerun();
				startEngine.testsuitrun();
				Log.debug("TestCaserunner>main: startEngine object has been called.");
				Log.info("Creating Logs backup and storing for further reference");
				ExtentLogs.LogBackup();
				Log.debug("TestCaserunner>main: Extent log backup has been called.");
				Log.LogBackup();
				Log.debug("TestCaserunner>main: log backup has been called.");
				Constant.EnableEmail = true;
				if (!Constant.EnableEmail == true) {
					Constant.EmailSubject = "MagicBricks Rerun "
							+ Constant.rerun + " Automation Report";
					Mail_Util.Sendmail();
					Log.debug("TestCaserunner>main:Mail_Util.Sendmail has been called when Constant.EnableEmail is true.");
					// ExcelReporting.UpdateRunStatus("Close");
				}
			}

		System.exit(0);
		System.err.println("Exit with Error");
		System.exit(1);
		System.exit(0);
	}

	private static void setuprerun() {

		Log.debug("TestCaserunner>main: Creating Test suit report.");
		Log.info("Creating Test suit report");
		Constant.testSuitWorkbook = Constant.ExcelReportLocation;
		// ###################################################
		ExcelReporting.CreateReport();
		testSuitResult = true;
		testScriptResult = true;
		Constant.ObjrepLoc.clear();
		Constant.ObjrepType.clear();

		DOMConfigurator.configure("src//Util//Log4j.xml");
		Log.debug("TestCaserunner>main: DOM has been configured at src//Util//Log4j.xml location");
		Log.info("Starting Automation framework");
		Log.info("Cleaning Logs");
		Log.LogClean();
		Log.debug("TestCaserunner>main: Log.LogClean have been called.");
		Log.info("Test Scenario Configuration started");
		Log.debug("TestCaserunner>main: startEngine object has been created.");
		Log.info("Initialing HTML reporting");
		ExtentLogs.HTMLINIT();
		Log.debug("TestCaserunner>main: HTML report has been initiated.");
		Log.info("Starting Test Suit Execution");
	}

	// Test Suit Handle Function
	private void testsuitrun() {
		testSuitResult = true;
		testScriptResult = true;
		int DataCount;
		Constant.ObjrepLoc.clear();
		Constant.ObjrepType.clear();
		try {
			if (Constant.BuildObjrep) {
				BuildObjRep();
				infoMessage = "Object repository is created successfully";
				Log.info(infoMessage);
				Log.debug("TestCaserunner>testsuitrun:BuildObjrep function is called.");
			} else {
				warningMessage = "Object repository creation is skipped please set buildobjrep = true to create object repository";
				Log.warn(warningMessage);
				Log.debug("TestCaserunner>testsuitrun:Else block when Constant.BuildObjrep is false.");
			}
		} catch (Exception e) {
			fatalMessage = "Object repository creation failed";
			Log.fatal(fatalMessage);
			Log.debug("TestCaserunner>testsuitrun:Error catch in creating build Objectrep:."
					+ e.toString());
		}
		// Loop for Test Suit Control till close/Blank in the run mode is found
		for (testSuitindex = 1; testSuitindex < 1048576; testSuitindex++) {
			Log.debug("TestCaserunner>testsuitrun:Loop starting for test suit where value of testsuitindex is ."
					+ testSuitindex);
			// Setting File for test suit
			// Clearing the parameters for various controls
			Constant.datacolumnnumber.clear();
			Constant.TestSuit.clear();
			DataCount = 0;
			try {
				Log.debug("TestCaserunner>testsuitrun:Test suit file has been configured using excelUtils.setexcelfile for values ."
						+ Constant.testSuitWorkbook
						+ " "
						+ Constant.TestSuitsheet);
				ExcelUtils.setExcelFile(Constant.testSuitWorkbook,
						Constant.TestSuitsheet);
				Log.debug("TestCaserunner>testsuitrun:Test suit file read row function is called");
				ExcelUtils.ReadRowTestSuit();
			} catch (Exception e) {
				fatalMessage = "After read Row Test Suit Not able to configure test suit Please verify constant.java values for test suit";
				Log.fatal(fatalMessage);
				Log.debug("TestCaserunner>testsuitrun:Error in setting up test suit file/reading row");

				return;
			}
			if (Constant.TestSuit.get("RunMode").contains("Blank")) {
				Log.debug("TestCaserunner>testsuitrun:Condiction to check values for test suit run mode is blank");
				warningMessage = "RunMode is not Defined for the test scirpt moving to next test script"
						+ Constant.TestSuit.get("Testcaseid");
				Log.warn(warningMessage);
				
				//###### Defect Update
				ExtentLogs.startTestCase(Constant.TestSuit.get("Testcaseid"));
				ExtentLogs.startSubTestCase(Constant.TestSuit.get("Testcaseid"));
				ExtentLogs.fatal(warningMessage);
				ExtentLogs.StopSubTestCase();
				ExtentLogs.endTestCase();
				ExcelReporting.UpdateNewRow();
				ExcelReporting.TestSuitReportUpdate("Fatal");
				ExcelReporting.UpdateRunStatus("No");
			}
			if (Constant.TestSuit.get("RunMode").contains("Close")) {
				ExcelReporting.UpdateRunStatus("Close");
				break;
			}
			if (Constant.TestSuit.get("RunMode").equalsIgnoreCase("Yes")) {
				Log.debug("TestCaserunner>testsuitrun:Condiction to check values for test suit run mode is Yes");
				if (Constant.TestSuit.get("File location").contains("Blank")
						|| Constant.TestSuit.get("SheetName").contains("Blank")) {
					fatalMessage = "Test script Location/sheet name is not provided for "
							+ Constant.TestSuit.get("Testcaseid");
					Log.fatal(fatalMessage);
					//###### Defect Update
					ExtentLogs.startTestCase(Constant.TestSuit.get("Testcaseid"));
					ExtentLogs.startSubTestCase(Constant.TestSuit.get("Testcaseid"));
					ExtentLogs.fatal(fatalMessage);
					ExtentLogs.StopSubTestCase();
					ExtentLogs.endTestCase();
					ExcelReporting.UpdateNewRow();
					ExcelReporting.TestSuitReportUpdate("Fatal");
					ExcelReporting.UpdateRunStatus("No");
					testSuitResult = true;
					continue;
				} else if (Constant.TestSuit.get("Browser").equalsIgnoreCase(
						"Blank")) {
					fatalMessage = "Browser is not defined for the test case, Please mention Browser value (Chrome/Firefox/IE/Safari/Android/HTMLUnit/androidAPP/NoBrowser)for test Script"
							+ Constant.TestSuit.get("Testcaseid");
					Log.fatal(fatalMessage);
					//###### Defect Update
					ExtentLogs.startTestCase(Constant.TestSuit.get("Testcaseid"));
					ExtentLogs.startSubTestCase(Constant.TestSuit.get("Testcaseid"));
					ExtentLogs.fatal(fatalMessage);
					ExtentLogs.StopSubTestCase();
					ExtentLogs.endTestCase();
					ExcelReporting.UpdateNewRow();
					ExcelReporting.TestSuitReportUpdate("Fatal");
					ExcelReporting.UpdateRunStatus("No");
					testSuitResult = true;
					continue;
				}
				// Setting up test case file parameters
				Constant.TestScriptSheet = Constant.TestSuit.get("SheetName");
				Constant.TestScriptWorkbook = Constant.Testdataloc
						+ Constant.TestSuit.get("File location");
				Log.debug("TestCaserunner>testsuitrun:Test Script and Test script workbook parameters has been set for"
						+ Constant.TestScriptSheet
						+ "# and #"
						+ Constant.TestScriptWorkbook);
				// Reading number of data column present in the test script
				ExcelUtils.setExcelFile(Constant.TestScriptWorkbook,
						Constant.TestScriptSheet);
				Log.debug("TestCaserunner>testsuitrun:Test Script and Test script workbook parameters has been set for using setExcelFile"
						+ Constant.TestScriptSheet
						+ "# and #"
						+ Constant.TestScriptWorkbook);
				ExcelUtils.getTestDataCount();
				Log.debug("TestCaserunner>testsuitrun:Datacolumn has been counted");
				if (testSuitResult == false) {
					fatalMessage = "No Data(Yes) column found in test script"
							+ Constant.TestScriptWorkbook;
					Log.fatal(fatalMessage);
					//###### Defect Update
					ExtentLogs.startTestCase(Constant.TestSuit.get("Testcaseid"));
					ExtentLogs.startSubTestCase(Constant.TestSuit.get("Testcaseid"));
					ExtentLogs.fatal(fatalMessage);
					ExtentLogs.StopSubTestCase();
					ExtentLogs.endTestCase();
					ExcelReporting.UpdateNewRow();
					ExcelReporting.TestSuitReportUpdate("Fatal");
					ExcelReporting.UpdateRunStatus("No");
					
					testSuitResult = true;
					continue;
				}

				for (DataCount = 1; DataCount <= Constant.datacolumnnumber
						.size(); DataCount++) {
					Log.debug("TestCaserunner>testsuitrun:Loop Execution starting for Datacount"
							+ DataCount);
					// SettingupDataColumn
					Constant.Data = Constant.datacolumnnumber
							.get(DataCount - 1);
					Log.debug("TestCaserunner>testsuitrun:Constant Data has been set to "
							+ Constant.Data);
					// Starting Test case in the logs
					infoTestStartDetail = "#### Executing Test case: "
							+ Constant.TestSuit.get("Testcaseid")
							+ "; Description: "
							+ Constant.TestSuit.get("Description")
							+ " For Data: " + DataCount;
					Log.debug("TestCaserunner>testsuitrun:Data has been passed to log for start test case"
							+ infoTestStartDetail);
					Log.startTestCase(infoTestStartDetail);
					Log.debug("TestCaserunner>testsuitrun:Data has been passed to HTMLlog for start test case"
							+ infoTestStartDetail);
					ExtentLogs.startTestCase(infoTestStartDetail);

					// Setting File for test suit
					Log.debug("TestCaserunner>testsuitrun:Test suit has been set using ExcelUtils set excelfile");
					ExcelUtils.setExcelFile(Constant.testSuitWorkbook,
							Constant.TestSuitsheet);
					// Reading Browser
					Log.debug("TestCaserunner>testsuitrun:Browser count function in support Lib has been called");
					SupportLib.countbrowser();
					// running scripts for all browsers requested
					for (browsercount = 0; browsercount < Constant.Browser.length; browsercount++) {

						Log.debug("TestCaserunner>testsuitrun:For Loop has been started for Specified number of browsers"
								+ browsercount);
						// opening Browser
						Log.debug("TestCaserunner>testsuitrun:Action Keyword Open Browser has been called");
						ActionKeyword.OpenBrowser();
						// ################///////////////////////////
						ExcelReporting.UpdateNewRow();
						Log.debug("TestCaserunner>testsuitrun:Open Browser has been return successfully");
						// Video Recording options
						if (Constant.VideoRecording == true) {
							Log.debug("TestCaserunner>testsuitrun:Video Recording is started when Constant.VideoRecording is true");
							VideoRecorder.Startvideo();

						}
						// Starting Node Test Case for the browser.
						infoMessage = "#### Executing Test case: "
								+ Constant.TestSuit.get("Testcaseid")
								+ "; Description: "
								+ Constant.TestSuit.get("Description") + " In "
								+ ActionKeyword.browser
								+ " Browser for TestData:" + DataCount;
						Log.info(infoMessage);

						try {
							Log.debug("TestCaserunner>testsuitrun:Test Step runner is being called");
							teststepsrun();
						} catch (Exception e) {
							fatalMessage = "#Exception# Following Error encountered moving to the next test case"
									+ e.toString();
							Log.fatal(fatalMessage);
							ExtentLogs.fatal(fatalMessage);
							ExcelReporting.TestSuitReportUpdate("Error");
							ExcelReporting.UpdateRunStatus("Yes");
							continue;
						} catch (Throwable e) {
							fatalMessage = "#Throwable# Following Error encountered moving to the next test case"
									+ e.toString();
							Log.fatal(fatalMessage);
							ExcelReporting.TestSuitReportUpdate("Error");
							ExcelReporting.UpdateRunStatus("Yes");
							continue;
						}
						if (!testSuitResult) {
							Log.debug("TestCaserunner>testsuitrun:Test Suit result is found false");
							// ExcelReporting.TestSuitReportUpdate("Fail");
							// ExcelReporting.UpdateRunStatus("Yes");
							testSuitResult = true;
							Log.debug("TestCaserunner>testsuitrun:Test Suit result is set to true");
							ActionKeyword.Quit();
							Log.debug("TestCaserunner>testsuitrun:Action Keyword Quit function is completed successfully");
							Log.endTestCase();
							Log.debug("TestCaserunner>testsuitrun:Log.endTestCase function is completed successfully");
							if (Constant.VideoRecording) {
								VideoRecorder.Stoptvideo();
								Log.debug("TestCaserunner>testsuitrun:Video Recording is stop");

							}
							//############################## Defect Observed on 25 fixed for blank status in excel for extent report too.
							//ExtentLogs.error(errorMessage);
							//ExtentLogs.StopSubTestCase();
							Log.fatal(errorMessage);
							ExcelReporting.TestSuitReportUpdate("Error");
							ExcelReporting.UpdateRunStatus("Yes");
							continue;

						} else if (testSuitResult) {
							Log.debug("TestCaserunner>testsuitrun:Test Suit result has been found true");
							ExcelUtils.setExcelFile(Constant.testSuitWorkbook,
									Constant.TestSuitsheet);
							infoMessage = "### Test case: "
									+ Constant.TestSuit.get("Testcaseid")
									+ "; Description: "
									+ Constant.TestSuit.get("Description")
									+ "; status Executed";
							Log.info(infoMessage);
							ExcelReporting.TestSuitReportUpdate("Pass");
							ExcelReporting.UpdateRunStatus("No");
							if (Constant.VideoRecording) {
								VideoRecorder.Stoptvideo();
								Log.debug("TestCaserunner>testsuitrun:Video Recording is stop");
							}
							ActionKeyword.Quit();
							Log.debug("TestCaserunner>testsuitrun:Action Keyword Quit function is completed successfully");
							Log.endTestCase();
							Log.debug("TestCaserunner>testsuitrun:Log.endTestCase function is completed successfully");

						}

					}
					// data block
					if (testSuitResult == false) {
						testSuitResult = true;
						Log.endTestCase();
						ExtentLogs.endTestCase();
						ExcelReporting.TestSuitReportUpdate("Error");
						ExcelReporting.UpdateRunStatus("Yes");
						continue;
					} else if (testSuitResult == true) {
						Log.endTestCase();
						ExtentLogs.endTestCase();

					}

				}

				// if run mode yes block
			} else if (Constant.TestSuit.get("RunMode").equalsIgnoreCase("No")) {

				ExcelUtils.setExcelFile(Constant.testSuitWorkbook,
						Constant.TestSuitsheet);
				Endmessage = "### Test case no: "
						+ Constant.TestSuit.get("Testcaseid")
						+ "; Description: "
						+ Constant.TestSuit.get("Description")
						+ "; Status: NotRun";
				Log.skipTestCase(Constant.TestSuit.get("Testcaseid") + "_"
						+ Constant.TestSuit.get("Description"));
				// ################///////////////////////////
				ExcelReporting.UpdateNewRow();
				ExcelReporting.TestSuitReportUpdate("Skip");
				ExcelReporting.UpdateRunStatus("No");
			} else if (Constant.TestSuit.get("RunMode").equalsIgnoreCase(
					"close")) {

				ExcelUtils.setExcelFile(Constant.testSuitWorkbook,
						Constant.TestSuitsheet);
				infoMessage = "####Closing Test Suite ####";
				// ################///////////////////////////
				// ExcelReporting.UpdateRunStatus("Close");
				Log.info(infoMessage);
				Endmessage = "Closing Test Suite";
				Log.endTestCase();

				break;
			}

			// test suit block
		}

		// function block
	}

	public static void teststepsrun() throws Throwable {

		sActionKeyword = null;
		testScriptIndex = 0;
		testScriptResult = true;
		testSuitResult = true;

		ActionKeyword.ValueCaptured.clear();
		ActionKeyword.Valuestored.clear();
		ActionKeyword.SpamCaptured.clear();
		ActionKeyword.ValuesCaptured.clear();
		ActionKeyword.ActiveFrameElement.clear();
		ActionKeyword.activeframe.clear();
		ActionKeyword.WindowHandle.clear();

		ExcelUtils.setExcelFile(Constant.TestScriptWorkbook,
				Constant.TestScriptSheet);
		try {

			Class cls = Class.forName("Configuration.ActionKeyword");
			Method method = null;
			testScriptResult = true;
			ExtentLogs.startSubTestCase(Constant.TestSuit.get("Testcaseid")
					+ Constant.TestSuit.get("Description")
					+ "Started in Browser: " + ActionKeyword.browser);
			for (testScriptIndex = 1; testScriptIndex <= 1048576; testScriptIndex++) {
				if (testScriptResult == false || testSuitResult == false) {
					ExtentLogs.StopSubTestCase();
					break;
				}
				Constant.TestCaseRow.clear();
				Constant.testcaserownum = testScriptIndex;
				Log.debug("TestCaserunner>teststepsrun:Calling Excel Utils function to read the rows");
				ExcelUtils.ReadRowTestScript();

				if (Constant.TestCaseRow.get("Data").equalsIgnoreCase("#Skip")) {
					String skipMessage = Constant.TestCaseRow.get("TestStepID")
							+ ": "
							+ Constant.TestCaseRow.get("TeststepDescription")
							+ ":Step is skipped based on data selections";
					Log.skip(skipMessage);
					String eSkipMessage = " Action Element"
							+ Constant.TestCaseRow.get("Elementlocation")
							+ " Moving to next step";
					ExtentLogs.Skip(eSkipMessage);
					continue;
				}

				if (testScriptResult == false || testSuitResult == false) {
					ExtentLogs.StopSubTestCase();
					break;
				}
				sActionKeyword = Constant.TestCaseRow.get("Action");
				if (sActionKeyword.contains("Blank")) {
					fatalMessage = Constant.TestCaseRow.get("Testcaseid")
							+ sActionKeyword
							+ ":Action Keyword is not defined. Please defined Action Keyword";
					Log.fatal(fatalMessage);
					//##################################Defect updated for fatal issue during observation on 25th Feb
					ExtentLogs.fatal(fatalMessage);
					ExtentLogs.StopSubTestCase();
					//###### Defect Update
					//ExtentLogs.endTestCase();
					//ExcelReporting.UpdateNewRow();
					ExcelReporting.TestSuitReportUpdate("Fatal");
					ExcelReporting.UpdateRunStatus("No");
					
					testScriptResult = false;
					testSuitResult = false;
				
					break;
				}
				method = ((Class) cls).getDeclaredMethod(sActionKeyword, null);

				if (!(sActionKeyword.equalsIgnoreCase("Close"))) {
					method.invoke(sActionKeyword, null);

				} else if (sActionKeyword.contains("Close")
						&& Constant.commonsteps == true) {
					return;
				} else if (sActionKeyword.contains("Close")
						&& Constant.commonsteps == false) {
					ActionKeyword.Close();
					ExtentLogs.StopSubTestCase();
					break;
				}
			}
		}

		// Error handling code
		catch (NoSuchMethodException e) {
			String logmessage = Constant.TestCaseRow.get("Testcaseid")
					+ sActionKeyword + ":Invalid Action Kewyord";
			errorMessage = logmessage + e.toString();
			Log.error(errorMessage);
			ExtentLogs.error(errorMessage);

			ExtentLogs.StopSubTestCase();
			testSuitResult = false;
			ExcelReporting.TestSuitReportUpdate("Error");
			ExcelReporting.UpdateRunStatus("Yes");

		} catch (NoSuchElementException e) {
			String logmessage = Constant.TestCaseRow.get("Testcaseid")
					+ sActionKeyword
					+ ":Not able to search the Element on Active window";
			errorMessage = logmessage + e.toString();
			Log.error(errorMessage);
			ExtentLogs.error(errorMessage);
			ExtentLogs.StopSubTestCase();
			testSuitResult = false;
			ExcelReporting.TestSuitReportUpdate("Error");
			ExcelReporting.UpdateRunStatus("Yes");

		} catch (UnreachableBrowserException e) {
			errorMessage = Constant.TestCaseRow.get("Testcaseid")
					+ "; Browser not available" + e.toString();
			Log.error(errorMessage);
			ExtentLogs.error(errorMessage);
			ExtentLogs.StopSubTestCase();
			testSuitResult = false;
			ExcelReporting.TestSuitReportUpdate("Error");
			ExcelReporting.UpdateRunStatus("Yes");
		} catch (NullPointerException e) {
			errorMessage = Constant.TestCaseRow.get("Testcaseid")
					+ "; Action/ActionElement not found " + e.toString();
			Log.error(errorMessage);
			ExtentLogs.error(errorMessage);
			ExtentLogs.StopSubTestCase();
			testSuitResult = false;
			ExcelReporting.TestSuitReportUpdate("Error");
			ExcelReporting.UpdateRunStatus("Yes");
		} catch (IllegalArgumentException e) {
			errorMessage = Constant.TestCaseRow.get("TestStepID")
					+ "; Element not found " + e.toString();
			Log.error(errorMessage);
			ExtentLogs.error(errorMessage);
			ExtentLogs.StopSubTestCase();
			testSuitResult = false;
			ExcelReporting.TestSuitReportUpdate("Error");
			ExcelReporting.UpdateRunStatus("Yes");
		} catch (NoSuchWindowException e) {
			errorMessage = Constant.TestCaseRow.get("Testcaseid")
					+ "; Browser Window is Closed" + e.toString();
			Log.error(errorMessage);
			ExtentLogs.error(errorMessage);
			ExtentLogs.StopSubTestCase();
			testSuitResult = false;
			ExcelReporting.TestSuitReportUpdate("Error");
			ExcelReporting.UpdateRunStatus("Yes");

		} catch (WebDriverException e) {
			errorMessage = Constant.TestCaseRow.get("Testcaseid")
					+ "; Browser Window is Closed" + e.toString();
			Log.error(errorMessage);
			ExtentLogs.error(errorMessage);
			ExtentLogs.StopSubTestCase();
			testSuitResult = false;
			ExcelReporting.TestSuitReportUpdate("Error");
			ExcelReporting.UpdateRunStatus("Yes");
		} catch (InvocationTargetException e) {

			Throwable cause = e.getCause();
			if (cause == null) {

				errorMessage = "Got InvocationTargetException, but the cause is null ";

			} else if (cause instanceof NoSuchElementException) {
				String logmessage = Constant.TestCaseRow.get("Testcaseid")
						+ Constant.TestCaseRow.get("TestStepID")
						+ Constant.TestCaseRow.get("ElementFinderType")
						+ TestCaseRunner.sActionKeyword
						+ ": Not able to search the Element on Active window ";
				errorMessage = logmessage + cause.toString();

			} else if (cause instanceof java.io.IOException) {

				errorMessage = Constant.TestCaseRow.get("Testcaseid")
						+ Constant.TestCaseRow.get("TestStepID")
						+ Constant.TestCaseRow.get("ElementFinderType")
						+ "; Error in communicating with browser: "
						+ cause.toString();
			}

			else if (cause instanceof org.openqa.selenium.NoSuchWindowException) {
				errorMessage = Constant.TestCaseRow.get("Testcaseid")
						+ Constant.TestCaseRow.get("TestStepID")
						+ Constant.TestCaseRow.get("ElementFinderType")
						+ "; Browser Window is Closed: " + cause.toString();
			}

			else if (cause instanceof org.openqa.selenium.remote.UnreachableBrowserException) {
				errorMessage = Constant.TestCaseRow.get("Testcaseid")
						+ Constant.TestCaseRow.get("TestStepID")
						+ Constant.TestCaseRow.get("ElementFinderType")
						+ "; Browser not available: " + cause.toString();
			}

			else if (cause instanceof NoSuchElementException) {
				errorMessage = Constant.TestCaseRow.get("Testcaseid")
						+ Constant.TestCaseRow.get("TestStepID")
						+ Constant.TestCaseRow.get("ElementFinderType")
						+ sActionKeyword
						+ ":Not able to search the Element on Active window "
						+ cause.toString();

			} else {
				errorMessage = Constant.TestCaseRow.get("Testcaseid")
						+ Constant.TestCaseRow.get("TestStepID")
						+ Constant.TestCaseRow.get("ElementFinderType")
						+ sActionKeyword + "  " + e.getCause() + e.getMessage()
						+ ":Exception is not handled";
			}
			Log.error(errorMessage);
			ExtentLogs.error(errorMessage);
			ExtentLogs.StopSubTestCase();
			testSuitResult = false;
			ExcelReporting.TestSuitReportUpdate("Error");
			ExcelReporting.UpdateRunStatus("Yes");
		}

		catch (Exception e) {
			errorMessage = Constant.TestCaseRow.get("Testcaseid")
					+ "; Exception Not handled: " + e.toString();
			Log.error(errorMessage);
			ExtentLogs.error(errorMessage);
			ExtentLogs.StopSubTestCase();
			testSuitResult = false;
			ExcelReporting.TestSuitReportUpdate("Error");
			ExcelReporting.UpdateRunStatus("Yes");
		}

		catch (Throwable e) {
			errorMessage = Constant.TestCaseRow.get("Testcaseid")
					+ "; Throwable Exception Not handled: " + e.toString();
			Log.fatal(errorMessage);
			ExtentLogs.error(errorMessage);
			ExtentLogs.StopSubTestCase();
			testSuitResult = false;
			ExcelReporting.TestSuitReportUpdate("Error");
			ExcelReporting.UpdateRunStatus("Yes");

		}

	}

	public static void Config() {
		try {
			ExcelUtils.setExcelFile(Constant.TestScriptWorkbook,
					Constant.TestScriptSheet);
			int i = 1;
			float configvaluefloat;
			String configvalue = "NoValue";
			infoMessage = "Updating configuraiton for FrameWork";
			while (i < Constant.ConfigNoOfRow) {
				int logic;
				switch (i) {
				case 1:
					configvaluefloat = Float.parseFloat(ExcelUtils.getCellData(
							i, 1));
					Constant.Testcaseid = (int) configvaluefloat;
					infoMessage = "Configuration value has been updated for testcaseid to: "
							+ Constant.Testcaseid;
					break;
				case 2:

					configvaluefloat = Float.parseFloat(ExcelUtils.getCellData(
							i, 1));
					Constant.TestStepID = (int) configvaluefloat;
					infoMessage = "Configuration value has been updated for TestStepID to: "
							+ Constant.TestStepID;
					break;
				case 3:

					configvaluefloat = Float.parseFloat(ExcelUtils.getCellData(
							i, 1));
					Constant.TeststepDescription = (int) configvaluefloat;
					infoMessage = "Configuration value has been updated for TeststepDescription to: "
							+ Constant.TeststepDescription;
					break;
				case 4:

					configvaluefloat = Float.parseFloat(ExcelUtils.getCellData(
							i, 1));
					Constant.ElementFinderType = (int) configvaluefloat;
					infoMessage = "Configuration value has been updated for ElementFinderType to: "
							+ Constant.ElementFinderType;
					break;
				case 5:

					configvaluefloat = Float.parseFloat(ExcelUtils.getCellData(
							i, 1));
					Constant.Elementlocation = (int) configvaluefloat;
					infoMessage = "Configuration value has been updated for Elementlocation to: "
							+ Constant.Elementlocation;
					break;
				case 6:

					configvaluefloat = Float.parseFloat(ExcelUtils.getCellData(
							i, 1));
					Constant.Data = (int) configvaluefloat;
					infoMessage = "Configuration value has been updated for Data to: "
							+ Constant.Data;
					break;
				case 7:
					configvaluefloat = Float.parseFloat(ExcelUtils.getCellData(
							i, 1));
					Constant.Action = (int) configvaluefloat;
					infoMessage = "Configuration value has been updated for Action to: "
							+ Constant.Action;
					break;
				case 8:
					configvaluefloat = Float.parseFloat(ExcelUtils.getCellData(
							i, 1));
					Constant.ActionSupportValue = (int) configvaluefloat;
					infoMessage = "Configuration value has been updated for nSupportData to: "
							+ Constant.ActionSupportValue;
					break;
				case 9:
					configvaluefloat = Float.parseFloat(ExcelUtils.getCellData(
							i, 1));
					Constant.ExplicitWait = (int) configvaluefloat;
					infoMessage = "Configuration value has been updated for ExplicitWait to: "
							+ Constant.ExplicitWait;
					break;
				case 10:
					configvaluefloat = Float.parseFloat(ExcelUtils.getCellData(
							i, 1));
					Constant.ExplicitWaitif = (int) configvaluefloat;
					infoMessage = "Configuration value has been updated for ExplicitWaitif to: "
							+ Constant.ExplicitWaitif;
					break;
				case 11:
					configvaluefloat = Float.parseFloat(ExcelUtils.getCellData(
							i, 1));
					Constant.ImplicitWait = (int) configvaluefloat;
					infoMessage = "Configuration value has been updated for ImplicitWait to: "
							+ Constant.ImplicitWait;
					break;
				case 12:
					configvalue = ExcelUtils.getCellData(i, 1);
					Constant.dataformate = configvalue;
					infoMessage = "Configuration value has been updated for dataformate to: "
							+ Constant.dataformate;
					break;
				case 13:
					configvaluefloat = Float.parseFloat(ExcelUtils.getCellData(
							i, 1));
					logic = (int) configvaluefloat;
					if (logic == 1) {
						Constant.ErrorScreenShotFlag = true;
					} else {
						Constant.ErrorScreenShotFlag = false;
					}
					infoMessage = "Configuration value has been updated for ErrorScreenShotFlag to: "
							+ Constant.ErrorScreenShotFlag;
					break;
				case 14:
					configvaluefloat = Float.parseFloat(ExcelUtils.getCellData(
							i, 1));
					logic = (int) configvaluefloat;
					if (logic == 1) {
						Constant.ErrorScreenShotFlag = true;
					} else {
						Constant.ErrorScreenShotFlag = false;
					}
					infoMessage = "Configuration value has been updated for FirefoxTabprefrence to: "
							+ Constant.FirefoxTabprefrence;
					break;
				case 15:
					configvaluefloat = Float.parseFloat(ExcelUtils.getCellData(
							i, 1));
					logic = (int) configvaluefloat;
					if (logic == 1) {
						Constant.ErrorScreenShotFlag = true;
					} else {
						Constant.ErrorScreenShotFlag = false;
					}
					infoMessage = "Configuration value has been updated for ChromeExtEnable to: "
							+ Constant.ChromeExtEnable;
					break;
				case 16:
					configvaluefloat = Float.parseFloat(ExcelUtils.getCellData(
							i, 1));
					logic = (int) configvaluefloat;
					if (logic == 1) {
						Constant.ErrorScreenShotFlag = true;
					} else {
						Constant.ErrorScreenShotFlag = false;
					}
					infoMessage = "Configuration value has been updated for IECleanHistoryFlag to: "
							+ Constant.IECleanHistoryFlag;
					break;
				default:
					;
				}
				i++;
				Log.info(infoMessage);
			}
		} catch (Exception e) {
			Log.fatal("Error in Setting up Runtime configuration"
					+ e.toString());
			return;
		}

	}

	public static void BuildObjRep() throws Exception {
		ActionKeyword.Quit();
		Log.debug("TestCaserunner>BuildObjRep: Object rep Build function is started and excel files has been set to: "
				+ Constant.ObjRepName + "#And#" + Constant.ObjRepSheetname);
		ExcelUtils.setExcelFile(Constant.ObjRepName, Constant.ObjRepSheetname);
		int i = 0, j = 0;
		String key, Value, Type;
		if (Constant.ObjrepLoc.isEmpty()) {
			Log.debug("TestCaserunner>BuildObjRep: In the block when object rep is found blank:");
			while (!ExcelUtils.getCellData(i + 1, j).equalsIgnoreCase("close")
					|| !(ExcelUtils.getCellData(i, j).equalsIgnoreCase("Blank")
							&& !ExcelUtils.getCellData(i + 1, j)
									.equalsIgnoreCase("Blank") && !ExcelUtils
							.getCellData(i + 2, j).equalsIgnoreCase("Blank"))) {
				Log.debug("TestCaserunner>BuildObjRep: Loop Started for Object rep creation");
				key = ExcelUtils.getCellData(i, j);
				Log.debug("TestCaserunner>BuildObjRep: Following #Key# is stored in Object rep"
						+ key);
				Value = ExcelUtils.getCellData(i, j + 1);
				Log.debug("TestCaserunner>BuildObjRep: Following #Value# is stored in Object rep"
						+ Value);
				Type = ExcelUtils.getCellData(i, j + 2);
				Log.debug("TestCaserunner>BuildObjRep: Following #Type# is stored in Object rep"
						+ Type);
				Constant.ObjrepLoc.put(key, Value);
				Constant.ObjrepType.put(key, Type);

				if (ExcelUtils.getCellData(i + 1, j).equalsIgnoreCase("close")
						|| (ExcelUtils.getCellData(i + 1, j).equalsIgnoreCase(
								"Blank")
								&& ExcelUtils.getCellData(i + 2, j)
										.equalsIgnoreCase("Blank") && ExcelUtils
								.getCellData(i + 3, j)
								.equalsIgnoreCase("Blank"))) {
					Log.debug("TestCaserunner>BuildObjRep: Finish Building Object rep"
							+ Type);
					break;
				}
				Log.debug("TestCaserunner>BuildObjRep: Value of loop iterator is "
						+ i);
				i++;
			}

		}
	}
}
