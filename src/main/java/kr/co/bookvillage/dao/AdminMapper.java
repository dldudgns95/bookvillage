package kr.co.bookvillage.dao;

import java.util.List;
import java.util.Map;

import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.UserDto;

public interface AdminMapper {
  public int insertBook(BookDto bookDto);
  public int userTotalCount();
  public List<UserDto> getUserList(Map<String, Object> map);
}
