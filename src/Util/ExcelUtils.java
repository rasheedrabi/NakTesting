package Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import Configuration.Constant;
import TestRunner.TestCaseRunner;

public class ExcelUtils {

	private static XSSFRow Row = null;
	private static XSSFSheet ExcelWSheet;
	private static XSSFWorkbook ExcelWBook;
	private static XSSFCell Cell;
	static FormulaEvaluator evaluator;
	static FileInputStream ExcelFile;
	static FileOutputStream fileOut;
	private static String errorMessage = "", fatalMessage = "";

	// This method is to set the File path and to open the Excel file
	// Pass Excel Path and SheetName as Arguments to this method
	public static void setExcelFile(String Path, String SheetName) {
		try {
			Log.debug("ExcelUtils>setExcelFile: Setting up Excel for file name and sheet name: "
					+ Path + " #And#" + SheetName);
			if (Path.contains("Blank") || SheetName.contains("Blank")) {
				Log.debug("In the block if path or sheet name is blank");
				fatalMessage = "File Location/SheetName not found. Please provide File Location or Sheet Name";
				Log.fatal(fatalMessage);
				IOException e = new IOException();
				throw e;
			}
			Log.debug("ExcelUtils>setExcelFile: Opening File input stream");
			FileInputStream ExcelFile;
			Log.debug("ExcelUtils>setExcelFile: Creating file object with the path provided"
					+ Path);
			ExcelFile = new FileInputStream(Path);
			Log.debug("ExcelUtils>setExcelFile: Setting up Excel Workbook");
			ExcelWBook = new XSSFWorkbook(ExcelFile);
			Log.debug("ExcelUtils>setExcelFile: Setting up the sheet name"
					+ SheetName);
			ExcelWSheet = ExcelWBook.getSheet(SheetName);
			Log.debug("ExcelUtils>setExcelFile: Creating Object for formula evaluator");
			evaluator = ExcelWBook.getCreationHelper().createFormulaEvaluator();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.debug("ExcelUtils>setExcelFile: FileNotFoundException while creating object to work on excel file");
			fatalMessage = "Not able to open File: " + Path + ": "
					+ e.toString();
			Log.fatal(fatalMessage);
			Log.debug("ExcelUtils>setExcelFile: Setting test suit and test script flags to false to stop execution for the row in test suit");
			TestCaseRunner.testSuitResult = false;
			TestCaseRunner.testScriptResult = false;
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.debug("ExcelUtils>setExcelFile: IOException while creating object to work on excel file");
			fatalMessage = "Error in opening file: " + Path + e.toString();
			Log.fatal(fatalMessage);
			Log.debug("ExcelUtils>setExcelFile: Setting test suit and test script flags to false to stop execution for the row in test suit");
			TestCaseRunner.testSuitResult = false;
			TestCaseRunner.testScriptResult = false;
			return;
		} catch (java.lang.NullPointerException e) {
			// TODO Auto-generated catch block
			Log.debug("ExcelUtils>setExcelFile: NullPointerException while creating object to work on excel file");
			fatalMessage = "Error in opening file: " + Path + e.toString();
			Log.fatal(fatalMessage);
			Log.debug("Setting test suit and test script flags to false to stop execution for the row in test suit");
			TestCaseRunner.testSuitResult = false;
			TestCaseRunner.testScriptResult = false;
			return;
		}
	}

	// This method is to read the test data from the Excel cell
	// In this we are passing parameters/arguments as Row Num and Col Num
	public static String getCellData(int RowNum, int ColNum) {
		Log.debug("ExcelUtils>getCellData: In the function to get Cell data for the specified excel file");
		// CellValue CellValue = null;
		String CellData = "Blank";
		Log.debug("ExcelUtils>getCellData:Saving value for Row no and Column No"
				+ RowNum + " #And# " + ColNum);
		try {
			Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
		} catch (Exception e) {
		}
		if (Cell == null) {
			Log.debug("ExcelUtils>getCellData:Logic to check if Cell value is null No error is throwing here and moving to next step");
			// java.lang.NullPointerException e = null;

		} else {
			if (Cell.getCellType() == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_FORMULA) {
				Log.debug("ExcelUtils>getCellData:Cell Value is identified ad Formula");
				evaluator.evaluateFormulaCell(Cell);

				if (Cell.getCachedFormulaResultType() == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC) {
					if (DateUtil.isCellDateFormatted(Cell)) {
						Log.debug("ExcelUtils>getCellData:After processing formula Cell Value is identified as Date");
						java.util.Date date = Cell.getDateCellValue();
						SimpleDateFormat sdf = new SimpleDateFormat(
								Constant.dataformate);
						CellData = sdf.format(date);
					} else {
						CellData = String.valueOf(Cell.getNumericCellValue());
						Log.debug("ExcelUtils>getCellData:After processing formula Cell Value is identified as Numeric");
					}
				} else if (Cell.getCachedFormulaResultType() == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN) {
					Log.debug("ExcelUtils>getCellData:Cell Value is identified as Boolean");
					CellData = String.valueOf(Cell.getBooleanCellValue());
					// System.out.println(CellData);
					// System.out.println(Cell.getBooleanCellValue());
				} else {
					CellData = Cell.getStringCellValue();
					Log.debug("ExcelUtils>getCellData:Cell Value is identified as String");
				}
			} else if (Cell.getCellType() == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC) {
				Log.debug("ExcelUtils>getCellData:Cell Value is identified as Numeric");
				CellData = String.valueOf(Cell.getNumericCellValue());

			} else if (Cell.getCellType() == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN) {
				Log.debug("ExcelUtils>getCellData:Cell Value is identified as Boolean");
				CellData = String.valueOf(Cell.getBooleanCellValue());
			} else {
				Log.debug("ExcelUtils>getCellData:Cell Value is identified as String");
				CellData = Cell.getStringCellValue();
			}

		}
		if (CellData.isEmpty()) {
			Log.debug("ExcelUtils>getCellData:In Case if cell is blank setting value return as Blank");
			CellData = "Blank";

		}
		// System.out.println(CellData);
		Log.debug("ExcelUtils>getCellData:Returning Cell Data" + CellData);
		return CellData;
	}

	// This method is use to write value in the excel sheet
	// This method accepts four arguments (Result, Row Number, Column Number &
	// Sheet Name)

	@SuppressWarnings("static-access")
	public static void setCellData(String Result, int RowNum, int ColNum,
			String Path, String SheetName) throws Exception {
		try {
			FileInputStream ReportExcel = new FileInputStream(Path);
			XSSFWorkbook ReportWorkbook = new XSSFWorkbook(ReportExcel);
			XSSFSheet ReportSheet = ReportWorkbook.getSheet(SheetName);
			Row = ReportSheet.getRow(RowNum);
//			Cell = Row.getCell(ColNum, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK);
			if (Cell == null) {
				Cell = Row.createCell(ColNum);
				Cell.setCellValue(Result);
			} else {
				Cell.setCellValue(Result);
			}
			// Constant variables Test Data path and Test Data file name
			FileOutputStream fileOut = new FileOutputStream(Path);
			ReportWorkbook.write(fileOut);
			// fileOut.flush();
			fileOut.close();
			ReportExcel.close();
			// ######DefectFix 25 Feb
			// ExcelWBook = new XSSFWorkbook(new FileInputStream(Path));
		} catch (Exception e) {

		}
	}

	public static void setReportingExcel() {
		try {

			Date date = new Date();
			DateFormat df = new SimpleDateFormat("dd MMM yyyy kk:mm:ss");
			df.setTimeZone(TimeZone.getTimeZone("IST"));
			File SRC_LOG_FILE = FileUtils.getFile(Constant.testSuitWorkbook);
			String LogFileName = df.format(date) + "_Report" + ".xlsx";
			LogFileName = LogFileName.replace(':', '_');
			LogFileName = LogFileName.replace(' ', '_');
			File targetFile = new File(Constant.testreport + LogFileName);
			Constant.ExcelReportLocation = Constant.testreport + LogFileName;
			FileUtils.copyFile(SRC_LOG_FILE, targetFile);
			FileInputStream ExcelFile = new FileInputStream(
					Constant.ExcelReportLocation);
			ExcelWBook = new XSSFWorkbook(ExcelFile);
			ExcelWSheet = ExcelWBook.getSheet(Constant.TestSuitsheet);
		} catch (Exception e) {
			System.out.println("Error Creating Excel Testsuit report"
					+ e.toString());
		}
	}

	public static void CloseReportingExcel(String Result, int RowNum,
			int ColNum, String Path, String SheetName) {
		try {
			fileOut.close();
			ExcelFile.close();
			ExcelWBook = new XSSFWorkbook(new FileInputStream(Path));
		} catch (Exception e) {

		}
	}

	public static void ReadRowTestScript() {
		Log.debug("ExcelUtils>ReadRowTestScript: Reading Test script row Function");

		if (TestCaseRunner.testSuitResult == false
				|| TestCaseRunner.testScriptResult == false) {
			Log.debug("ExcelUtils>ReadRowTestScript: IN Case of test suit/test script result is false function wont go ahead");
			return;
		}

		Constant.TestCaseRow.put("Testcaseid", ExcelUtils.getCellData(
				Constant.testcaserownum, Constant.Testcaseid));

		Constant.TestCaseRow.put("TestStepID", ExcelUtils.getCellData(
				Constant.testcaserownum, Constant.TestStepID));
		Constant.TestCaseRow.put("TeststepDescription", ExcelUtils.getCellData(
				Constant.testcaserownum, Constant.TeststepDescription));
		Constant.TestCaseRow.put("ElementFinderType", ExcelUtils.getCellData(
				Constant.testcaserownum, Constant.ElementFinderType));
		Constant.TestCaseRow.put("Elementlocation", ExcelUtils.getCellData(
				Constant.testcaserownum, Constant.Elementlocation));
		Constant.TestCaseRow.put("Data",
				ExcelUtils.getCellData(Constant.testcaserownum, Constant.Data));
		Constant.TestCaseRow.put("Action", ExcelUtils.getCellData(
				Constant.testcaserownum, Constant.Action));
		Constant.TestCaseRow.put("ActionSupportValue", ExcelUtils.getCellData(
				Constant.testcaserownum, Constant.ActionSupportValue));
		Log.debug("ExcelUtils>ReadRowTestScript: Following values has been read for the test script read row function #Testcaseid"
				+ Constant.TestCaseRow.get("Testcaseid")
				+ "#TeststepDescription: "
				+ Constant.TestCaseRow.get("TeststepDescription")
				+ "#ElementFinderType: "
				+ Constant.TestCaseRow.get("ElementFinderType")
				+ "#Elementlocation: "
				+ Constant.TestCaseRow.get("Elementlocation")
				+ "#Data: "
				+ Constant.TestCaseRow.get("Data")
				+ "#Action: "
				+ Constant.TestCaseRow.get("Action")
				+ "#ActionSupportValue: "
				+ Constant.TestCaseRow.get("ActionSupportValue"));
	}

	public static void ReadRowTestSuit() throws Exception {
		Constant.TestSuit.put("Testcaseid",
				ExcelUtils.getCellData(TestCaseRunner.testSuitindex, 0));
		Constant.TestSuit.put("Description",
				ExcelUtils.getCellData(TestCaseRunner.testSuitindex, 1));
		Constant.TestSuit.put("RunMode",
				ExcelUtils.getCellData(TestCaseRunner.testSuitindex, 2));
		Constant.TestSuit.put("File location",
				ExcelUtils.getCellData(TestCaseRunner.testSuitindex, 3));
		Constant.TestSuit.put("SheetName",
				ExcelUtils.getCellData(TestCaseRunner.testSuitindex, 4));
		Constant.TestSuit.put("Browser",
				ExcelUtils.getCellData(TestCaseRunner.testSuitindex, 5));
		Constant.TestSuit.put("Category",
				ExcelUtils.getCellData(TestCaseRunner.testSuitindex, 6));
		Log.debug("ExcelUtils>ReadRowTestSuit: Following values has been read for the test Suit read row function #Testcaseid"
				+ Constant.TestSuit.get("Testcaseid")
				+ "#Description: "
				+ Constant.TestSuit.get("Description")
				+ "#RunMode: "
				+ Constant.TestSuit.get("RunMode")
				+ "#SheetName: "
				+ Constant.TestSuit.get("SheetName")
				+ "#Browser: "
				+ Constant.TestSuit.get("Browser")
				+ "#Category: "
				+ Constant.TestSuit.get("Category"));
	}

	public static int getRowCount() {
		int iNumber = 0;
		try {
			ExcelWSheet = ExcelWBook.getSheet(Constant.TestSuitsheet);
			iNumber = ExcelWSheet.getLastRowNum() + 1;
		} catch (Exception e) {
			errorMessage = "Class Utils | Method getRowCount | Exception desc : "
					+ e.getMessage();
			Log.error(errorMessage);
			TestCaseRunner.testSuitResult = false;
		}
		return iNumber;
	}

	public static int getTestStepsCount(String SheetName, String sTestCaseID,
			int iTestCaseStart) throws Exception {
		try {
			ExcelWSheet = ExcelWBook.getSheet(Constant.TestScriptSheet);
			int number = ExcelWSheet.getLastRowNum() + 1;
			return number;
		} catch (Exception e) {
			errorMessage = "Class Utils | Method getRowContains | Exception desc : "
					+ e.getMessage();
			Log.error(errorMessage);
			TestCaseRunner.testSuitResult = false;
			return 0;
		}

	}

	public static void getTestDataCount() {
		Log.debug("ExcelUtils>getTestDataCount: Function to read data count from test script");
		try {
			for (int i = 0; i < 256; i++) {
				if (getCellData(0, i).equalsIgnoreCase("Data(Yes)")) {

					Constant.datacolumnnumber.add(i);
				} else if (getCellData(0, i).contains("Blank")) {
					break;
				} else {
					continue;
				}
			}
			if (Constant.datacolumnnumber.size() == 0) {
				Log.debug("ExcelUtils>getTestDataCount: Error message if data count has been found 0");
				fatalMessage = "Not able to find Active Data column, Please make sure that atlease 1 column have 'Data(Yes)' header in respective test script";
				TestCaseRunner.testSuitResult = false;
				Log.fatal(fatalMessage);

			}

		} catch (Exception e) {
			Log.fatal("Error in getting Data count" + e.toString());
			return;
		}
	}

	public static String getreportcelldata(int RowNum, int ColNum, String Path,
			String SheetName) {
		String celldata;
		try {
			FileInputStream ReportExcel = new FileInputStream(Path);
			XSSFWorkbook ReportWorkbook = new XSSFWorkbook(ReportExcel);
			XSSFSheet ReportSheet = ReportWorkbook.getSheet(SheetName);
			Row = ReportSheet.getRow(RowNum);
//			Cell = Row.getCell(ColNum, Row.RETURN_BLANK_AS_NULL);
			celldata = Cell.getStringCellValue();
			ReportExcel.close();
			// ######DefectFix 25 Feb
			// ExcelWBook = new XSSFWorkbook(new FileInputStream(Path));
		} catch (Exception e) {
			return "Blank";
		}
		return celldata;

	}
}
