package kr.co.bookvillage.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.bookvillage.dao.UserMapper;
import kr.co.bookvillage.dto.InactiveUserDto;
import kr.co.bookvillage.dto.UserDto;
import kr.co.bookvillage.util.MyJavaMailUtils;
import kr.co.bookvillage.util.MySecurityUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
  
  private final UserMapper userMapper;
  private final MySecurityUtils mySecurityUtils;
  private final MyJavaMailUtils myJavaMailUtils;
  
      
  private final String client_id = "s69mitzAnTVJZPIV_Hkc";
  private final String client_secret = "G21YQxY5Ku";
            
  
  public void login(HttpServletRequest request, HttpServletResponse response) throws Exception {
      
    String email = request.getParameter("email");
    String pw = mySecurityUtils.getSHA256(request.getParameter("pw"));
    
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
  
  
  @Override
  public UserDto getUser(String email) {
    return userMapper.getUser(Map.of("email", email));
  }
  
  
  
  // 이메일 주소 등록되어있는지 확인
  @Transactional(readOnly = true)
  @Override
  public ResponseEntity<Map<String, Object>> checkEmail(String email) {
    
    Map<String, Object> map = Map.of("email", email);
    
    boolean enableEmail = userMapper.getUser(map) == null
                       && userMapper.getInactiveUser(map) == null;
    
    System.out.println("enableEmail" + enableEmail);
    
    return new ResponseEntity<>(Map.of("enableEmail", enableEmail), HttpStatus.OK);
    
  }
  
  @Override
  public ResponseEntity<Map<String, Object>> sendCode(String email) {
    
    // RandomString 생성(6자리, 문자 사용, 숫자 사용)
    String code = mySecurityUtils.getRandomString(6, true, true);
    
    // 메일 전송
    myJavaMailUtils.sendJavaMail(email
                               , "책빌리지 인증 코드"
                               , "<div>인증코드는 <strong>" + code + "</strong>입니다.</div>");
    
    return new ResponseEntity<>(Map.of("code", code), HttpStatus.OK);
    
  }

  @Override
  public void join(HttpServletRequest request, HttpServletResponse response) {

    String email = request.getParameter("email");
    String pw = mySecurityUtils.getSHA256(request.getParameter("pw"));
    String name = mySecurityUtils.preventXSS(request.getParameter("name"));
    String gender = request.getParameter("gender");
    String mobile = request.getParameter("mobile");
    String event = request.getParameter("event");
    
    UserDto user = UserDto.builder()
                        .email(email)
                        .pw(pw)
                        .name(name)
                        .gender(gender)
                        .mobile(mobile)
                        .agree(event.equals("on") ? 1: 0)
                        .build();
    
    int joinResult = userMapper.insertUsesr(user);
    
    try {
      
      response.setContentType("text/html; charset=UTF-8");
      PrintWriter out = response.getWriter();
      out.println("<script>");
      if(joinResult == 1) {
        request.getSession().setAttribute("user", userMapper.getUser(Map.of("email", email)));
        userMapper.insertAccess(email);
        out.println("alert('회원 가입되었습니다.')");
        out.println("location.href='" + request.getContextPath() + "/main.do'");
      } else {
        out.println("alert('회원 가입이 실패했습니다.')");
        out.println("history.go(-2)");
      }
      out.println("</script>");
      out.flush();
      out.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }


  @Override
  public String findId(String name, String mobile) {
    
    
    String findIdresult = "";
        
        try {
          
          findIdresult = userMapper.findId(name, mobile);
        } catch(Exception e) {
          
          e.printStackTrace();
        }
        
        return findIdresult ;
      }
      







}
