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
  private String ss; // 검색 범위 select 결과
  private String st; // 검색어
  
  //--페이징 주소 적기에 필요한지 안한지 모르겠음
  private int userNo;
  private int pageNum;
  private int pageSize;

}
