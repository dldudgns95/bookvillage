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
  private String isbn;
  private int userNo;
  private Timestamp wishDate;
  
  // 추가
  private int status; //--나중에
}
