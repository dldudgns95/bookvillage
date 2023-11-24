package kr.co.bookvillage.service;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.bookvillage.dao.NoticeMapper;
import kr.co.bookvillage.dto.AttachNtDto;
import kr.co.bookvillage.dto.NoticeDto;
import kr.co.bookvillage.util.MyFileUtils;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;

@Transactional
@RequiredArgsConstructor
@Service
public class NoticeServiceImpl implements NoticeService {
	
	private final NoticeMapper noticeMapper;
	private final MyFileUtils myFileUtils;
	
	@Override
	public boolean addNotice(MultipartHttpServletRequest multipartRequest) throws Exception {
		String ntTitle = multipartRequest.getParameter("ntTitle");
	    String ntContent = multipartRequest.getParameter("ntContent");
	    NoticeDto notice = NoticeDto.builder()
	                        .ntTitle(ntTitle)
	                        .ntContent(ntContent)
	                        .build();
	    
	    int noticeCount = noticeMapper.insertNotice(notice);
	    
	    List<MultipartFile> files = multipartRequest.getFiles("files");
	    
	    // 첨부 없을 때 : [MultipartFile[field="files", filename=, contentType=application/octet-stream, size=0]]
	    // 첨부 1개     : [MultipartFile[field="files", filename="animal1.jpg", contentType=image/jpeg, size=123456]]
	    
	    int attachCount;
	    if(files.get(0).getSize() == 0) {
	      attachCount = 1;
	    } else {
	      attachCount = 0;
	    }
	    
	    for(MultipartFile multipartFile : files) {
	      
	      if(multipartFile != null && !multipartFile.isEmpty()) {
	        
	        String ntPath = myFileUtils.getUploadPath();
	        File dir = new File(ntPath);
	        if(!dir.exists()) {
	          dir.mkdirs();
	        }
	        
	        String ntOriginalFilename = multipartFile.getOriginalFilename();
	        String ntFilesystemName = myFileUtils.getFilesystemName(ntOriginalFilename);
	        File file = new File(dir, ntFilesystemName);
	        
	        multipartFile.transferTo(file);
	        
	        String contentType = Files.probeContentType(file.toPath());  // 이미지의 Content-Type은 image/jpeg, image/png 등 image로 시작한다.
	        int ntHasThumbnail = (contentType != null && contentType.startsWith("image")) ? 1 : 0;
	        
	        if(ntHasThumbnail == 1) {
	          File thumbnail = new File(dir, "s_" + ntFilesystemName);  // small 이미지를 의미하는 s_을 덧붙임
	          Thumbnails.of(file)
	                    .size(100, 100)      // 가로 100px, 세로 100px
	                    .toFile(thumbnail);
	        }
	        
	        AttachNtDto attach = AttachNtDto.builder()
	                            .ntPath(ntPath)
	                            .ntOriginalFilename(ntOriginalFilename)
	                            .ntFilesystemName(ntFilesystemName)
	                            .ntHasThumbnail(ntHasThumbnail)
	                            .ntNo(notice.getNtNo())
	                            .build();
	        
	        attachCount += noticeMapper.insertAttach(attach);
	        
	      }  // if
	      
	    }  // for
	    
	    return (noticeCount == 1) && (files.size() == attachCount);
	}
}
