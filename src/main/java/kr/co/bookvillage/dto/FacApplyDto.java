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
	private UserDto userDto;  // int userNo
	private FacilityDto facilityDto; // int facNo
	private Date facRegDate;
	private Date facStart;
	private int facStatus;
}
