package kr.co.bookvillage.dao;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.BookSearchDto;


@Mapper
public interface BookMapper {
  //신간도서 가져오기
  public List<BookDto> getNewBook();
  //추천도서 가져오기
  public List<BookDto> getRecoBook();
  
  //도서 검색 정렬 결과 가져오기
  public List<BookDto> getBook(Map<String, Object> map);
  //페이징 total
  public int getBookCount(BookSearchDto bookSearchDto);
  //마이페이지 정보 표시
  public List<BookDto> getBookDetail(String isbn);

  
  //대출
  public void updateBookStatus(BookDto bookDto);
  public int checkBookCOStatus(int userNo);
  public void insertCheckoutStatus(BookDto bookDto);
  
}
