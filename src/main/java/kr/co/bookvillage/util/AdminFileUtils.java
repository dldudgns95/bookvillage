package kr.co.bookvillage.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class AdminFileUtils {
    
  // 맥 전용 반환
   public String getFacMacImagePath() {
     LocalDate today = LocalDate.now();
     return "/Users/seoga-eul/fac/" + DateTimeFormatter.ofPattern("yyyy/MM/dd").format(today);
   }

  // 블로그 작성시 사용된 이미지가 저장될 경로 반환하기
  public String getFacImagePath() {
    LocalDate today = LocalDate.now();
    return "/admin/fac/" + DateTimeFormatter.ofPattern("yyyy/MM/dd").format(today);
  }
  
  // 도서 직접 등록시 사용된 이미지가 저장될 경로 반환하기
  public String getBookImagePath() {
    LocalDate today = LocalDate.now();
    return "/admin/book/" + DateTimeFormatter.ofPattern("yyyy/MM/dd").format(today);
  }
  
  // 블로그 이미지가 저장된 어제 경로를 반환
  public String getFacImagePathInYesterday() {
    LocalDate date = LocalDate.now();
    date = date.minusDays(1);  // 1일 전
    return "/admin/fac/" + DateTimeFormatter.ofPattern("yyyy/MM/dd").format(date);
  }
  
  //시설 게시판 작성 시 첨부한 파일이 저장될 경로 반환하기
   public String getFacPath() {
     LocalDate today = LocalDate.now();
     return "/admin/fac/" + DateTimeFormatter.ofPattern("yyyy/MM/dd").format(today);
   }
  
  // 임시 파일이 저장될 경로 반환하기 (zip 파일)
  public String getTempPath() {
    return "/admin/temp/";
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