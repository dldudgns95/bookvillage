package kr.co.bookvillage.util;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@PropertySource(value = "classpath:application.yml")
@Component
public class MyJavaUtil {
  
  @Autowired
  private Environment env;
  
  public void sendJavaMail(String to, String subject, String content) {
    
    try {
    
      Properties properties = new Properties();
      properties.put("mail.smtp.host", env.getProperty("spring.mail.host"));
      properties.put("mail.smtp.prot", env.getProperty("spring.mail.host"));
      properties.put("mail.smtp.auth", env.getProperty("spring.mail.properties.mail.smtp.auth"));
      properties.put("mail.smtp.starttls.enable", env.getProperty("spring.mail.properties.mail.smtp.starttls.enable"));
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
}
