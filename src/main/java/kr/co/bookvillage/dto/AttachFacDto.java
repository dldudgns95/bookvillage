package kr.co.bookvillage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AttachFacDto {
	private int facAttachNo;
	private FacilityDto facilityDto; // int facNo;
	private String facPath;
	private String facOriginalFilename;
	private String facFilesystemName;
	private int facHasThumbnail;
	
	
}
