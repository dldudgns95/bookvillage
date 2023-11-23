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

public class SupportDto {
	private int ntNo;
	private String ntTitle;
	private String ntContent;
	private Date ntDate;
}
