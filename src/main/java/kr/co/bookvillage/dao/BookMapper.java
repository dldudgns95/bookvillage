package kr.co.bookvillage.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.BookSearchDto;


@Mapper
public interface BookMapper {
  public List<BookDto> getBook(Map<String, Object> map);
  //페이징 total
  public int getBookCount(BookSearchDto bookSearchDto);
  //마이페이지 정보 표시
  public List<BookDto> getBookDetail(String isbn);
  
  //대출
  public void updateBookStatus(BookDto bookDto);
  public void insertCheckoutStatus(BookDto bookDto);
}
