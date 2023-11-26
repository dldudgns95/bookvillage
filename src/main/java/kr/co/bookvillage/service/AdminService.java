package kr.co.bookvillage.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import kr.co.bookvillage.dto.UserDto;

public interface AdminService  {
  public int insertBook(HttpServletRequest request);
  public List<UserDto> getUserList();
}
