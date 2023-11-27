package kr.co.bookvillage.service;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import kr.co.bookvillage.dao.MypageMapper;
import kr.co.bookvillage.dto.UserDto;
import kr.co.bookvillage.util.MySecurityUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MypageServiceImpl implements MypageService {
  
  private final MypageMapper mypageMapper;
  private final MySecurityUtils mySecurityUtils;
  
  // 회원정보 가져오기
  @Override
  public UserDto getUser(String email) {
    return mypageMapper.getUser(Map.of("email", email));
  }
  
  // 회원정보 수정
  @Override
  public ResponseEntity<Map<String, Object>> modify(HttpServletRequest request) {
    String name = mySecurityUtils.preventXSS(request.getParameter("name"));
    String gender = request.getParameter("gender");
    String mobile = request.getParameter("mobile");
    String event = request.getParameter("event");
    int agree = event.equals("on") ? 1 : 0;
    int userNo  = Integer.parseInt(request.getParameter("userNo"));
    
    UserDto user = UserDto.builder()
        .name(name)
        .gender(gender)
        .mobile(mobile)
        .agree(agree)
        .userNo(userNo)
        .build();
    
    int modifyResult = mypageMapper.updateUser(user);
    
    if(modifyResult == 1) {
      HttpSession session = request.getSession();
      UserDto sessionUser = (UserDto)session.getAttribute("user");
      sessionUser.setName(name);
      sessionUser.setGender(gender);
      sessionUser.setMobile(mobile);
      sessionUser.setAgree(agree);
    }
    
    return new ResponseEntity<>(Map.of("modifyResult", modifyResult), HttpStatus.OK);
  }
  
  @Override
  public void modifyPw(HttpServletRequest request, HttpServletResponse response) {
    
    String pw = mySecurityUtils.getSHA256(request.getParameter("pw"));
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    
    UserDto user = UserDto.builder()
        .pw(pw)
        .userNo(userNo)
        .build();
    
    int modifyPwResult = mypageMapper.updateUserPw(user);
    
    try {
      
      response.setContentType("text/html; charset=UTF-8");
      PrintWriter out = response.getWriter();
      out.println("<script>");
      if(modifyPwResult == 1) {
        HttpSession session = request.getSession();
        UserDto sessionUser = (UserDto)session.getAttribute("user");
        sessionUser.setPw(pw);
        out.println("alert('비밀번호가 수정되었습니다.')");
        out.println("location.href='" + request.getContextPath() + "/mypage/edit.form'");
      } else {
        out.println("alert('비밀번호가 수정되지 않았습니다.')");
        out.println("history.back()");
      }
      out.println("</script>");
      out.flush();
      out.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }

}
