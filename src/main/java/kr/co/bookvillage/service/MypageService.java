package kr.co.bookvillage.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import kr.co.bookvillage.dto.UserDto;

public interface MypageService {
  public UserDto getMypageUser(String email);
  public ResponseEntity<Map<String, Object>> modify(HttpServletRequest request);
  public void modifyPw(HttpServletRequest request, HttpServletResponse response);
  public void loadBookCheckoutList(HttpServletRequest request, Model model);
  public int delayBookCheckout(int checkoutNo);
  public void loadReviewList(HttpServletRequest request, Model model);
  public int removeReview(String isbn, int userNo);
  public void loadWishBookList(HttpServletRequest request, Model model);
  public int cancleBookCheckout(HttpServletRequest request);
  public int removeWish(String isbn, int userNo);
  public void loadBookApplyList(HttpServletRequest request, Model model);
  public int modifyBookApply(HttpServletRequest request);
  public int deleteApply(int applyNo);
  public void loadFacApplyList(HttpServletRequest request, Model model);
  public int deleteFacApply(int facApplyNo);
  
}
