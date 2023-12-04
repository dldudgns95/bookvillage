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
    String requestUrl = null;
    if(referer != null) {
      requestUrl = referer.substring(referer.lastIndexOf("/") + 1);
      switch(requestUrl) {
      case "user/login.form":
      case "":
        referer = "/main.do";
        break;
      }
    }
    model.addAttribute("referer", referer);
    // 네이버, 카카오-1
    model.addAttribute("naverLoginURL", userService.getNaverLoginURL(request));
    model.addAttribute("kakaoLoginURL", userService.getKakaoLoginURL(request) );

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
  
  @GetMapping("/kakao/getAccessToken.do")
  public String KakaotAccessToken( HttpServletRequest request ) throws Exception {
      String accessToken = userService.getKakaoLoginAccessToken(request);
      return "redirect:/user/kakao/getProfile.do?accessToken=" + accessToken;
  }

  @GetMapping("/kakao/getProfile.do")
  public  String getKakaoProfile(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
    UserDto kakaoProfile = userService.getKakaoProfile(request.getParameter("accessToken")); 
    UserDto user = userService.getUser(kakaoProfile.getEmail());

    if(user == null) {
      model.addAttribute("kakaoProfile", kakaoProfile);
      return "user/kakao_join";
    } else {
      userService.kakaoLogin(request, response, kakaoProfile);
      return "redirect:/main.do";
    }
  }
  
  @PostMapping("/kakao/join.do")
  public void kakaoJoin(HttpServletRequest request, HttpServletResponse response) {
    userService.naverJoin(request, response);
  }
  
  
  // 로그인
  @PostMapping("/login.do")
  public void login(HttpSession session , UserDto dto, HttpServletRequest request, HttpServletResponse response) throws Exception {
    //원래 이거 한줄
    userService.login(request, response);
  }
  
  // 로그아웃 
  @GetMapping("/logout.do")
  public void logout(HttpServletRequest request, HttpServletResponse response) {
    userService.logout(request, response);
  }
  

  // 약관 동의 페이지로 이동
  @GetMapping("/joinChose.form")
  public String joinChoseForm(HttpServletRequest request, Model model) throws Exception {
    model.addAttribute("naverLoginURL", userService.getNaverLoginURL(request));
    model.addAttribute("kakaoLoginURL", userService.getKakaoLoginURL(request) );

    return "user/join-chose";
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
  public String findIdForm(HttpServletRequest request, Model model) throws Exception {
    model.addAttribute("naverLoginURL", userService.getNaverLoginURL(request));

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
  
  // 비번 인증코드 발송
  @PostMapping(value = "/sendTmpCodes.do", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> sendTmpCodes(@RequestBody Map<String, String> requestBody) {
      String email = requestBody.get("email");
      return userService.sendTmpCode(email);
  }
  // 임시 비번 발송 및 업데이트
  @PostMapping(value = "/sendTmpPw.do", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> sendTmpPw(@RequestBody Map<String, String> requestBody) {
      String email = requestBody.get("email");
      return userService.updateTmpPw(email);
  }

  // 90일 경과 후 비밀번호 업데이트 
  @GetMapping("/autoUpdatePw.do")
  public String autoUpdatePw(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    int autoUpdatePw90Result = userService.autoUpdatePw90(request); 
    redirectAttributes.addFlashAttribute("autoUpdatePw90Result", autoUpdatePw90Result);
    return "redirect:/main.do";
    
  }


//  @GetMapping("/books")
//  public String getBooks(Model model) {
//      List<BookDto> newBooks = bookService.getNewBooks();
//      model.addAttribute("newBooks", newBooks);
//      return "books"; // books.html을 응답으로 사용
//  }
  
  
  

  
}
  