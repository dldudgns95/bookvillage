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
public class InactiveUserDto {
  
  private int userNo;
  private String email;
  private String pw;
  private String name;
  private String mobile;
  private int gender;
  private int agree;
  private int state;
  private int auth;
  private Date pwModifiedDate;
  private Date joinedDate;
  private Date inactiveDate;
  

}
