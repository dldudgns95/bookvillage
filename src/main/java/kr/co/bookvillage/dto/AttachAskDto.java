package kr.co.bookvillage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AttachAskDto {
	
	private int AskAttachNo;
	private QnaDto qnaDto;
	private String askPath;
	private String askOriginalFilename;
	private String askFilesystemName;
	private String askDownloadCount;
	private int askHasThumbnail;

	
}