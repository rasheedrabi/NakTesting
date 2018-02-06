package Util;

import static org.monte.media.FormatKeys.EncodingKey;
import static org.monte.media.FormatKeys.FrameRateKey;
import static org.monte.media.FormatKeys.KeyFrameIntervalKey;
import static org.monte.media.FormatKeys.MIME_AVI;
import static org.monte.media.FormatKeys.MediaTypeKey;
import static org.monte.media.FormatKeys.MimeTypeKey;
import static org.monte.media.VideoFormatKeys.CompressorNameKey;
import static org.monte.media.VideoFormatKeys.DepthKey;
import static org.monte.media.VideoFormatKeys.ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE;
import static org.monte.media.VideoFormatKeys.QualityKey;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;

import org.monte.media.Format;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

import Configuration.Constant;

public class VideoRecorder {
	private ScreenRecorder screenRecorder;
	static VideoRecorder videoReord = new VideoRecorder();

	public static void Startvideo() {
		try {
			videoReord.startRecording();
		} catch (Exception e) {
			Log.fatal("Error in Start video recording" + e.toString());
			return;
		}

	}

	public static void Stoptvideo() {
		try {
			if (videoReord == null) {
				return;
			}

			videoReord.stopRecording();
		} catch (Exception e) {
			Log.fatal("Error in Stop video" + e.toString());
			return;
		}
	}

	public void startRecording() {
		try {

			GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
					.getDefaultConfiguration();

			File file = new File(Constant.videofilelocation);

			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int width = screenSize.width;
			int height = screenSize.height;
			Rectangle captureSize = new Rectangle(0, 0, width, height);

			screenRecorder = new SpecializedScreenRecorder(gc, captureSize,
					new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
					new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
							CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey,
							Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
					new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
					null, file, "");
			screenRecorder.start();
		} catch (Exception e) {
			Log.fatal("Error in Start video recording" + e.toString());
			return;
		}

	}

	public void stopRecording() {
		try {
			System.out.println(screenRecorder.getStateMessage());
			if (screenRecorder == null) {
				return;
			}
			screenRecorder.stop();
		} catch (Exception e) {
			Log.fatal("Error in Start video recording" + e.toString());
			return;
		}
	}
}
