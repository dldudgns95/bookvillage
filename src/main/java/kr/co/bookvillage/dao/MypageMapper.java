package kr.co.bookvillage.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.bookvillage.dto.BookCheckoutDto;
import kr.co.bookvillage.dto.UserDto;

@Mapper
public interface MypageMapper {
  public UserDto getMypageUser(Map<String, Object> map);                     // 사용자정보 가져오기
  public int updateUser(UserDto userDto);                                    // 사용자정보 수정
  public int updateUserPw(UserDto userDto);                                  // 비밀번호 수정
  public int getBookCheckoutCount();                                         // 대출내역 갯수
  public List<BookCheckoutDto> getBookCheckoutList(Map<String, Object> map); // 도서대출내역 가져오기
  public int updateDueDate(int checkoutNo);                                  // 대출연기신청(반납예정일 + 7)
 
}
