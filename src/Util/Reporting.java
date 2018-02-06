package Util;



public class Reporting {

	
	public void Reportupdate(int iRow,int columnno, String actionname, String Path, String sheename) throws Exception
	{
			ExcelUtils.setCellData(actionname,iRow,columnno,Path,sheename);
	}
	
	public void DReportupdate(int iRow,int columnno, String actionname, String Path, String sheename) throws Exception
	{
			ExcelUtils.setCellData(actionname,iRow,columnno,Path,sheename);
	}
}
