package kr.co.bookvillage.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class CommentMyFileUtils {

  // 문의 작성시 사용된 이미지가 저장될 경로 반환하기
  public String getAskImagePath() {
    LocalDate today = LocalDate.now();
    return "/comment/" + DateTimeFormatter.ofPattern("yyyy/MM/dd").format(today);
  }
  
  // 문의 이미지가 저장된 어제 경로를 반환
  public String getAskImagePathInYesterday() {
    LocalDate date = LocalDate.now();
    date = date.minusDays(1);  // 1일 전
    return "/comment/" + DateTimeFormatter.ofPattern("yyyy/MM/dd").format(date);
  }
  
  
  
  // 파일이 저장될 이름 반환하기
  public String getFilesystemName(String originalFilename) {
    
    /*  UUID.확장자  */
    
    String extName = null;
    if(originalFilename.endsWith("tar.gz")) {  // 확장자에 마침표가 포함되는 예외 경우를 처리한다.
      extName = "tar.gz";
    } else {
      String[] arr = originalFilename.split("\\.");  // [.] 또는 \\.
      extName = arr[arr.length - 1];
    }
    
    return UUID.randomUUID().toString().replace("-", "") + "." + extName;
    
  }

  // 임시 파일 이름 반환하기 (확장자는 제외하고 이름만 반환)
  public String getTempFilename() {
    return System.currentTimeMillis() + "";
  }
  
  
}