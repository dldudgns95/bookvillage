package kr.co.bookvillage.dao;

import java.util.List;
import java.util.Map;

import kr.co.bookvillage.dto.AttachFacDto;
import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.FacilityDto;
import kr.co.bookvillage.dto.UserDto;

public interface AdminMapper {
  public int insertBook(BookDto bookDto);
  public int userTotalCount();
  public int bookTotalCount();
  public List<UserDto> getUserList(Map<String, Object> map);
  public List<BookDto> getBookList(Map<String, Object> map);
  public int addFac(FacilityDto facilityDto);
  public int addFacImage(AttachFacDto attachFacDto);
}
