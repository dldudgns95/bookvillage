package kr.co.bookvillage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.BookSearchDto;


@Mapper
public interface BookMapper {
  public List<BookDto> getBook(BookSearchDto bookSearchDto);
  public List<BookDto> getBookDetail(String isbn);
}
