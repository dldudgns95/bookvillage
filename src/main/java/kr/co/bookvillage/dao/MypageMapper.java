package kr.co.bookvillage.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.bookvillage.dto.BookApplyDto;
import kr.co.bookvillage.dto.BookCheckoutDto;
import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.ScoreDto;
import kr.co.bookvillage.dto.UserDto;
import kr.co.bookvillage.dto.WishDto;

@Mapper
public interface MypageMapper {
  public UserDto getMypageUser(Map<String, Object> map);                         // 사용자정보 가져오기
  public int updateUser(UserDto userDto);                                        // 사용자정보 수정
  public int updateUserPw(UserDto userDto);                                      // 비밀번호 수정
  public int getUserBookCheckoutCount(int userNo);                               // 대출내역 갯수
  public List<BookCheckoutDto> getUserBookCheckoutList(Map<String, Object> map); // 도서대출내역 가져오기
  public int updateDueDate(int checkoutNo);                                      // 대출연기신청(반납예정일 + 7)
  public int getReviewCount(int userNo);                                         // 한줄평갯수
  public List<ScoreDto> getReviewList(Map<String, Object> map);                  // 한줄평리스트
  public int getWishCount(int userNo);                                           // 관심도서갯수
  public List<WishDto> getWishBookList(Map<String, Object> map);                 // 관심도서리스트
  public int cancleCheckout(int checkoutNo);                                     // 도서대출신청 취소(대출테이블에서삭제)
  public int updateBookStatus(BookDto book);                                     // 도서대출신청 취소(책의대출상태를 대출가능으로변경)
  public int minusBookCount(int userNo);                                         // 도서대출신청 취소(유저의대출권수변경)
  public int deleteWish(Map<String, Object> map);                                // 관심도서 삭제
  public int getApplyBookCount(int userNo);                                      // 희망도서신청갯수
  public List<BookApplyDto> getApplyBookList(Map<String, Object> map);           // 희망도서신청리스트
  public int updateBookApply(BookApplyDto applyBook);                            // 희망도서신청내역수정
  public int deleteApply(int applyNo);                                           // 희망도서신청취소
}
