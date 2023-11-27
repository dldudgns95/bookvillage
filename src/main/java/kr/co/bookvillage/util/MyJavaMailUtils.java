package kr.co.bookvillage.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@PropertySource(value="classpath:application.yml")
@Component
public class MyJavaMailUtils {

  @Autowired
  private Environment env;
  
  public void sendJavaMail(String to, String subject, String content) {
    
    try {
      
      Properties properties = new Properties();
      properties.put("mail.smtp.host", env.getProperty("spring.mail.host"));
      properties.put("mail.smtp.port", env.getProperty("spring.mail.host"));
      properties.put("mail.smtp.auth", env.getProperty("spring.mail.properties.mail.smtp.auth"));
      properties.put("mail.smtp.starttls.enable", env.getProperty("spring.mail.properties.mail.smtp.starttls.enable"));
      
      Session session = Session.getInstance(properties, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(env.getProperty("spring.mail.username"), env.getProperty("spring.mail.password"));
        }
      });
      
      MimeMessage mimeMessage = new MimeMessage(session);
      mimeMessage.setFrom(new InternetAddress(env.getProperty("spring.mail.username"), "사이트관리자"));
      mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
      mimeMessage.setSubject(subject);
      mimeMessage.setContent(content, "text/html; charset=UTF-8");
      
      Transport.send(mimeMessage);
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
}