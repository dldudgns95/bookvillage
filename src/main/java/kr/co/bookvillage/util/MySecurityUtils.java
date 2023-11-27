package kr.co.bookvillage.util;

import java.security.MessageDigest;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class MySecurityUtils {
  
 
  public String getSHA256(String password) {
    StringBuilder sb = new StringBuilder();
    try {
      MessageDigest messageDigest= MessageDigest.getInstance("SHA-256");
      messageDigest.update(password.getBytes());
      byte[] b = messageDigest.digest(); 
      for(int i = 0; i< b.length; i++) {
        sb.append(String.format("%02X", b[i]));  
        
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return sb.toString();
  }
  
  // 인증코드 반환
  public String getRandomString(int count, boolean letters, boolean numbers) {
    return RandomStringUtils.random(count, letters, numbers);
  }
  
  // 크로스 사이트 스크립팅(Cross Site Scripting) 방지
  public String preventXSS(String source) {
    return source.replace("<", "&lt;").replace(">", "&gt;");
  }
  
  
  
  
  
  

}
