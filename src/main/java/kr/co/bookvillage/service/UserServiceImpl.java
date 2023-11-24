package kr.co.bookvillage.service;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

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
  
  @Override
  public void login(HttpServletRequest request, HttpServletResponse response , Model model) throws Exception {
    String email = request.getParameter("userNo");
    String pw = mySecurityUtils.getSHA256(request.getParameter("pw"));
    
    Map<String, Object> map = Map.of("email", email
                                   , "pw", pw);
    
    HttpSession session = request.getSession();
    
    InactiveUserDto inacticeUser = userMapper.getInactiveUser(map);
    if(inacticeUser != null) {
      session.setAttribute("inacticeUser", inacticeUser);
      response.sendRedirect(request.getContextPath() + "/user/actice.from");
    }
    // 정상 로그인
    UserDto user = userMapper.getUser(map);
    
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
 
  

}
