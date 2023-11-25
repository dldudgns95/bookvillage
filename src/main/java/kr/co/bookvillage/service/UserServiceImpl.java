package kr.co.bookvillage.service;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import kr.co.bookvillage.dao.UserMapper;
import kr.co.bookvillage.dto.InactiveUserDto;
import kr.co.bookvillage.dto.UserDto;
import kr.co.bookvillage.util.MySecurityUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
  
  public final UserMapper userMapper;
  public final MySecurityUtils mySecurityUtils;
  
public void login(HttpServletRequest request, HttpServletResponse response) throws Exception {
    
  String email = request.getParameter("email");
  String pw = request.getParameter("pw");
  
  Map<String, Object> map = Map.of("email", email
                                 , "pw", pw);

  HttpSession session = request.getSession();
  
  // 휴면 계정인지 확인하기
  InactiveUserDto inactiveUser = userMapper.getInactiveUser(map);
  if(inactiveUser != null) {
    session.setAttribute("inactiveUser", inactiveUser);
    response.sendRedirect(request.getContextPath() + "/user/active.form");
  }
  
  // 정상적인 로그인 처리하기
  UserDto user = userMapper.getUser(map);
  System.out.println(user);
  System.out.println("Query: " + userMapper.getUser(map));
  System.out.println("Parameters: " + map);

  
  if(user != null) {
    request.getSession().setAttribute("user", user);
    userMapper.insertAccess(email);
    response.sendRedirect(request.getParameter("referer"));
  } else {
    response.setContentType("text/html; charset=UTF-8");
    PrintWriter out = response.getWriter();
    out.println("<script>");
    out.println("alert('일치하는 회원 정보가 없습니다.')");
    out.println("location.href='" + request.getContextPath() + "/main.do'");
    out.println("</script>");
    out.flush();
    out.close();
  }
    
  }
  
@Override
public void logout(HttpServletRequest request, HttpServletResponse response) {
  
  HttpSession session = request.getSession();
  
  session.invalidate();
  
  try {
    response.sendRedirect(request.getContextPath() + "/main.do");
  } catch (Exception e) {
    e.printStackTrace();
  }
  
}


}
