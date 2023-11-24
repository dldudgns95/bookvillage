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
public class QnaDto {
	

	private int askNo;
	private int userNo;
	private String askTitle;
	private String askContent;
	private Date askDate;
	private int askState;
	
}