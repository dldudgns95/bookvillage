package kr.co.bookvillage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AttachNtDto {
	private int attachNtNo;
	private int ntNo;
	private String ntPath;
	private String ntOriginalFilename;
	private String ntFilesystemName;
	private int ntDownloadCount;
	private int ntHasThumbnail;
	
}
