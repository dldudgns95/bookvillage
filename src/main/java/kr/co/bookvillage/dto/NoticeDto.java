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
public class NoticeDto {
	private int ntNo; 			//공지번호
	private String ntTitle; 	//공지제목
    private String ntContent;	//공지내용
    private int userNo;
    private String ntDate;
    private int ntAttachCount;
    private UserDto userDto;
}
