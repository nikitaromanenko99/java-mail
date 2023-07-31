package ee.microlink.livelink.mail;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.io.*;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Sender {
  
  public Sender() {
  }
  
  public Sender(String logFilePath) {
  }
  
  public void send(String host, int port, String to, String from, String subject, String body, String files) {
    boolean debug = true;
    Properties props = new Properties();
    props.put("mail.smtp.host", host);
    props.put("mail.smtp.port", port);
    if (debug)
      props.put("mail.debug", host); 
    Session session = Session.getInstance(props, null);
    session.setDebug(debug);
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    try {
      MimeMessage mimeMessage = new MimeMessage(session);
      mimeMessage.setFrom((Address)new InternetAddress(from));
      InternetAddress[] address = { new InternetAddress(to) };
      mimeMessage.setRecipients(Message.RecipientType.TO, (Address[])address);
      mimeMessage.setSubject(subject);
      mimeMessage.setSentDate(new Date());
      MimeMultipart mimeMultipart = new MimeMultipart();
      MimeBodyPart mbp1 = new MimeBodyPart();
      mbp1.setContent(getHTMLBody(in, (Message)mimeMessage, body), "text/html");
      mimeMultipart.addBodyPart((BodyPart)mbp1);
      mimeMessage.setContent((Multipart)mimeMultipart);
      Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
      Transport.send((Message)mimeMessage);
      System.out.println("Mail sent to: " + to);
    } catch (MessagingException ex) {
    	System.out.println((Throwable)ex);
      do {
        if (ex instanceof SendFailedException) {
          SendFailedException sfex = (SendFailedException)ex;
          Address[] invalid = sfex.getInvalidAddresses();
          if (invalid != null) {
        	  System.out.println("    ** Invalid Addresses");
            if (invalid != null)
              for (int i = 0; i < invalid.length; i++)
            	  System.out.println("         " + invalid[i]);  
          } 
          Address[] validUnsent = sfex.getValidUnsentAddresses();
          if (validUnsent != null) {
        	  System.out.println("    ** ValidUnsent Addresses");
            if (validUnsent != null)
              for (int j = 0; j < validUnsent.length; j++)
            	  System.out.println("         " + validUnsent[j]);  
          } 
          Address[] validSent = sfex.getValidSentAddresses();
          if (validSent != null) {
        	  System.out.println("    ** ValidSent Addresses");
            if (validSent != null)
              for (int j = 0; j < validSent.length; j++)
            	  System.out.println("         " + validSent[j]);  
          } 
        } 
        if (ex instanceof MessagingException) {
          ex = (MessagingException)ex.getNextException();
        } else {
          ex = null;
        } 
      } while (ex != null);
    } 
  }
  
  public String getHTMLBody(BufferedReader in, Message msg, String body) throws MessagingException {
    String subject = msg.getSubject();
    StringBuffer sb = new StringBuffer();
    sb.append("<HTML>\n");
    sb.append("<HEAD>\n");
    sb.append("<TITLE>\n");
    sb.append(subject + "\n");
    sb.append("</TITLE>\n");
    sb.append("</HEAD>\n");
    sb.append("<BODY>\n");
    sb.append(body + "\n");
    sb.append("</BODY>\n");
    sb.append("</HTML>\n");
    return sb.toString();
  }
}
