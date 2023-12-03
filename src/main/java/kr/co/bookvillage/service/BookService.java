package kr.co.bookvillage.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.BookSearchDto;
import kr.co.bookvillage.dto.ScoreDto;
import kr.co.bookvillage.dto.WishDto;


public interface BookService {

  // 검색
  public void searchBook(BookSearchDto bookSearchDto, HttpServletRequest request,Model model);
  
  // 상세
  public void getBookDetail(String isbn, Model model);
  
  // 별점, 한줄평
  public int insertScore(ScoreDto scoreDto);
  public void getScoreList(String isbn, Model model);
  public void deleteScore(ScoreDto scoreDto);
  public void likeScore(ScoreDto scoreDto, Model model);
  
  
  // 관심도서
  public int wishExists(WishDto wishDto);
  public void insertWish(WishDto wishDto);
  public void deleteWish(WishDto wishDto);
  
  // 대출
  public void updateCheckout(BookDto bookDto);
  public void updateBook(BookDto bookDto);

  // 카테고리 추출
  public void categoryParser (BookDto bookDto);
}
