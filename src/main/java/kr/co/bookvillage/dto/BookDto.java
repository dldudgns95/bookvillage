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
public class BookDto {
  private String isbn;
  private String title;
  private String cover;
  private String author;
  private String publisher;
  private Date pubdate;
  private String description;
  private int status;
  private String categoryName;
  private int categoryId;
  
  //대출(updateMyCheckOut,updateCheckout)을 위한 것
  private int UserNo;
  
}