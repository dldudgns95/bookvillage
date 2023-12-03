package kr.co.bookvillage.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class WishDto {
  //wish 테이블
  private String isbn;
  private int userNo;
  private Timestamp wishDate;
  //book 테이블
  private String title;
  private String cover;
  private String author;
  
  // 추가
  private int status; //--나중에
}
