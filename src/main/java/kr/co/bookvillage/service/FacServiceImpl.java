package kr.co.bookvillage.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.bookvillage.dao.BookApplyMapper;
import kr.co.bookvillage.dao.FacMapper;
import kr.co.bookvillage.dto.AttachFacDto;
import kr.co.bookvillage.dto.BookApplyDto;
import kr.co.bookvillage.dto.FacilityDto;
import kr.co.bookvillage.dto.UserDto;
import kr.co.bookvillage.util.AdminFileUtils;
import kr.co.bookvillage.util.AdminPageUtils;
import kr.co.bookvillage.util.MyPageUtils;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;

@Transactional
@Service
@RequiredArgsConstructor
public class FacServiceImpl implements FacService {
  
  private final FacMapper facMapper;
  private final BookApplyMapper bookapplyMapper;
  private final MyPageUtils myPageUtils;
  private final AdminPageUtils adminPageUtils;
  private final AdminFileUtils adminFileUtils;


  @Override
  public void addFacility(MultipartHttpServletRequest multiRequest) throws Exception {
    
    String facName = multiRequest.getParameter("facName");
    String facContent = multiRequest.getParameter("facContent");
    int checkStatus = Integer.parseInt(multiRequest.getParameter("checkStatus"));
    System.out.println("checkStatus : " + checkStatus);
    FacilityDto facility = FacilityDto.builder()
                                      .facName(facName)
                                      .facContent(facContent)
                                      .build();
    int addResult = facMapper.addFacility(facility);
    
    List<MultipartFile> files = multiRequest.getFiles("files");
    
    int attachCount;
    if(files.get(0).getSize() == 0) {
      attachCount = 1;
    } else {
      attachCount = 0;
    }
    
    for(MultipartFile multipartFile : files) {
      if(multipartFile != null && !multipartFile.isEmpty()) {
        String path = "";
        if(checkStatus == 0) {
          path = adminFileUtils.getFacPath();
        } else if(checkStatus == 1) {
          path = adminFileUtils.getFacMacImagePath();
        }
        File dir = new File(path);
        if(!dir.exists()) {
          dir.mkdirs();
        }
        
        String facOriginalFilename = multipartFile.getOriginalFilename();
        String facFilesystemName = adminFileUtils.getFilesystemName(facOriginalFilename);
        
        System.out.println("path : " + path);
        System.out.println("facOriginalFilename : " +facOriginalFilename);
        System.out.println("facFilesystemName : " +facFilesystemName);
        
        String url = path + "/" + facFilesystemName;
        
        Path paths = Paths.get(url).toAbsolutePath();
        
        File file = new File(dir, facFilesystemName);
        
        System.out.println("file : " + file);
        
          //multipartFile.transferTo(file); // 이거 안됨
          multipartFile.transferTo(paths.toFile());
        
      String contentType = Files.probeContentType(paths); // 이미지의 Content-Type : image/jpeg, image/png 등 image로 시작한다.
      int hasThumbnail = (contentType != null && contentType.startsWith("image")) ? 1 : 0;
      
      // 썸네일이 있으면 원본파일 썸네일로 만들기
      if(hasThumbnail == 1) {
        File thumbnail = new File(dir, "s_" + facFilesystemName); // small 이미지를 의미하는 s_를 덧붙임
        Thumbnails.of(file)
                  .size(100, 100)       // 가로 100px, 세로 100px
                  .toFile(thumbnail);
      }
      
      AttachFacDto attachFac = AttachFacDto.builder()
                                 .facPath(path)
                                 .facOriginalFilename(facOriginalFilename)
                                 .facFilesystemName(facFilesystemName)
                                 .facHasThumbnail(0)
                                 .build();
      System.out.println("attachFac : " + attachFac);
      attachCount += facMapper.addFacImage(attachFac);
        
      }
    }
  }
  
  @Override
  public Map<String, Object> getFacTotalList(HttpServletRequest request) {
    
    String facStart = request.getParameter("facStart");
    facStart = facStart.replaceAll("-", "");
    facStart = facStart.substring(2);
    
    return Map.of("availableFacList", facMapper.availableFacList(facStart), "unavailableFacList", facMapper.unavailableFacList(facStart));
  }
  
  @Override
  public int addFacApply(HttpServletRequest request) {
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    int facNo = Integer.parseInt(request.getParameter("facNo"));
    Date facStart = Date.valueOf(request.getParameter("facStart"));
    
    int addResult = facMapper.addFacApply(Map.of("userNo", userNo
                                                 , "facNo", facNo
                                                 , "facStart", facStart));
    return addResult;
  }
  
  @Override
  public boolean checkFacApply(HttpServletRequest request) {
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    String facStart = request.getParameter("facStart");
    facStart = facStart.replaceAll("-", "");
    facStart = facStart.substring(2);
    boolean check = facMapper.checkFacApply(Map.of("userNo", userNo, "facStart", facStart));
    return check;
  }

  @Override
  public void getFacApplyList(HttpServletRequest request, Model model) {
    model.addAttribute("facApplyList", facMapper.getFacApplyList());
  }
  @Override
	public int addbook(HttpServletRequest request) {
	  // BLOG_T에 추가할 데이터
	  String bookName = request.getParameter("bookName");
	    String author = request.getParameter("author");
	    String publisher = request.getParameter("publisher");
	    String wish = request.getParameter("wish");
	    int userNo = Integer.parseInt(request.getParameter("userNo"));
	    
	    BookApplyDto book = BookApplyDto.builder()
								        .bookName(bookName)
								        .author(author)
								        .publisher(publisher)
								        .wish(wish)
								        .userDto(UserDto.builder()
								            .userNo(userNo)
								            .build())
								        .build();
	    int addResult = bookapplyMapper.insertBook(book);
	    return addResult;
	}
  
  
  
}
