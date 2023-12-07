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
  
  @Override
  public void getFacList(Model model) {
   model.addAttribute("facList", facMapper.getFacList());
		
	}
  
}
