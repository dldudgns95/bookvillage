package kr.co.bookvillage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BookApplyDto {
  private int applyNo;
  private UserDto userDto; // int userNo;
  private String bookName;
  private String author;
  private String publisher;
  private String wish;
  private int status;
}