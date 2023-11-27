package kr.co.bookvillage.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import kr.co.bookvillage.dto.UserDto;

public interface AdminService  {
  public int insertBook(HttpServletRequest request);
  public void getUserList(HttpServletRequest request, Model model);
  public void getBookList(HttpServletRequest request, Model model);
}
