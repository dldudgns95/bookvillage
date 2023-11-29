package kr.co.bookvillage.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.bookvillage.dto.UserDto;
import kr.co.bookvillage.service.UserService;
import lombok.RequiredArgsConstructor;

@RequestMapping(value="/user")
@RequiredArgsConstructor
@Controller
public class UserController {
  
  public final UserService  userService;
  
  @GetMapping("/login.form")
  public String loginForm(HttpServletRequest request, Model model) throws Exception {
    // referer : 이전 주소가 저장되는 요청 Header 값
    String referer = request.getHeader("referer");
    model.addAttribute("referer", referer == null ? request.getContextPath() + "/main.do" : referer);
    // 네이버로그인-1
    model.addAttribute("naverLoginURL", userService.getNaverLoginURL(request));
    return "user/login";
  }
  
  @GetMapping("/naver/getAccessToken.do")
  public String getAccessToken(HttpServletRequest resRequest) throws Exception {
    String accessToken = userService.getNaverLoginAccessToken(resRequest);
    return "redirect:/user/naver/getProfile.do?accessToken=" + accessToken;
  }
  @GetMapping("/naver/getProfile.do")
  public  String getProfile(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
    // 네이버 로그인 -3
    UserDto naverProfile = userService.getNaverProfile(request.getParameter("accessToken")); 
    // 네이버로그인 후속 작업(처음 시도 : 간편가입, 이미 가입 : 로그인)
    UserDto user = userService.getUser(naverProfile.getEmail());
    // 처음시도 간편가입 - 프로필에 있는 정보를 간편가입 페이지에 입력되어있도록 처리할거임
    if(user == null) {
      model.addAttribute("naverProfile", naverProfile);
      return "user/naver_join";
    } else {
      // naverProfile로 로그인 처리하기
      userService.naverLogin(request, response, naverProfile);
      return "redirect:/main.do";
    }
  }
  
  @PostMapping("/naver/join.do")
  public void naverJoin(HttpServletRequest request, HttpServletResponse response) {
    userService.naverJoin(request, response);
  }
  
  
  // 로그인
  @PostMapping("/login.do")
  public void login(HttpServletRequest request, HttpServletResponse response) throws Exception {
    userService.login(request, response);
  }
  
  // 로그아웃 
  @GetMapping("/logout.do")
  public void logout(HttpServletRequest request, HttpServletResponse response) {
    userService.logout(request, response);
  }
  
  // 약관 동의 페이지로 이동
  @GetMapping("/agree.form")
  public String agreeForm() {
    return "user/agree";
  }
  
 
  // 회원 가입 페이지로 이동
  @GetMapping("/join.form")
  public String joinForm(@RequestParam(value = "service", required = false, defaultValue = "off") String service
                        , @RequestParam(value = "event", required = false, defaultValue = "off") String event
                        , Model model) {
    String rtn = null;
    if(service.equals("off")) {
      rtn = "redirect:/main.do";
    } else {
      model.addAttribute("event", event);
      rtn = "user/join";
    }
    return rtn;
  }
  
  // 이메일 확인
  @GetMapping(value="/checkEmail.do", produces=MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam String email) {
    return userService.checkEmail(email);
  }
  
  // 인증 메일 발송
  @GetMapping(value = "/sendCode.do", produces =MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> sendCode(@RequestParam String email){
    return userService.sendCode(email);
  }
  
  // 일반 가입
  @PostMapping("/join.do")
  public void join(HttpServletRequest request, HttpServletResponse response) {
    userService.join(request, response);
  }
  
  // 아이디 찾기 이동
  @GetMapping("/findId.form")
  public String findIdForm() {
    return "user/findId";
  }
  
  // 아이디(메일) 찾기
  @ResponseBody
  @PostMapping(value="/findId.do", produces="application/json")
  public Map<String, Object> findId(@RequestParam(value = "name") String name
                     , @RequestParam(value = "mobile",  required = true) String mobile) {
    UserDto user = userService.findId(name, mobile);
    return Map.of("email", user == null ? "" : user.getEmail());  // {"email": "aaaaa@naver.com"}
  }
  
  // 회원 이메일 확인
  @GetMapping(value="/checkPwEmail.do", produces=MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> checkPwEmail(@RequestParam String email) {
    return userService.checkEmail(email);
  }
  
  
  // 임시 비번 발송 및 업데이트
  @PostMapping(value = "/sendTmpPw.do", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> sendTmpPw(@RequestBody Map<String, String> requestBody) {
      String email = requestBody.get("email");
      return userService.sendTmpPw(email);
  }



  

  
  
}
  