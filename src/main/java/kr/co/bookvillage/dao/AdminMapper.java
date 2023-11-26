package kr.co.bookvillage.dao;

import java.util.List;

import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.UserDto;

public interface AdminMapper {
  public int insertBook(BookDto bookDto);
  public List<UserDto> getUserList();
}
