package kr.co.bookvillage.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.bookvillage.dto.AttachFacDto;
import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.FacilityDto;
import kr.co.bookvillage.dto.FaqDto;
import kr.co.bookvillage.dto.NoticeDto;
import kr.co.bookvillage.service.AdminService;
import kr.co.bookvillage.service.BookService;
import kr.co.bookvillage.service.MainService;
import kr.co.bookvillage.service.UserService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/main")
@Controller
@RequiredArgsConstructor
public class MainController {
  
  private final UserService userService;
  private final BookService bookService;
  private final AdminService adminService;
  private final MainService mainService;
  
  
  @ResponseBody
  @GetMapping("/faqList.do")
  public List<FaqDto> faqList() {
      return userService.getFaqList();
  }
  
  @ResponseBody
  @GetMapping("/noticeList.do")
  public List<NoticeDto> noticeList(){
    return userService.getNoticeList();
  }
  
  
  @GetMapping("/mainSearch.form")
  public String mainSearch() {
    return "main/mainSearch";
  }
  
  // 책 이미지..
  @ResponseBody
  @GetMapping(value = "/bookImageList.do", produces="application/json")
  public Map<String, Object> bookImageList(Model model){
    List<BookDto> book = userService.getBookList();
   model.addAttribute("book", book);
    
    return Map.of("book", book == null ? "" : book);
  }
  
  @ResponseBody
  @GetMapping(value = "/reviewTop3.do", produces="application/json")
  public Map<String, Object> reviewTop3(Model model){
    List<BookDto> review3 = mainService.getReviewTop3();
    model.addAttribute("reviw3", review3);
    
    return Map.of("review3", review3 == null ? "" : review3);
  }
  
  @ResponseBody
  @GetMapping(value = "/mainFac.do", produces = "application/json")
  public Map<String, Object> mainFac(Model model){
    List<AttachFacDto> facList =  mainService.getFacList();
    System.out.println("결과" + facList);
    return Map.of("facList", facList == null ? "" : facList);
    
  }
  
}
