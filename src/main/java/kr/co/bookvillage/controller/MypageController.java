package kr.co.bookvillage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/mypage")
@Controller
public class MypageController {
  
  // 서비스 객체선언
  
  // 마이페이지 메인목록
  @GetMapping(value="/list.do")
  public String list() {
    return "mypage/list";
  }
  
  // 회원정보 수정페이지 이동
  @GetMapping(value="/edit.form")
  public String edit(int userNo) {
    return "mypage/edit";
  }
  
  
  
  
}
