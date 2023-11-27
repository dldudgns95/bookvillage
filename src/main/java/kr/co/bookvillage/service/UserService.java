package kr.co.bookvillage.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

public interface UserService {
  
  public void login(HttpServletRequest request, HttpServletResponse response) throws Exception;
  public void logout(HttpServletRequest request, HttpServletResponse response);

  public ResponseEntity<Map<String, Object>> checkEmail(String email);
  
  
}
