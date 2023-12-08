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
public class AnswerDto {
	
	private int ansNo;
	private UserDto userDto;
	private String ansConent;
	private String ansDate;
	private QnaDto qnaDto;
	
}