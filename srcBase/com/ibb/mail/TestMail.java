package com.ibb.mail;

import conf.ConfigReader;

public class TestMail
{
  public static void main(String[] args)
  {
    MailSenderInfo mailInfo = new MailSenderInfo();
    mailInfo.setMailServerHost(ConfigReader.getProperty("serverHost_mail"));
    mailInfo.setMailServerPort(ConfigReader.getProperty("SSLPort_mail"));
    mailInfo.setValidate(true);
    mailInfo.setUserName(ConfigReader.getProperty("userName_mail"));
    mailInfo.setPassword(ConfigReader.getProperty("password_mail"));
    mailInfo.setFromAddress(ConfigReader.getProperty("userName_mail"));
    mailInfo.setToAddress("357035993@qq.com");
    mailInfo.setSubject("设置邮箱标题 如http://localhost:8080/pms/pmsIndex.action 中国桂花网");
    mailInfo.setContent("设置邮箱内容 如http://www.guihua.org 中国桂花网 是中国最大桂花网站==");

  //  String[] files = { "F:\\music\\text1.txt" };
    String[] files={};
    mailInfo.setAttachFileNames(files);
    MultiPartMailSender multiPartMailSender = new MultiPartMailSender();
    MultiPartMailSender.sendHtmlMultiPartMail(mailInfo);
  }
}