package kr.co.bookvillage.dao;

import kr.co.bookvillage.dto.BookDto;

public interface AdminMapper {
  public int insertBook(BookDto bookDto);
}
