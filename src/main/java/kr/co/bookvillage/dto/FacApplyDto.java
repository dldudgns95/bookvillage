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
public class FacApplyDto {
	private int FacApplyNo;
	private int userNo;
	private int facNo;
	private Date facRegDate;
	private Date facStart;
	private int facStatus;
}
