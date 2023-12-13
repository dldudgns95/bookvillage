package kr.co.bookvillage.service;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import kr.co.bookvillage.dao.MypageMapper;
import kr.co.bookvillage.dto.BookApplyDto;
import kr.co.bookvillage.dto.BookCheckoutDto;
import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.FacApplyDto;
import kr.co.bookvillage.dto.ScoreDto;
import kr.co.bookvillage.dto.UserDto;
import kr.co.bookvillage.dto.WishDto;
import kr.co.bookvillage.util.AdminPageUtils;
import kr.co.bookvillage.util.MySecurityUtils;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class MypageServiceImpl implements MypageService {
  
  private final MypageMapper mypageMapper;
  private final MySecurityUtils mySecurityUtils;
  private final AdminPageUtils adminPageUtils;
  
  // 회원정보 가져오기
  @Override
  public UserDto getMypageUser(String email) {
    return mypageMapper.getMypageUser(Map.of("email", email));
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
    
    // 휴대전화번호 중복 체크(중복데이터가 있으면 1)
    int mobileCount = mypageMapper.getMobileCheck(user.getMobile());
    if (mobileCount > 0) {
        // 중복된 휴대전화번호가 있을 경우 처리
        return new ResponseEntity<>(Map.of("modifyResult", -1), HttpStatus.OK);
    }
    
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
  
  // 비밀번호 수정
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
  
  @Transactional(readOnly=true)
  @Override
  public void loadBookCheckoutList(HttpServletRequest request, Model model) {    
    
    HttpSession session = request.getSession();
    int userNo = ((UserDto)session.getAttribute("user")).getUserNo();
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = mypageMapper.getUserBookCheckoutCount(userNo);    
    int display = 10;
    
    adminPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", adminPageUtils.getBegin()
                                   , "end", adminPageUtils.getEnd()
                                   , "userNo", userNo);
    
    List<BookCheckoutDto> bookList = mypageMapper.getUserBookCheckoutList(map);
    
    model.addAttribute("bookList", bookList);
    model.addAttribute("paging", adminPageUtils.getMvcPaging(request.getContextPath() + "mypage/booklist.do"));
    model.addAttribute("beginNo", total - (page -1) * display);
    model.addAttribute("bookcount", mypageMapper.getUserBookCount(userNo));

  }
  
  @Override
  public int delayBookCheckout(int checkoutNo) {
    return mypageMapper.updateDueDate(checkoutNo);
  }
  
  @Transactional(readOnly=true)
  @Override
  public void loadReviewList(HttpServletRequest request, Model model) {
    
    HttpSession session = request.getSession();
    int userNo = ((UserDto)session.getAttribute("user")).getUserNo();
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = mypageMapper.getReviewCount(userNo);
    int display = 10;
    
    adminPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", adminPageUtils.getBegin()
                                    , "end", adminPageUtils.getEnd()
                                    , "userNo", userNo);
    
    List<ScoreDto> reviewList = mypageMapper.getReviewList(map);
    
    model.addAttribute("reviewList", reviewList);
    model.addAttribute("paging", adminPageUtils.getMvcPaging(request.getContextPath() + "mypage/review.do"));
    model.addAttribute("beginNo", total - (page -1) * display);
    
  }
  
  @Override
  public int removeReview(String isbn, int userNo) {
    Map<String, Object> map = new HashMap<>();
    map.put("isbn", isbn);
    map.put("userNo", userNo);
    
    return mypageMapper.deleteReview(map);
  }
  
  @Transactional(readOnly=true)
  @Override
  public void loadWishBookList(HttpServletRequest request, Model model) {
    
    HttpSession session = request.getSession();
    int userNo = ((UserDto)session.getAttribute("user")).getUserNo();
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = mypageMapper.getWishCount(userNo);
    int display = 10;
    
    adminPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", adminPageUtils.getBegin()
                                    , "end", adminPageUtils.getEnd()
                                    , "userNo", userNo);
    
    List<WishDto> wishList = mypageMapper.getWishBookList(map);
    
    model.addAttribute("wishList", wishList);
    model.addAttribute("paging", adminPageUtils.getMvcPaging(request.getContextPath() + "mypage/review.do"));
    model.addAttribute("beginNo", total - (page -1) * display);
 
  }
  
  @Transactional(readOnly=true)
  @Override
  public int cancleBookCheckout(HttpServletRequest request) {
    int checkoutNo = Integer.parseInt(request.getParameter("checkoutNo"));
    HttpSession session = request.getSession();
    int userNo = ((UserDto)session.getAttribute("user")).getUserNo();
    String isbn = request.getParameter("isbn");
    BookDto book = BookDto.builder()
                     .isbn(isbn)
                     .build();
    
    
    int deleteResult = mypageMapper.cancleCheckout(checkoutNo);
    if(deleteResult == 1) {
      mypageMapper.updateBookStatus(book);
      mypageMapper.minusBookCount(userNo);
    }
    return deleteResult;
  }
  
  @Override
  public int removeWish(String isbn, int userNo) {

    Map<String, Object> map = new HashMap<>();
    map.put("isbn", isbn);
    map.put("userNo", userNo);
    
    return mypageMapper.deleteWish(map);
  }
  
  @Override
  public void loadBookApplyList(HttpServletRequest request, Model model) {
    
    HttpSession session = request.getSession();
    int userNo = ((UserDto)session.getAttribute("user")).getUserNo();
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = mypageMapper.getApplyBookCount(userNo);
    int display = 10;
    
    adminPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", adminPageUtils.getBegin()
                                    , "end", adminPageUtils.getEnd()
                                    , "userNo", userNo);
    
    List<BookApplyDto> applyBookList = mypageMapper.getApplyBookList(map);
    
    model.addAttribute("applyBookList", applyBookList);
    model.addAttribute("paging", adminPageUtils.getMvcPaging(request.getContextPath() + "mypage/applyBook.do"));
    model.addAttribute("beginNo", total - (page -1) * display);
  }
  
  @Override
  public int modifyBookApply(HttpServletRequest request) {
    
    String bookName = request.getParameter("bookName");
    String author = request.getParameter("author");
    String publisher = request.getParameter("publisher");
    String wish = request.getParameter("wish");
    int applyNo = Integer.parseInt(request.getParameter("applyNo"));
    
    BookApplyDto applyBook = BookApplyDto.builder()
                                .bookName(bookName)
                                .author(author)
                                .publisher(publisher)
                                .wish(wish)
                                .applyNo(applyNo)
                                .build();
    
    int modifyResult = mypageMapper.updateBookApply(applyBook);
    
    return modifyResult;
  }
  
  @Override
  public int deleteApply(int applyNo) {
    return mypageMapper.deleteApply(applyNo);
  }
  
  @Override
  public void loadFacApplyList(HttpServletRequest request, Model model) {
    
    HttpSession session = request.getSession();
    int userNo = ((UserDto)session.getAttribute("user")).getUserNo();
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = mypageMapper.getFacApplyCount(userNo);
    int display = 10;
    
    adminPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", adminPageUtils.getBegin()
                                    , "end", adminPageUtils.getEnd()
                                    , "userNo", userNo);
    
    List<FacApplyDto> facApplyList = mypageMapper.getFacApplyList(map);
    
    model.addAttribute("facApplyList", facApplyList);
    model.addAttribute("paging", adminPageUtils.getMvcPaging(request.getContextPath() + "mypage/facApply.do"));
    model.addAttribute("beginNo", total - (page -1) * display);

  }
  
  @Override
  public int deleteFacApply(int facApplyNo) {
    return mypageMapper.deleteFacApply(facApplyNo);
  }
  

}
