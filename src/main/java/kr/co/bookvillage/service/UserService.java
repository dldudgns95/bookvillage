package kr.co.bookvillage.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

public interface UserService {
  
  public void login(HttpServletRequest request, HttpServletResponse response , Model model) throws Exception;
}
