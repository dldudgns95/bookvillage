package kr.co.bookvillage.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.bookvillage.dto.FaqDto;
import kr.co.bookvillage.dto.NoticeDto;
import kr.co.bookvillage.service.UserService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/main")

@Controller
@RequiredArgsConstructor
public class MainController {
  
  private final UserService userService;
  
  
  @ResponseBody
  @GetMapping("/faqList.do")
  public List<FaqDto> faqList() {
      return userService.getFaqList();
  }
  
  @ResponseBody
  @GetMapping("/noticeList.do")
  public List<NoticeDto> NoticeList(){
    return userService.getNoticeList();
  }
  
  
  
}
