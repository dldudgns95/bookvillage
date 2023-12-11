package kr.co.bookvillage.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;

import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.FaqDto;
import kr.co.bookvillage.dto.NoticeDto;
import kr.co.bookvillage.dto.UserDto;

public interface UserService {
  
  public void login(HttpServletRequest request, HttpServletResponse response) throws Exception;
  public void logout(HttpServletRequest request, HttpServletResponse response);
  
  public String getNaverLoginURL(HttpServletRequest request) throws Exception;
  public String getNaverLoginAccessToken(HttpServletRequest request) throws Exception;
  public UserDto getNaverProfile(String accessToken) throws Exception;
  public void naverJoin(HttpServletRequest request, HttpServletResponse response);
  public void naverLogin(HttpServletRequest request, HttpServletResponse response, UserDto naverProfile) throws Exception;
  public UserDto getUser(String email);
  public ResponseEntity<Map<String, Object>> checkEmail(String email);
  public ResponseEntity<Map<String, Object>> sendCode(String email);
  public void join(HttpServletRequest request, HttpServletResponse response);
  // 이메일 찾기(아이디)
  public UserDto findId(String name, String mobile);
  // 임시 비밀번호 메일 보내기
  public ResponseEntity<Map<String, Object>> sendTmpCode(String email);
  public ResponseEntity<Map<String, Object>> updateTmpPw(String email);
  
  public void kakaoLogin(HttpServletRequest request, HttpServletResponse response, UserDto kakaoProfile) throws Exception;
  public void kakaoJoin(HttpServletRequest request, HttpServletResponse response) throws Exception;
  public String getKakaoLoginURL(HttpServletRequest request) throws Exception;
  public String getKakaoLoginAccessToken(HttpServletRequest request) throws Exception ;
  public UserDto getKakaoProfile(String accessToken) throws Exception;
  
  public int autoUpdatePw90(HttpServletRequest request);
 
  public List<FaqDto> getFaqList();
  public List<NoticeDto> getNoticeList();
  public List<BookDto> getBookList();
  // 휴면회원.. 
  public void inactiveUserBatch(); 
  public void active(HttpSession session, HttpServletRequest request, HttpServletResponse response);
  
}






