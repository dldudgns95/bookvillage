package kr.co.bookvillage.dto;

import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ScoreDto {
  private String isbn;
  private int userNo;
  private Date reviewDate;
  private int star;
  private String review;
  
  //bookDto (마이페이지 표시 위해서 조인)
  private BookDto bookDto;
  private String title;
  private String author;
  private int status;
  
  //추가한 dto
  private int recommend; //추천순 정렬에서 사용? --나중에

  //평균별점
  private List<ScoreDto> scores;
  private int totalScore;
  private double avgScore;
  private int reader;
  public double starRating() {
    totalScore =0;
    reader=0;
    for (ScoreDto score : scores) {
      totalScore += score.getStar();
      reader++;
    }
    avgScore = (double)totalScore/reader;
    return avgScore;
  }
}
