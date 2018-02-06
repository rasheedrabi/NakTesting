package Util;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import Configuration.Constant;
import Configuration.SupportLib;
import TestRunner.TestCaseRunner;

public class ExtentLogs {
	static ExtentReports extent1 = new ExtentReports(Constant.testreport
			+ "TestReport.html", true);
	public static ExtentTest extent, extentParent;

	public static void startTestCase(String sTestCaseName) {
		if (Constant.HTMLREPORTING == true) {

			extentParent = extent1.startTest(sTestCaseName).assignCategory(
					Constant.TestSuit.get("Category"));

		}
	}

	public static void SkipTestCase() {
		if (Constant.HTMLREPORTING == true) {
			String eSkipMessage = "Run Mode is set to No for the respective test case";
			extentParent.log(LogStatus.SKIP, eSkipMessage);
		}
		
		ExcelReporting.TestSuitReportUpdate("Skip");
		ExcelReporting.UpdateRunStatus("No");
	}

	public static void Skip(String eSkipMessage) {
		if (Constant.HTMLREPORTING == true) {

			extent.log(
					LogStatus.SKIP,
					ExcelUtils.getCellData(TestCaseRunner.testScriptIndex,
							Constant.TestStepID),
					ExcelUtils.getCellData(TestCaseRunner.testScriptIndex,
							Constant.TeststepDescription) + eSkipMessage);
		}
	}

	public static void pass(String ePassMessage) {
		if (Constant.HTMLREPORTING == true) {
			if (Constant.ExtReportloglevel == 3) {
				String location = SupportLib.createScreenshot();
				extent.log(
						LogStatus.PASS,
						ExcelUtils.getCellData(TestCaseRunner.testScriptIndex,
								Constant.TestStepID),
						ExcelUtils.getCellData(TestCaseRunner.testScriptIndex,
								Constant.TeststepDescription)
								+ ePassMessage
								+ extent.addScreenCapture(location));
			} else {
				extent.log(
						LogStatus.PASS,
						ExcelUtils.getCellData(TestCaseRunner.testScriptIndex,
								Constant.TestStepID),
						ExcelUtils.getCellData(TestCaseRunner.testScriptIndex,
								Constant.TeststepDescription) + ePassMessage);
			}
		}

	}

	public static void Fail(String eFailMessage) {
		if (Constant.HTMLREPORTING == true) {
			if (Constant.HTMLREPORTING == true) {

				String location = SupportLib.createScreenshot();
				String URL = null;
				if (Constant.defectcontrol == true) {
					try {
						URL = JiraFunction.Createdefect();
					} catch (Exception e) {
						Log.info(e.getStackTrace().toString());
						ExtentLogs.info(e.getStackTrace().toString());
					}
				}
				extent.log(

						LogStatus.FAIL,
						ExcelUtils.getCellData(TestCaseRunner.testScriptIndex,
								Constant.TestStepID),
						ExcelUtils.getCellData(TestCaseRunner.testScriptIndex,
								Constant.TeststepDescription)
								+ "<br/>"
								+ eFailMessage
								+ "<br/>"
								+ "<a href="
								+ "'"
								+ URL
								+ "'"
								+ ">"
								+ JiraFunction.DefectID
								+ "</a>"
								+ "<br/>"
								+ extent.addScreenCapture(location));

			}
			extent1.flush();
		}
		ExcelReporting.TestSuitReportUpdate("Fail");
		ExcelReporting.UpdateRunStatus("Yes");
	}

	public static void endTestCase() {
		if (Constant.HTMLREPORTING == true) {
			extent1.endTest(extentParent);
			extent1.flush();
		}
	}

	public static void info(String eInfoMessage) {
		if (eInfoMessage == null) {
			eInfoMessage = "Error To Capture logs";
		}
		if (Constant.HTMLREPORTING == true) {

			extent.log(LogStatus.INFO, ExcelUtils.getCellData(
					TestCaseRunner.testScriptIndex, Constant.TestStepID),
					ExcelUtils.getCellData(TestCaseRunner.testScriptIndex,
							Constant.TeststepDescription));
		}
	}

	public static void warn(String eWarningMessage) {
		if (eWarningMessage == null) {
			eWarningMessage = "Error To Capture logs";
		}
		if (Constant.HTMLREPORTING == true) {

			String location = SupportLib.createScreenshot();

			extent.log(

					LogStatus.WARNING,
					ExcelUtils.getCellData(TestCaseRunner.testScriptIndex,
							Constant.TestStepID),
					ExcelUtils.getCellData(TestCaseRunner.testScriptIndex,
							Constant.TeststepDescription)
							+ eWarningMessage
							+ extent.addScreenCapture(location));

		}
		extent1.flush();
	}

	public static void error(String eErrorMessage) {
		try {
			if (eErrorMessage == null) {
				Log.debug("ExtentReports>errorBlock in case of error message is null received");
				eErrorMessage = "Error To Capture logs";
			}
			if (Constant.HTMLREPORTING == true) {
				Log.debug("ExtentReports>errorBlock if HTML reporting is true");
				String URL = null;
				if (Constant.defectcontrol == true) {
					Log.debug("ExtentReports>errorBlock if Defect logging is true");
					URL = JiraFunction.Createdefect();
				}
				Log.debug("ExtentReports>errorCalling for image capture ");
				String location = SupportLib.createScreenshot();
				Log.debug("ExtentReports>error:Captured image location is"
						+ location);
				extent.log(

						LogStatus.ERROR,
						ExcelUtils.getCellData(TestCaseRunner.testScriptIndex,
								Constant.TestStepID),
						ExcelUtils.getCellData(TestCaseRunner.testScriptIndex,
								Constant.TeststepDescription)
								+ "<br/>"
								+ eErrorMessage
								+ "<br/>"
								+ "<br/>"
								+ "<a href="
								+ "'"
								+ URL
								+ "'"
								+ ">"
								+ JiraFunction.DefectID
								+ "</a>"
								+ "<br/>"
								+ extent.addScreenCapture(location));

			}
		} catch (Exception e) {
			Log.fatal("Error in creating HTML logs for Error test step"
					+ e.toString());
			return;
		}
//		ExcelReporting.TestSuitReportUpdate("Error");
//		ExcelReporting.UpdateRunStatus("Yes");
	}

	public static void fatal(String eFatalMessage) {
		if (eFatalMessage == null) {
			eFatalMessage = "Error To Capture logs";
		}
		if (Constant.HTMLREPORTING == true) {
			String location = "Not Available";
			//location = SupportLib.createScreenshot();
			extent.log(
					LogStatus.FATAL,
					(eFatalMessage == null) ? "" : eFatalMessage
							+ ((location == null) ? "" : ""
									));
			//+ extent.addScreenCapture(location)
		}
	}

	public static void LogBackup() {

		Date date = new Date();
		DateFormat df = new SimpleDateFormat("dd MMM yyyy kk:mm:ss");

		df.setTimeZone(TimeZone.getTimeZone("IST"));
		File SRC_LOG_FILE;
		// System.out.println(df.format(date));
		String LogFileName = df.format(date) + "_Report" + ".html";
		LogFileName = LogFileName.replace(':', '_');
		LogFileName = LogFileName.replace(' ', '_');
		if (Constant.HTMLREPORTING == true) {
			File targetFile = new File(Constant.testreport + LogFileName);
			SRC_LOG_FILE = FileUtils.getFile(Constant.testreport
					+ "TestReport.html");
			LogFileName = df.format(date) + "_FunctionalTestReport" + ".html";
			Constant.EmailSubject = df.format(date) + "_FunctionalTestReport";

			LogFileName = LogFileName.replace(':', '_');
			LogFileName = LogFileName.replace(' ', '_');

			// Setting Up Log File name in case of Test suit Finish for
			// Email
			// Attachment
			Constant.AttachmentReport = LogFileName;
			targetFile = new File(Constant.testreport + LogFileName);
			try {
				FileUtils.copyFile(SRC_LOG_FILE, targetFile);
			} catch (IOException e) {
				Log.fatal("I/O Error in creating HTML logs file" + e.toString());
				return;
			}
		}

	}

	public static void HTMLINIT() {
		if (Constant.HTMLREPORTING == true) {
			SupportLib.updateSystemDetails();
			String style = ".panel-lead { word-wrap: break-word; }.panel-lead { font-size: 10pt }";
			extent1.config().insertCustomStyles(style);
			extent1.config()
					.documentTitle(
							Constant.SystemDetails.get("ExtentReportTitle"))
					.reportName(Constant.SystemDetails.get("ExtentReportName"))
					.reportHeadline(
							Constant.SystemDetails.get("ExtentReportHeadline"))
					.insertCustomStyles(".test { border:2px solid #444; }");
			extent1.addSystemInfo(Constant.SystemDetails);
		}
	}

	public static void close() {
		extent1.close();
	}

	public static void startSubTestCase(String sTestCaseName) {
		if (Constant.HTMLREPORTING == true) {
			extent = extent1.startTest(sTestCaseName);
		}
	}

	public static void StopSubTestCase() {
		if (Constant.HTMLREPORTING == true) {
			extentParent.appendChild(extent);
		}
	}
}
