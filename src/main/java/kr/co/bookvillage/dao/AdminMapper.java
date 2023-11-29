package kr.co.bookvillage.dao;

import java.util.List;
import java.util.Map;

import kr.co.bookvillage.dto.AttachFacDto;
import kr.co.bookvillage.dto.BookApplyDto;
import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.FacilityDto;
import kr.co.bookvillage.dto.UserDto;

public interface AdminMapper {
  public int insertBook(BookDto bookDto);
  public int userTotalCount();
  public List<UserDto> getUserList(Map<String, Object> map);
  public UserDto getUserDetail(int userNo);
  public int userSearchCount(Map<String, Object> map);
  public List<UserDto> getSearchUserList(Map<String, Object> map);
  public int bookTotalCount();
  public List<BookDto> getBookList(Map<String, Object> map);
  public BookDto getBookDetail(long isbn);
  public int bookSearchCount(Map<String, Object> map);
  public List<UserDto> getSearchBookList(Map<String, Object> map);
  public int addFacility(FacilityDto facilityDto);
  public int addFacImage(AttachFacDto attachFacDto);
  public List<AttachFacDto> availableFacList(String facStart);
  public List<AttachFacDto> unavailableFacList(String facStart);
  public int addFacApply(Map<String, Object> map);
  public boolean checkFacApply(Map<String, Object> map);
  public List<BookApplyDto> getBookApplyList();
}
