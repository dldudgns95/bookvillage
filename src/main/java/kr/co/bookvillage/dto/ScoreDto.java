package kr.co.bookvillage.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ScoreDto {
  //score 테이블
  private String isbn;
  private int userNo;
  private Date reviewDate;
  private double star;
  private String review;
  private int good;
  // book 테이블
  private String title;
  private String author;
  private int status;
}
