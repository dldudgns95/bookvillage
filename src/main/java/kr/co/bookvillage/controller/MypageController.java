package kr.co.bookvillage.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.bookvillage.dto.UserDto;
import kr.co.bookvillage.service.MypageService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/mypage")
@RequiredArgsConstructor
@Controller
public class MypageController {
  
  // 서비스 객체선언
  private final MypageService mypageService;
  
  // 마이페이지 메인목록
  @GetMapping(value="/list.do")
  public String list() {
    return "mypage/list";
  }
  
  // 회원정보 수정페이지 이동
  @GetMapping("/edit.form")
  public String edit() {
    return "mypage/edit";
  }
  
  // 회원정보 수정
  @PostMapping(value="/modify.do", produces=MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> modify(HttpServletRequest request) {
    return mypageService.modify(request);
  }
  
  // 비밀번호 수정페이지 이동
  @GetMapping("/modifyPw.form")
  public String modifyPwForm() {
    return "mypage/pw";
  }
  
  // 비밀번호 수정
  @PostMapping("/modifyPw.do")
  public void modifyPw(HttpServletRequest request, HttpServletResponse response) {
    mypageService.modifyPw(request, response);
  }
  
  // 도서대출 목록 페이지 이동
  @GetMapping("/booklist.do")
  public String booklist(HttpServletRequest request, Model model) {    
    mypageService.loadBookCheckoutList(request, model);
    return "mypage/booklist";
  }

  // 한줄평 작성 목록 페이지 이동
  @GetMapping("/review.do")
  public String review() {
    return "mypage/review";
  }
  
  
  
  
  
}
