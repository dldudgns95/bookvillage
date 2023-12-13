package kr.co.bookvillage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnswerDto {
  
  private int ansNo;
  private String ansContent;
  private UserDto userDto; // int userNo
  private int askNo;
  private String createdDate;
  private int status;
  private int depth;
  private int groupNo;
  
  

}