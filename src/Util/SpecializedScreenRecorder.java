package Util;

import java.awt.AWTException;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.monte.media.Format;
import org.monte.media.Registry;
import org.monte.screenrecorder.ScreenRecorder;

import Configuration.ActionKeyword;
import Configuration.Constant;



public class SpecializedScreenRecorder extends ScreenRecorder {

	private String name;

	public SpecializedScreenRecorder(GraphicsConfiguration cfg, Rectangle captureArea, Format fileFormat,
			Format screenFormat, Format mouseFormat, Format audioFormat, File movieFolder, String name)
					throws IOException, AWTException {
		super(cfg, captureArea, fileFormat, screenFormat, mouseFormat, audioFormat, movieFolder);
		this.name = name;
	}

	@Override
	protected File createMovieFile(Format fileFormat) {
		if (!movieFolder.exists()) {
			movieFolder.mkdirs();
		} else if (!movieFolder.isDirectory()) {
			Log.error("\"" + movieFolder + "\" is not a directory.");
			ExtentLogs.error("\"" + movieFolder + "\" is not a directory.");
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
		dateFormat.format(new Date());
		Constant.videofilename = name + "-" + Constant.TestSuit.get("Testcaseid") + ActionKeyword.browser + "."
				+ Registry.getInstance().getExtension(fileFormat);
		return new File(movieFolder, name + "-" + Constant.TestSuit.get("Testcaseid") + ActionKeyword.browser + "."
				+ Registry.getInstance().getExtension(fileFormat));

	}
}

