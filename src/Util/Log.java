package Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import Configuration.Constant;
import TestRunner.TestCaseRunner;

public class Log {
	static String location, URL;

	// Initialize Log4j logs
	private static Logger Log = Logger.getLogger(Log.class.getName());

	// This is to print log for the beginning of the test case, as we usually
	// run so many test cases as a test suite
	public static void startTestCase(String testCaseDetail) {

		Log.info("****************************************************************************************");
		Log.info("$$$$$$$$$$$$$$$$$$$$$                 " + testCaseDetail + "       $$$$$$$$$$$$$$$$$$$$$$$$$");
		Log.info("****************************************************************************************");
		if (Constant.TestSuit.get("RunMode").contains("Yes")) {
			System.out.println(testCaseDetail);
		}
	}

	// This is to print log for the ending of the test case
	public static void endTestCase() {
		Log.info("XXXXXXXXXXXXXXXXXXXXXXX             " + "-E---N---D Test Case"
				+ "             XXXXXXXXXXXXXXXXXXXXXX");
	}

	// Need to create these methods, so that they can be called
	public static void info(String infoMessage) {
		try {
			System.out.println("Info# " + infoMessage);
			Log.info(infoMessage);
		} catch (Exception e) {
			System.out
					.println("Error in Logger returning to the calling function for continue execution" + e.toString());
			return;
		}
	}

	public static void pass(String passMessage) {
		try {
			System.out.println("Info#Pass# " + passMessage);
			Log.info(passMessage);
		} catch (Exception e) {
			System.out
					.println("Error in Logger returning to the calling function for continue execution" + e.toString());
			return;
		}
	}

	public static void fail(String FailMessage) {
		try {
			System.out.println("Info#Fail# " + FailMessage);
			Log.info(FailMessage);
		} catch (Exception e) {
			System.out
					.println("Error in Logger returning to the calling function for continue execution" + e.toString());
			return;
		}
	}

	public static void skip(String skipMessage) {
		try {
			System.out.println("Info#Skip# " + skipMessage);
			Log.info(skipMessage);
		} catch (Exception e) {
			System.out
					.println("Error in Logger returning to the calling function for continue execution" + e.toString());
			return;
		}
	}

	public static void skipTestCase(String skipTestCaseMessage) {
		try {
			System.out.println("Info#Skip test case# " + skipTestCaseMessage);
			Log.info(skipTestCaseMessage);
		} catch (Exception e) {
			System.out
					.println("Error in Logger returning to the calling function for continue execution" + e.toString());
			return;
		}
	}

	public static void warn(String warningMessage) {
		try {
			System.out.println("Warning# " + warningMessage);
			Log.warn(warningMessage);
		} catch (Exception e) {
			System.out
					.println("Error in Logger returning to the calling function for continue execution" + e.toString());
			return;
		}
	}

	public static void error(String errorMessage) {
		try {
			TestCaseRunner.testScriptResult = false;
			TestCaseRunner.testSuitResult = false;
			System.out.println(errorMessage);
			Log.error("Error#: " + errorMessage);
		} catch (Exception e) {
			System.out
					.println("Error in Logger returning to the calling function for continue execution" + e.toString());
			return;
		}

	}

	public static void fatal(String fatalMessage) {
		try {
			TestCaseRunner.testScriptResult = false;
			TestCaseRunner.testSuitResult = false;
			System.out.println("FATAL# " + fatalMessage);
			Log.fatal(fatalMessage);
		} catch (Exception e) {
			System.out
					.println("Error in Logger returning to the calling function for continue execution" + e.toString());
			return;
		}
	}

	public static void debug(String debugMessage) {
		try {
			if (Constant.loglevel == 3) {
				System.out.println("Debug# " + debugMessage);
				Log.info(debugMessage);
			}
		} catch (Exception e) {
			System.out
					.println("Error in Logger returning to the calling function for continue execution" + e.toString());
			return;
		}
	}

	public static void LogBackup() {
		try {
			Date date = new Date();
			DateFormat df = new SimpleDateFormat("dd MMM yyyy kk:mm:ss");
			df.setTimeZone(TimeZone.getTimeZone("IST"));
			File SRC_LOG_FILE = FileUtils.getFile("logfile.log");
			String LogFileName = df.format(date) + "_log" + ".txt";
			LogFileName = LogFileName.replace(':', '_');
			LogFileName = LogFileName.replace(' ', '_');
			Constant.AttachmentLog = LogFileName;
			File targetFile = new File(Constant.LogFileLocation + LogFileName);
			FileUtils.copyFile(SRC_LOG_FILE, targetFile);
		} catch (IOException e) {
			System.out.println("Error Creating backup of log file" + e.toString());
			return;
		} catch (Exception e) {
			System.out.println("Error Creating backup of log file" + e.toString());
			return;
		}
	}

	public static void LogClean() {
		try {
			PrintWriter writer = new PrintWriter("logfile.log");
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e) {
			System.out.println(
					"'Logfile.txt' is not available under source folder please make sure that file is available under root location"
							+ e.toString());
			return;
		}
	}

}