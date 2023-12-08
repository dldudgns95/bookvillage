package kr.co.bookvillage.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import kr.co.bookvillage.dto.UserDto;

/**
 * CheckAdminLoginInterceptor
 * 관리자 페이지를 이용할 때 관리자로 로그인 되어 있는지 점검하는 인터셉트
 */

@Component
public class CheckAdminLoginInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    
    HttpSession session = request.getSession();
    UserDto user = (UserDto)session.getAttribute("user");
    
    if(user == null || user.getAuth() != 9) {
      response.setContentType("text/html; charset=UTF-8");
      PrintWriter out = response.getWriter();
      out.println("<script>");
      out.println("alert('관리자 페이지를 이용하기 위해서는 관리자 계정으로 로그인 하셔야합니다.')");
      out.println("location.href = '/main.do'");
      out.println("</script>");
      out.flush();
      out.close();
      
      return false;  // 가로챈 컨트롤러 요청이 동작하지 않는다.
      
    }
    return true;     // 가로챈 컨트롤러 요청이 동작한다.
    
  }
  
}
