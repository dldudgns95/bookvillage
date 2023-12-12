package kr.co.bookvillage.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AskDto {
  
  private int askNo;
  private String askTitle;
  private String askContent;
  private UserDto userDto; // int userNo;
  private int hit;
  private Date createdDate;
  private Date modifiedDate;
  private int status;   // 0 : 답변전, 1: 답변 완료
  
 // private int userNo;
 // private String name;
}
