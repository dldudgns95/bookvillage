package kr.co.bookvillage.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.bookvillage.dao.QnaMapper;
import kr.co.bookvillage.dto.AttachAskDto;
import kr.co.bookvillage.dto.QnaDto;
import kr.co.bookvillage.dto.UserDto;
import kr.co.bookvillage.util.MyFileUtils;
import kr.co.bookvillage.util.MyPageUtils;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;

@Transactional
@RequiredArgsConstructor
@Service
public class QnaServiceImpl implements QnaService {
	private QnaMapper qnaMapper;
	private final MyFileUtils myFileUtils;
	private final MyPageUtils myPageUtils;	
	
	@Override
	public void addAsk(MultipartHttpServletRequest multipartRequest) throws Exception {

	    String askTitle = multipartRequest.getParameter("askTitle");
	    String askContent = multipartRequest.getParameter("askContent");
	    
	    int userNo = Integer.parseInt(multipartRequest.getParameter("userNo"));
	    
	    QnaDto qna = QnaDto.builder()
	                        .askTitle(askTitle)
	                        .askContent(askContent)
	                        .userDto(UserDto.builder()
	                                  .userNo(userNo)
	                                  .build())
	                        .build();
	    
	    int addResult = qnaMapper.insertAsk(qna);
	    
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
	        
	        String path = myFileUtils.getAskPath();
	        File dir = new File(path);
	        if(!dir.exists()) {
	          dir.mkdirs();
	        }
	        
	        String askOriginalFilename = multipartFile.getOriginalFilename();
	        String askFilesystemName = myFileUtils.getFilesystemName(askOriginalFilename);
	        
	        System.out.println("path:" + path);
	        System.out.println("askOriginalFilename:" + askOriginalFilename);
	        System.out.println("askFilesystemName:" + askFilesystemName);
	       
	        String url = path + "/" + askFilesystemName;
	        
	        Path paths = Paths.get(url).toAbsolutePath();
	        
	        File file = new File(dir, askFilesystemName);
	        
	        System.out.println("file :" + file);
	        	multipartFile.transferTo(paths.toFile());
	        
	        
	        String contentType = Files.probeContentType(paths);  // 이미지의 Content-Type은 image/jpeg, image/png 등 image로 시작한다.
	        int askHasThumbnail = (contentType != null && contentType.startsWith("image")) ? 1 : 0;
	        
	        if(askHasThumbnail == 1) {
	          File thumbnail = new File(dir, "s_" + askFilesystemName);  // small 이미지를 의미하는 s_을 덧붙임
	          Thumbnails.of(file)
	                    .size(100, 100)      // 가로 100px, 세로 100px
	                    .toFile(thumbnail);
	        }
	        
	        AttachAskDto attachAsk = AttachAskDto.builder()
	                            .askPath(path)
	                            .askOriginalFilename(askOriginalFilename)
	                            .askFilesystemName(askFilesystemName)
	                            .askHasThumbnail(askHasThumbnail)
	                            .build();
	        System.out.println("attachAsk: " + attachAsk);
	        attachCount += qnaMapper.insertAttach(attachAsk);
	        
	      }  // if
	      
	    }  // for
	}

	@Transactional(readOnly=true)
	@Override
	public Map<String, Object> getQnaList(HttpServletRequest request) {
	    
	    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
	    int page = Integer.parseInt(opt.orElse("1"));
	    int total = qnaMapper.getAskCount();
	    int display = 9;
	    
	    myPageUtils.setPaging(page, total, display);
	    
	    Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
	                                   , "end", myPageUtils.getEnd());
	    
	    List<QnaDto> askList = qnaMapper.getAskList(map);
	    
	    return Map.of("askList",askList
	                , "totalPage", myPageUtils.getTotalPage());
	    
	  }
}
