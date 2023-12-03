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
  
  private int userNo; // 페이징에 필요
  private String sortType; // 정렬 방식

  
  //카테고리
  

}
