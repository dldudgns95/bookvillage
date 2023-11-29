package kr.co.bookvillage.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

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
 
  
  // 회원인지 확인하기 
  // 임시 비밀번호 메일 보내기
  public ResponseEntity<Map<String, Object>> sendTmpPw(String email);
  //public void sendTmpPw(String email);
  
   // 임시 비번 메일 보내기 + 발송
  
  
  
  
  
}
