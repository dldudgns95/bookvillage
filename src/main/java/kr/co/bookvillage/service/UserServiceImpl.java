package kr.co.bookvillage.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import kr.co.bookvillage.dao.AdminMapper;
import kr.co.bookvillage.dao.UserMapper;
import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.FaqDto;
import kr.co.bookvillage.dto.InactiveUserDto;
import kr.co.bookvillage.dto.NoticeDto;
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
  
      
  private final String client_id = "akfMPV_DLx7u40rpRi7W";
  private final String client_secret = "5J5oz6ohRI";
  
  private final String ka_Client_id = "9ffad004d68c4c286ec206cd6660ae00";
            
  
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
  
    if (user != null) {
      // 로그인 성공 처리
      request.getSession().setAttribute("user", user);
      userMapper.insertAccess(email);

      // 비밀번호 변경 90일 지나면 알림      
      boolean userPW90 = userMapper.changePw90(email) == null;

        if (!userPW90 ) {
        response.setContentType("text/html; charset=UTF-8");
          PrintWriter outt = response.getWriter();
          outt.println("<script>");
          outt.println("alert('마지막 비밀번호 변경일로부터 90일이 경과했습니다. 비밀번호를 변경해주세요.')");
          outt.println("location.href='" +  "/mypage/modifyPw.form'");
          outt.println("</script>");
          outt.flush();
          outt.close();
      } else {
          // 90일 이전인 경우에 실행할거 있으면 적기  
      }
      response.sendRedirect(request.getParameter("referer"));
  } else {
      // 로그인 실패 처리
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
      response.sendRedirect( "/main.do");
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
  //네이버 로그인1
  @Override
    public String getNaverLoginURL(HttpServletRequest request) throws Exception {
    String apiURL = "https://nid.naver.com/oauth2.0/authorize";
    String response_type = "code";
    String redirect_uri = URLEncoder.encode("http://localhost:8080" + "/user/naver/getAccessToken.do", "UTF-8");
    String state = new BigInteger(130, new SecureRandom()).toString();

    StringBuilder sb = new StringBuilder();
    sb.append(apiURL);
    sb.append("?response_type=").append(response_type);
    sb.append("&client_id=").append(client_id);
    sb.append("&redirect_uri=").append(redirect_uri);
    sb.append("&state=").append(state);
    
    return sb.toString();
    }
  
  //네이버 로그인2
  @Override
  public String getNaverLoginAccessToken(HttpServletRequest request) throws Exception {

    String code = request.getParameter("code");
    String state = request.getParameter("state");
    
    String apiURL = "https://nid.naver.com/oauth2.0/token";
    String grant_type = "authorization_code"; 
    
    StringBuilder sb = new StringBuilder();
    sb.append(apiURL);
    sb.append("?grant_type=").append(grant_type);
    sb.append("&client_id=").append(client_id);
    sb.append("&client_secret=").append(client_secret);
    sb.append("&code=").append(code);
    sb.append("&state=").append(state);
    
    // 요청
    URL url = new URL(sb.toString());
    HttpURLConnection con = (HttpURLConnection)url.openConnection();
    con.setRequestMethod("GET");  // 반드시 대문자로 작성
    
    // 응답
    BufferedReader reader = null;
    int responseCode = con.getResponseCode();
    if(responseCode == 200) {
      reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
    } else {
      reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
    }
    
    String line = null;
    StringBuilder responseBody = new StringBuilder();
    while ((line = reader.readLine()) != null) {
      responseBody.append(line);
    }
    
    JSONObject obj = new JSONObject(responseBody.toString());
    return obj.getString("access_token");
  }
  
  // 네이버 로그인 3
  @Override
  public UserDto getNaverProfile(String accessToken) throws Exception {
  
    String apiURL = "https://openapi.naver.com/v1/nid/me";
    URL url = new URL(apiURL);
    HttpURLConnection con = (HttpURLConnection)url.openConnection();
    con.setRequestMethod("GET");
    con.setRequestProperty("Authorization", "Bearer " + accessToken);
    
    // 응답
    BufferedReader reader = null;
    int responseCode = con.getResponseCode();
    if(responseCode == 200) {
      reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
    } else {
      reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
    }
    
    String line = null;
    StringBuilder responseBody = new StringBuilder();
    while ((line = reader.readLine()) != null) {
      responseBody.append(line);
    }
    
    // 응답 결과(프로필을 JSON으로 응답) -> UserDto 객체
    JSONObject obj = new JSONObject(responseBody.toString());
    JSONObject response = obj.getJSONObject("response");
    UserDto user = UserDto.builder()
                    .email(response.getString("email"))
                    .name(response.getString("name"))
                    .gender(response.getString("gender"))
                    .mobile(response.getString("mobile"))
                    .build();
    
    return user;
  }
  
  @Override
  public void naverJoin(HttpServletRequest request, HttpServletResponse response) {
    
    String email = request.getParameter("email");
    String name = request.getParameter("name");
    String gender = request.getParameter("gender");
    String mobile = request.getParameter("mobile");
    String event = request.getParameter("event");
    
    UserDto user = UserDto.builder()
                        .email(email)
                        .name(name)
                        .gender(gender)
                        .mobile(mobile.replace("-", ""))
                        .agree(event != null ? 1 : 0)
                        .build();
    
    int naverJoinResult = userMapper.insertNaverUser(user);

    try {
      
      response.setContentType("text/html; charset=UTF-8");
      PrintWriter out = response.getWriter();
      out.println("<script>");
      if(naverJoinResult == 1 ) {
        request.getSession().setAttribute("usre", userMapper.getUser(Map.of("email", email)));
        userMapper.insertAccess(email);
        out.println("alert('네이버 간편 가입이 완료 되었습니다.')");
      } else {
        out.println("alert('네이버 간편 가입이 실패했습니다.')");
      }
      out.println("location.href='" + "/main.do'");
      out.println("</script>");
      out.flush();
      out.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  
  @Override
  public void naverLogin(HttpServletRequest request, HttpServletResponse response, UserDto naverProfile) throws Exception {
    
    String email = naverProfile.getEmail();
    UserDto user = userMapper.getUser(Map.of("email", email));
    
    if(user != null) {
      request.getSession().setAttribute("user", user);
      userMapper.insertAccess(email);
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

 
  // 아이디 찾기
  @Override
  public UserDto findId(String name, String mobile)  {
    
    UserDto emailResult = null;
        
        try {
          emailResult = userMapper.findId(name, mobile);
          
        } catch(Exception e) {
          e.printStackTrace();
        }
        return emailResult ;
      }
  
  
  
      
  // 인증코드 메일 발송
  @Override
  public ResponseEntity<Map<String, Object>> sendTmpCode(String email) {

    String pwCode = mySecurityUtils.getRandomString(6, true, true);

   myJavaMailUtils.sendJavaMail(email
       , "책빌리지 인증코드입니다."
       , "<div>인증코는 <Strong>" + pwCode + "</strong> 입니다. <br> 로그인 후에 비밀번호를 변경을 해주세요</div>");
  
  
   return new ResponseEntity<>(Map.of("pwCode", pwCode), HttpStatus.OK);

   
  }

  // 임시 비밀번호 발송 및 업데이트
  @Override
  public ResponseEntity<Map<String, Object>> updateTmpPw(String email) {
    
    String pwCode = mySecurityUtils.getRandomString(10, true, true);

    myJavaMailUtils.sendJavaMail(email
        , "책빌리지 임시비밀번호입니다."
        , "<div>임시비밀번호는 <Strong>" + pwCode + "</strong> 입니다. <br> 로그인 후에 비밀번호를 변경을 해주세요</div>");
   
     String hashedPwCode = mySecurityUtils.getSHA256(pwCode);
     
     userMapper.updatetmpPw(Map.of("email", email,"pwCode", hashedPwCode));
    
   return new ResponseEntity<>(Map.of("email", email, "pwCode", hashedPwCode), HttpStatus.OK);
  

  }
  
  // 카카오 가입
  @Override
  public void kakaoJoin(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String mobile = request.getParameter("mobile");
    String email = request.getParameter("email");
  //  String pw = request.getParameter("pw");
    String name = request.getParameter("name");
    String gender = request.getParameter("gender");
    
    UserDto user = UserDto.builder()    
                          .mobile(mobile)
                          .email(email)
  //                       .pw(pw)
                          .name(name)
                          .gender(gender)
                          .build();
    
    int kakaoJoinResult = userMapper.kakaoJoin(user);
    
  try {
      
      response.setContentType("text/html; charset=UTF-8");
      PrintWriter out = response.getWriter();
      out.println("<script>");
      if(kakaoJoinResult == 1) {
        request.getSession().setAttribute("user", userMapper.getUser(Map.of("email", email)));
        userMapper.insertAccess(email);
        out.println("alert('카카오 간편가입이 완료되었습니다.')");
      } else {
        out.println("alert('카카오 간편가입이 실패했습니다.')");
      }
      out.println("location.href='" + request.getContextPath() + "/main.do'");
      out.println("</script>");
      out.flush();
      out.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
    
  // 카카오 로그인1.. 
  @Override
  public String getKakaoLoginURL(HttpServletRequest request) throws Exception {
      String apiURL = "https://kauth.kakao.com/oauth/authorize";
      String response_type = "code";
      String redirect_uri = URLEncoder.encode("http://localhost:8080/" + "user/kakao/getAccessToken.do", "UTF-8");
      String state = new BigInteger(130, new SecureRandom()).toString();

      StringBuilder sb = new StringBuilder();
      sb.append(apiURL);
      sb.append("?response_type=").append(response_type);
      sb.append("&client_id=").append(ka_Client_id);
      sb.append("&redirect_uri=").append(redirect_uri);
      sb.append("&state=").append(state);

      return sb.toString();
  }

  // 카카오 로그인2..
  @Override
  public String getKakaoLoginAccessToken(HttpServletRequest request) throws Exception {
    
    String apiURL ="https://kauth.kakao.com/oauth/token";
    String grant_type = "authorization_code";
    
    String code =request.getParameter("code");
    String state =request.getParameter("state");
    
    StringBuilder sb = new StringBuilder();
    sb.append(apiURL);
    sb.append("?grant_type=").append(grant_type);
    sb.append("&client_id=").append(ka_Client_id);
    sb.append("&code=").append(code);
    sb.append("&state=").append(state);
    
    // 요청
    URL url = new URL(sb.toString());
    HttpURLConnection con = (HttpURLConnection)url.openConnection();
    con.setRequestMethod("GET"); 
    
    // 응답
    BufferedReader reader = null;
    int responseCode = con.getResponseCode();
    if(responseCode == 200) {
      reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
    } else {
      reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
    }
    
    String line = null;
    StringBuilder responseBody = new StringBuilder();
    while ((line = reader.readLine()) != null) {
      responseBody.append(line);
    }
    
    JSONObject obj = new JSONObject(responseBody.toString());
    return obj.getString("access_token");
  }
  
  // 카카오 3..
  //https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info
 @Override
 public UserDto getKakaoProfile(String accessToken) throws Exception {
 
   String apiURL = "https://kapi.kakao.com/v2/user/me";
   URL url = new URL(apiURL);
   HttpURLConnection con = (HttpURLConnection)url.openConnection();
   con.setRequestMethod("GET");
   con.setRequestProperty("Authorization", "Bearer " + accessToken);
   
   // 응답
   BufferedReader reader = null;
   int responseCode = con.getResponseCode();
   if(responseCode == 200) {
     reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
   } else {
     reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
   }
   String line = null;
   StringBuilder responseBody = new StringBuilder();
   while ((line = reader.readLine()) != null) {
     responseBody.append(line);
   }
   
   JSONObject obj = new JSONObject(responseBody.toString());
   
   JSONObject kakaoAccount = obj.getJSONObject("kakao_account");
   
   UserDto user = UserDto.builder()    
       .email(kakaoAccount.getString("email"))
       .name(kakaoAccount.getString("name"))
       .gender(kakaoAccount.getString("gender"))
       .mobile(kakaoAccount.getString("phone_number"))
                         .build();
   
   return user;
 }
  // 카카오 로그인
  @Override
  public void kakaoLogin(HttpServletRequest request, HttpServletResponse response, UserDto kakaoProfile)
      throws Exception {
    String email = kakaoProfile.getEmail();
    UserDto user = userMapper.getUser(Map.of("email", email));
    
    if(user != null) {
      request.getSession() .setAttribute("user", user);
      userMapper.insertAccess(email);
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
  public List<FaqDto> getFaqList() {
    return userMapper.getFaqList();
  }
   
   @Override
  public List<NoticeDto> getNoticeList() {
    return userMapper.getNoticeList();
  }

  @Override
  public int autoUpdatePw90(HttpServletRequest request) {
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    UserDto user = UserDto.builder()
                        .userNo(userNo)
                        .build();
     int autoUpdatePw90Result = userMapper.autoupdatetmpPw(user);  
     return autoUpdatePw90Result;
  }
  
  @Override
  public List<BookDto> getBookList() {
    return userMapper.getBookList();
  }
  
  // 휴면...
  @Override
  public void inactiveUserBatch() {
    userMapper.insertInactiveUser();
    userMapper.deleteUserForInactive();
  }
  
  
  @Override
  public void active(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
    InactiveUserDto  inactiveUser = (InactiveUserDto)session.getAttribute("inactiveUser");
    String email = inactiveUser.getEmail();

    System.out.println("삽입" + inactiveUser);
    
    int insertActiveUserResult = userMapper.insertActiveUser(email);
    int deleteInactiveUserResult = userMapper.deleteInactiveUser(email);
    
    
    
    try {
      response.setContentType("text/html; charset=UTF-8");
      PrintWriter out = response.getWriter();
      out.println("<script>");
      
      if(insertActiveUserResult == 1 && deleteInactiveUserResult == 1) {
        out.println("alert('휴면계정이 복구되었습니다. 계정 활성화를 위해 곧바로 로그인해주세요.')");
        out.println("location.href='" + "/main.do'");
      } else {
        out.println("alert('휴면계정 복구가 실패했습니다. 다시 시도해주세요.')");
        out.println("history.back()");
      }
      out.println("</script>");
      out.flush();
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    
    
    
  }

  
  // 시설
  @Override
  public void getFacList(Model model) {
    model.addAttribute("facList", userMapper.getFacList());
  }
  
  
  
}
  
  
  
  
  
  
  
  
  
  
  
  
  


