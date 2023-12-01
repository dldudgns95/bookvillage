package kr.co.bookvillage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BookSearchDto {
  private String ss;
  private String st;
  
  //페이징 주소 적기에 필요
  private int userNo;
  private int pageNum;
  private int pageSize;

}
