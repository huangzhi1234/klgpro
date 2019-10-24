package com.ibb.mail;

import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * 带附件的邮件发送
 * @author xiaozhou
 * @date 2014-3-28 上午10:10:48 
 */
public class MultiPartMailSender
{
	/**
	 * 带附件的邮件发送
	 *
	 * @author  xiaozhou   
	 * @date 2014-3-28 上午10:10:48 
	 * @param mailInfo
	 * @return
	 */
  public static boolean sendHtmlMultiPartMail(MailSenderInfo mailInfo)
  {
    MyAuthenticator authenticator = null;
    Properties pro = mailInfo.getProperties();

    if (mailInfo.isValidate()) {
      authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
    }

    Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
    try
    {
      Message mailMessage = new MimeMessage(sendMailSession);

      Address from = new InternetAddress(mailInfo.getFromAddress());

      mailMessage.setFrom(from);

      if (mailInfo.getToAddresses() != null) {
        Address[] tos = new InternetAddress[mailInfo.getToAddresses().length];
        for (int i = 0; i < mailInfo.getToAddresses().length; ++i) {
          tos[i] = new InternetAddress(mailInfo.getToAddresses()[i]);
        }

        mailMessage.setRecipients(Message.RecipientType.TO, tos);
      }
      else {
        Address to = new InternetAddress(mailInfo.getToAddress());

        mailMessage.setRecipient(Message.RecipientType.TO, to);
      }

      mailMessage.setSubject(mailInfo.getSubject());

      mailMessage.setSentDate(new Date());

      Multipart mainPart = new MimeMultipart();

      BodyPart html = new MimeBodyPart();

      html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
      mainPart.addBodyPart(html);

      if (mailInfo.getAttachFileNames() != null)
      {
        for (int i = 0; i < mailInfo.getAttachFileNames().length; ++i) {
          MimeBodyPart messageBodyPart2 = new MimeBodyPart();

          String filename = mailInfo.getAttachFileNames()[i];

          FileDataSource fds = new FileDataSource(filename);

          messageBodyPart2.setDataHandler(new DataHandler(fds));

          messageBodyPart2.setFileName(fds.getName());

          mainPart.addBodyPart(messageBodyPart2);
        }
      }

      mailMessage.setContent(mainPart);

      mailMessage.setSentDate(new Date());
      mailMessage.saveChanges();

      Transport transport = sendMailSession.getTransport("smtp");

      Transport.send(mailMessage);
      transport.close();
      return true;
    } catch (MessagingException ex) {
      ex.printStackTrace();
    }
    return false;
  }
}