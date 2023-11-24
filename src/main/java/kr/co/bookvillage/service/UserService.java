package kr.co.bookvillage.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

public interface UserService {
  
  public void login(HttpServletRequest request, Model model);

}
