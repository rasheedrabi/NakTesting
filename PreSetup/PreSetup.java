import java.io.File;
import java.io.PrintWriter;


public class runner {

	/**
	 * @param args
	 */
	
	//XML file is placed at D:\Selenium\abc.xml
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String currentdir = System.getProperty("user.dir");
		System.out.println("Current working directory : " + currentdir);
		folderstr(currentdir);
		System.out.println("---Pre-Setup tasks done!---");

	}
	
	public static void folderstr(String Sourceloc) {
		
		File files = new File(Sourceloc+"\\src\\Sikuli\\Images Unitaction\\");
		if (!files.exists()) {
			if (files.mkdirs()) {
				System.out.println("Multiple directories created :"+Sourceloc+"\\src\\Sikuli\\Images Unitaction\\");
			} else {
				System.out.println("Failed to create :"+Sourceloc+"\\src\\Sikuli\\Images Unitaction\\");
				System.out.println("Either create the directories manually or contact System Administrator.");
			}
		}
		
		File files2 = new File(Sourceloc+"\\driver\\");
		if (!files2.exists()) {
			if (files2.mkdirs()) {
				System.out.println("Driver directory created :"+Sourceloc+"\\driver\\");
				
	
			} else {
				System.out.println("Failed to create :"+Sourceloc+"\\driver\\");
				System.out.println("Either create the directories manually or contact System Administrator.");
			}
		}
		
		File files3 = new File(Sourceloc+"\\src\\Util\\");
		if (!files3.exists()) {
			if (files3.mkdirs()) {
				System.out.println("Util directory created :"+Sourceloc+"\\src\\Util\\");
				
			} else {
				System.out.println("Failed to create "+Sourceloc+"\\src\\Util\\");
				System.out.println("Either create the directories manually or contact System Administrator.");
			}
		}
		
		File files4 = new File(Sourceloc+"\\Screenshots\\VerificationScreenshots\\");
		if (!files4.exists()) {
			if (files4.mkdirs()) {
				System.out.println("VerificationScreenshots directory created :"+Sourceloc+"\\Screenshots\\VerificationScreenshots\\");
			} else {
				System.out.println("Failed to create "+Sourceloc+"\\Screenshots\\VerificationScreenshots\\");
				System.out.println("Either create the directories manually or contact System Administrator.");
			}
		}
	
		File files5 = new File(Sourceloc+"\\Screenshots\\ErrorScreenshots\\");
		if (!files5.exists()) {
			if (files5.mkdirs()) {
				System.out.println("ErrorScreenshots directory created :"+Sourceloc+"\\Screenshots\\ErrorScreenshots\\");
			} else {
				System.out.println("Failed to create "+Sourceloc+"\\Screenshots\\ErrorScreenshots\\");
				System.out.println("Either create the directories manually or contact System Administrator.");
			}
		}
		
		File files6 = new File(Sourceloc+"\\SikuliX\\");
		if (!files6.exists()) {
			if (files6.mkdirs()) {
				System.out.println("SikuliX directory created :"+Sourceloc+"\\SikuliX\\");
			} else {
				System.out.println("Failed to create "+Sourceloc+"\\SikuliX\\");
				System.out.println("Either create the directories manually or contact System Administrator.");
			}
		}
		
		File files7 = new File(Sourceloc+"\\src\\Logs\\");
		if (!files7.exists()) {
			if (files7.mkdirs()) {
				System.out.println("Logs will be created : "+Sourceloc+"\\src\\Logs\\");
				//Place AUTH.exe here
			} else {
				System.out.println("Failed to create "+Sourceloc+"\\src\\Logs\\");
				System.out.println("Either create the directories manually or contact System Administrator.");
			}
		}
		
		File files8 = new File(Sourceloc+"\\src\\DataEngine\\");
		if (!files8.exists()) {
			if (files8.mkdirs()) {
				System.out.println("DataEngine directory created :"+Sourceloc+"\\src\\DataEngine\\");
			} else {
				System.out.println("Failed to create DataEngine directory!");
			}
		}
		
		File files9 = new File(Sourceloc+"\\src\\reports\\");
		if (!files9.exists()) {
			if (files9.mkdirs()) {
				System.out.println("Test reports will be generated at : " + Sourceloc+"\\src\\reports\\");
			} else {
				System.out.println("Failed to create reports directory!");
			}
		}
		
		try{
		PrintWriter writer = new PrintWriter("Read this.txt", "UTF-8");
		writer.println("Following needs to be taken care before running the automation.");
		writer.println("Place the testsuit.xlsx file at " +Sourceloc+"\\src\\DataEngine\\");
		writer.println("Place the runsikulix.bat file at "+Sourceloc+"\\SikuliX\\");
		writer.println("Place the AUTH.exe file at "+Sourceloc+"\\src\\Util\\");
		writer.println("Place the chromedriver.exe and 32 bit IEDriverServer.exe at "+Sourceloc+"\\driver\\");
		writer.println("\t***\t");
		writer.close();
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
