package Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.FileUtils;

import Configuration.Constant;

public class Mail_Util {

	public static void Sendmail() {
		File file = null;
		// Sender's email ID needs to be mentioned
		String from = "tester9120@gmail.com";// change accordingly
		final String username = Constant.emailUsername;// change accordingly
		final String password = Constant.emailPassword;// change accordingly

		// Assuming you are sending email through Gmail
		String host = "smtp.gmail.com";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");

		// Get the Session object.
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		try {
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.

			List<String> tolist = new ArrayList<String>();
			List<String> CClist = new ArrayList<String>();

			StringTokenizer todetail = new StringTokenizer(Constant.tolist, ";");
			StringTokenizer ccdetail = new StringTokenizer(Constant.CClist, ";");

			while (todetail.hasMoreTokens()) {
				tolist.add(todetail.nextToken());
			}
			while (ccdetail.hasMoreTokens()) {
				CClist.add(ccdetail.nextToken());
			}

			for (int i = 0; i < tolist.size(); i++) {
				// System.out.println(Constant.tolist.get(i));
				message.addRecipients(Message.RecipientType.TO,
						InternetAddress.parse(tolist.get(i)));

			}
			for (int i = 0; i < CClist.size(); i++) {
				message.addRecipients(Message.RecipientType.CC,
						InternetAddress.parse(CClist.get(i)));
			}
			// Set Subject: header field
			BodyPart messageBodyPart = new MimeBodyPart();
			Multipart multipart = new MimeMultipart();
			message.setSubject(Constant.EmailSubject);
			String filename = Constant.testreport + "TestReport.html";
			DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(source.getName());
			multipart.addBodyPart(messageBodyPart);

			// Attaching Log File
			messageBodyPart = new MimeBodyPart();
			String Logfilename = Constant.Sourceloc + "\\logfile.log";
			DataSource Logssource = new FileDataSource(Logfilename);
			messageBodyPart.setDataHandler(new DataHandler(Logssource));
			messageBodyPart.setFileName(Logssource.getName());
			multipart.addBodyPart(messageBodyPart);

			messageBodyPart = new MimeBodyPart();
			if (Constant.SEND_BATCH) {
				// Zipping and then cleaning the folder
				try {
					ZipUtils.main(null);
					file = new File(Constant.BATCH_LOCATION);
					// if (file != null && file.isDirectory())
					// FileUtils.cleanDirectory(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
				String batchFilename = Constant.ZIPPED_LOCATION
						+ File.separator + Constant.ZIPPED_FILENAME;
				File batchFile = new File(batchFilename);
				File logFile = new File(Logfilename);
				long fileSizeInBytes = batchFile.length() + logFile.length();
				long fileSizeInKB = fileSizeInBytes / 1024;
				long fileSizeInMB = fileSizeInKB / 1024;

				if (fileSizeInMB > 25) {
					System.out
							.println("Batch file and Log file sum-up to greater than 25mb. Unable to send batch file.");
					messageBodyPart = new MimeBodyPart();
					messageBodyPart.setText(Constant.EmailBody
							+ Constant.ZIPPED_FILENAME + Constant.EmailBody1);
					multipart.addBodyPart(messageBodyPart);
				} else {
					file = new File(Constant.BATCH_LOCATION);
					generateFileList(file);

					for (String fileName : fileList) {
						messageBodyPart = new MimeBodyPart();
						DataSource dataSrc = new FileDataSource(fileName);
						messageBodyPart
								.setDataHandler(new DataHandler(dataSrc));
						messageBodyPart.setFileName(dataSrc.getName());
						multipart.addBodyPart(messageBodyPart);
					}
					messageBodyPart = new MimeBodyPart();
					messageBodyPart.setText(Constant.EmailBody
							+ Constant.ZIPPED_FILENAME + Constant.EmailBody1);
					multipart.addBodyPart(messageBodyPart);
				}

			}

			else {
				messageBodyPart = new MimeBodyPart();
				messageBodyPart.setText(Constant.EmailBodyWithoutZipAttachment);
				multipart.addBodyPart(messageBodyPart);
			}

			// Attaching zipped batch file

			/*
			 * if (Constant.SEND_BATCH) { messageBodyPart = new MimeBodyPart();
			 * String batchFilename = Constant.BATCH_LOCATION + File.separator +
			 * Constant.ZIPPED_FILENAME; File batchFile = new
			 * File(batchFilename); File logFile = new File(Logfilename); // Get
			 * length of file in // bytes long fileSizeInBytes =
			 * batchFile.length() + logFile.length(); // Convert // the // bytes
			 * // to // Kilobytes // (1 // KB // = // 1024 // Bytes) long
			 * fileSizeInKB = fileSizeInBytes / 1024; // Convert the KB to //
			 * MegaBytes (1 MB = // 1024 KBytes) long fileSizeInMB =
			 * fileSizeInKB / 1024;
			 * 
			 * if (fileSizeInMB > 25) { System.out .println(
			 * "Batch file and Log file sum-up to greater than 25mb. Unable to send batch file."
			 * );
			 * 
			 * messageBodyPart .setText(Constant.EmailBodyWithoutZipAttachment);
			 * } else { DataSource dataSource = new
			 * FileDataSource(batchFilename); messageBodyPart.setDataHandler(new
			 * DataHandler(dataSource));
			 * messageBodyPart.setFileName(dataSource.getName());
			 * messageBodyPart.setText(Constant.EmailBody);
			 * multipart.addBodyPart(messageBodyPart); } }
			 */

			// Send the complete message parts
			message.setContent(multipart);
			// Set text message part
			Transport.send(message);
			System.out.println("Test Report is sent");
			if (Constant.SEND_BATCH) {

				try {
					File location = new File(Constant.BATCH_LOCATION);
					FileUtils.cleanDirectory(location);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		catch (MessagingException e) {
			// Log.error("Error in sending mail, Please refer mention error
			// message for more details"+e.toString());
			return;
		}
	}

	static List<String> fileList = new ArrayList<String>();

	public static void generateFileList(File node) {

		// add file only
		if (node.isFile()) {
			fileList.add(node.toString());
		}

		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateFileList(new File(node, filename));
			}
		}
	}
}