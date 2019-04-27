package novopay.in.batchautomation.utils;

import java.util.HashMap;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
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
import javax.print.attribute.standard.DateTimeAtCompleted;

import org.apache.poi.openxml4j.util.ZipFileZipEntrySource;

public class EmailUtils {

	public String newFileName;
	public String emailTopUpFile(HashMap<String, String> emailData,String fileName) {
	
		final String from = JavaUtils.configProperties.get("mail.id");
		final String fromPass = JavaUtils.configProperties.get("mail.password");
		String[] to = { emailData.get("username") };
		String subject = emailData.get("subject");
		String bodyText = "";
		String topupFilePath= JavaUtils.configProperties.get("batchfiledir")+fileName;
		Properties properties = System.getProperties();
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, fromPass);
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			for (String str : to) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(str));
			}

			message.setSubject(subject);
			Multipart multipart = new MimeMultipart();
			MimeBodyPart messageBodyPart1 = new MimeBodyPart();
			messageBodyPart1.setText(bodyText);

			MimeBodyPart messageBodyPart2 = new MimeBodyPart();
			DataSource source = new FileDataSource(topupFilePath);
			messageBodyPart2.setDataHandler(new DataHandler(source));
			if (fileName.contains("xlsx"))
			{
				String extension = fileName.substring(fileName.length()-5,fileName.length());
				newFileName = fileName.substring(0,fileName.length()-5)+System.currentTimeMillis()+extension;	
			}
			else 
			{
			String extension = fileName.substring(fileName.length()-4,fileName.length());
			newFileName = fileName.substring(0,fileName.length()-4)+System.currentTimeMillis()+extension;
			}
			System.out.println(newFileName);
			messageBodyPart2.setFileName(newFileName);

			multipart.addBodyPart(messageBodyPart1);
			multipart.addBodyPart(messageBodyPart2);
			message.setContent(multipart);
			Transport.send(message);
			System.out.println("Mail sent...");
			return newFileName;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
