package kr.co.bookvillage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.bookvillage.dto.BookCheckoutDto;
import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.BookSearchDto;
import kr.co.bookvillage.dto.ScoreDto;


@Mapper
public interface BookMapper {
  public List<BookDto> getBook(BookSearchDto bookSearchDto);
  public int getBookCount(BookSearchDto bookSearchDto);
  public List<BookDto> getBookDetail(String isbn);
  
  //대출
  public void updateBookStatus(BookDto bookDto);
  public void insertCheckoutStatus(BookDto bookDto);
}
