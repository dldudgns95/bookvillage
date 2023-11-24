package kr.co.bookvillage.util;

import java.security.MessageDigest;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class MySecurityUtils {
  
  /* SHA256 암호화
   * 1. 입력값을 256비트(32바이트)로 암호화하는 해시 알고리즘이다.
   * 2. 원본을 암호화할 수 있느나, 암호화된 결과를 원본으로 되돌리는 복호화는 불가능하다.
   *    (암호화 결과를 오픈해놔도 상관 없다.)
   * 3. java.security 패키지를 활용해서 구연하거나, 암호화 디펜던시(예시 commons-lang3)를 활용
   * 
   */
 
  public String getSHA256(String password) {
    StringBuilder sb = new StringBuilder();
    try {
      MessageDigest messageDigest= MessageDigest.getInstance("SHA-256");
      messageDigest.update(password.getBytes());
      byte[] b = messageDigest.digest();  // 암호화된 32바이트 배열이 생성됨 -> 16바이트 어쩌구 - 64글자 탄생!
      for(int i = 0; i< b.length; i++) {
        sb.append(String.format("%02X", b[i]));  // 2자리 16진수 문자열로 변환하기
        
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
