package kr.co.bookvillage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MypageController {
  
  // 서비스 객체선언
  
  
  @GetMapping(value="/mypage/list.do")
  public String list() {
    return "mypage/list";
  }
  
  
}
