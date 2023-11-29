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
public class BookCheckoutDto {
  private int checkoutNo;   // 대출 번호
  private UserDto userDto;  // 회원 번호 (int userNo)
  private BookDto bookDto;  // ISBN (int isbn)
  private int status;       // 도서 상태(0:대출신청, 1:대출중, 2:반납완료, 3:연체)
  private Date startDate;   // 대출시작날짜
  private Date dueDate;     // 대출반납예정일
  private Date endDate;     // 대출반납일
}
