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
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Configuration.ActionKeyword;
import Configuration.Constant;

public class ExcelReporting {

	static int CurrentRowCount = 0;

	public void Reportupdate(int iRow, int columnno, String actionname,
			String Path, String sheename) throws Exception {
		ExcelUtils.setCellData(actionname, iRow, columnno, Path, sheename);
	}

	public static void TestSuitReportUpdate(String Status) {
		boolean update = true;
		if (Constant.ExcelReport) {

			if (Status.contains("Close")) {
				CurrentRowCount++;
			}

			if (Status.contains("Error") || Status.contains("Pass")) {
				String cellvalue = ExcelUtils.getreportcelldata(
						CurrentRowCount, Constant.ExcelReportColumnNO,
						Constant.ExcelReportLocation, Constant.TestSuitsheet);
				if (cellvalue.contains("Blank")) {
					update = true;
				} else if (cellvalue.contains("Fail")||cellvalue.contains("Fatal")) {
					update = false;
				}
			}
			Log.debug("Reporting>DReportupdate:Reporting test case status in excel report");
			try {
				if (Constant.ExcelReport && update) {
					ExcelUtils.setCellData(Status, CurrentRowCount,
							Constant.ExcelReportColumnNO,
							Constant.ExcelReportLocation,
							Constant.TestSuitsheet);
				}

			} catch (Exception e) {
				Log.debug("Reporting>DReportupdate:Reporting test case status in excel report");
				System.out.println("Error reporting Excel Testsuit report"
						+ e.toString());

			}
		}
		// CurrentRowCount++;
	}

	public static void UpdateRunStatus(String Status) {
		boolean update = true;
		if (Constant.ExcelReport) {

			if (Status.contains("Close")) {
				UpdateNewRow();
			}
			Log.debug("Reporting>UpdateRunStatus:Reporting test case runmode in excel report");
			try {
				if (Constant.ExcelReport) {

					String cellvalue = ExcelUtils.getreportcelldata(
							CurrentRowCount, 2,
							Constant.ExcelReportLocation,
							Constant.TestSuitsheet);
					if (cellvalue.contains("Blank")) {
						update = true;
					} else if (cellvalue.contains("Yes")||cellvalue.contains("No")) {
						update = false;
					}
					if (update) {
						ExcelUtils.setCellData(Status, CurrentRowCount, 2,
								Constant.ExcelReportLocation,
								Constant.TestSuitsheet);
					}
				}
			} catch (Exception e) {
				Log.debug("Reporting>UpdateRunStatus:Reporting test case status in excel report");
				System.out.println("Error updating Excel Testsuit runmode"
						+ e.toString());

			}
		}
		// CurrentRowCount++;
	}

	public static void CreateReport() {
		if (Constant.ExcelReport) {
			try {
				Date date = new Date();
				DateFormat df = new SimpleDateFormat("dd MMM yyyy kk:mm:ss");
				df.setTimeZone(TimeZone.getTimeZone("IST"));
				String ReportFileName = df.format(date) + "_Report" + ".xlsx";
				ReportFileName = ReportFileName.replace(':', '_');
				ReportFileName = ReportFileName.replace(' ', '_');

				FileOutputStream fileOut = new FileOutputStream(
						Constant.testreport + ReportFileName);
				Constant.ExcelReportLocation = Constant.testreport
						+ ReportFileName;
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet worksheet = workbook.createSheet("Testsuit");
				XSSFRow Header = worksheet.createRow(0);
				// Creating Header part
				// Setting Formate

				XSSFCell r1c1 = Header.createCell(0);
				r1c1.setCellValue("Test case Id");

				XSSFCell r1c2 = Header.createCell(1);
				r1c2.setCellValue("Description");

				XSSFCell r1c3 = Header.createCell(2);
				r1c3.setCellValue("RunMode(Yes/No)");

				XSSFCell r1c4 = Header.createCell(3);
				r1c4.setCellValue("File location");

				XSSFCell r1c5 = Header.createCell(4);
				r1c5.setCellValue("SheetName");

				XSSFCell r1c6 = Header.createCell(5);
				r1c6.setCellValue("Browser");

				XSSFCell r1c7 = Header.createCell(6);
				r1c7.setCellValue("Category");

				XSSFCell r1c8 = Header.createCell(7);
				r1c8.setCellValue("Data");

				XSSFCell r1c9 = Header.createCell(7);
				r1c9.setCellValue("Result");

				// Updating the size of cell
				worksheet.autoSizeColumn(0);
				worksheet.autoSizeColumn(2);
				worksheet.autoSizeColumn(3);
				worksheet.autoSizeColumn(4);
				worksheet.autoSizeColumn(5);
				worksheet.autoSizeColumn(6);
				worksheet.autoSizeColumn(7);
				worksheet.autoSizeColumn(8);
				// Closing file
				workbook.write(fileOut);
				fileOut.flush();
				fileOut.close();
				// System.out.println("Done");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// CurrentRowCount++;

	}

	public static void PrepareExcelReport() {
		if (Constant.ExcelReport) {
			try {
				Date date = new Date();
				DateFormat df = new SimpleDateFormat("dd MMM yyyy kk:mm:ss");
				df.setTimeZone(TimeZone.getTimeZone("IST"));
				File SRC_LOG_FILE = FileUtils
						.getFile(Constant.testSuitWorkbook);
				String LogFileName = df.format(date) + "_Report" + ".xlsx";
				LogFileName = LogFileName.replace(':', '_');
				LogFileName = LogFileName.replace(' ', '_');
				Constant.AttachmentLog = LogFileName;
				File targetFile = new File(Constant.testreport + LogFileName);
				Constant.ExcelReportLocation = Constant.testreport
						+ LogFileName;
				FileUtils.copyFile(SRC_LOG_FILE, targetFile);
			} catch (IOException e) {
				System.out.println("Error Creating Excel Testsuit report"
						+ e.toString());
				return;
			} catch (Exception e) {
				System.out.println("Error Creating Excel Testsuit report"
						+ e.toString());
				return;

			}
		}
	}

	public static void UpdateNewRow() {

		if (Constant.ExcelReport) {
			CurrentRowCount++;
			try {
				FileInputStream fileIn = new FileInputStream(
						Constant.ExcelReportLocation);
				XSSFWorkbook workbook = new XSSFWorkbook(fileIn);
				XSSFSheet worksheet = workbook.getSheet("Testsuit");
				XSSFRow WorkingRow = worksheet.createRow(CurrentRowCount);

				// Creating row default Entry part

				XSSFCell r1c1 = WorkingRow.createCell(0);
				r1c1.setCellValue(Constant.TestSuit.get("Testcaseid"));

				XSSFCell r1c2 = WorkingRow.createCell(1);
				r1c2.setCellValue(Constant.TestSuit.get("Description"));

				XSSFCell r1c3 = WorkingRow.createCell(2);
				// r1c3.setCellValue(Constant.TestSuit.get("RunMode"));

				XSSFCell r1c4 = WorkingRow.createCell(3);
				r1c4.setCellValue(Constant.TestSuit.get("File location"));

				XSSFCell r1c5 = WorkingRow.createCell(4);
				r1c5.setCellValue(Constant.TestSuit.get("SheetName"));

				XSSFCell r1c6 = WorkingRow.createCell(5);
				r1c6.setCellValue(ActionKeyword.browser);

				XSSFCell r1c7 = WorkingRow.createCell(6);
				r1c7.setCellValue(Constant.TestSuit.get("Category"));

				XSSFCell r1c8 = WorkingRow.createCell(7);
				r1c8.setCellValue("Data");

				XSSFCell r1c9 = WorkingRow.createCell(7);
				// r1c9.setCellValue("Result");
				// Updating the size of cell
				worksheet.autoSizeColumn(0);
				worksheet.autoSizeColumn(2);
				worksheet.autoSizeColumn(3);
				worksheet.autoSizeColumn(4);
				worksheet.autoSizeColumn(5);
				worksheet.autoSizeColumn(6);
				worksheet.autoSizeColumn(7);
				worksheet.autoSizeColumn(8);
				// Closing file
				FileOutputStream fileOut = new FileOutputStream(
						Constant.ExcelReportLocation);
				workbook.write(fileOut);
				fileOut.flush();
				fileOut.close();
				// System.out.println("Done");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public static void UpdateRunStatusClose(String Status) {
		if (Constant.ExcelReport) {
			CurrentRowCount++;
			Log.debug("Reporting>DReportupdate:Reporting test case status in excel report");
			try {
				if (Constant.ExcelReport) {
					ExcelUtils.setCellData(Status, CurrentRowCount,
							Constant.ExcelReportColumnNO,
							Constant.ExcelReportLocation,
							Constant.TestSuitsheet);
				}

			} catch (Exception e) {
				Log.debug("Reporting>DReportupdate:Reporting test case status in excel report");
				System.out.println("Error reporting Excel Testsuit report"
						+ e.toString());

			}
		}

	}
}