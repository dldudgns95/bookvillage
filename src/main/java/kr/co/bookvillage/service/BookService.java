package kr.co.bookvillage.service;

import org.springframework.ui.Model;

import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.BookSearchDto;
import kr.co.bookvillage.dto.ScoreDto;
import kr.co.bookvillage.dto.WishDto;


public interface BookService {

  public void searchBook(BookSearchDto bookSearchDto, Model model);
  public void getBookDetail(String isbn, Model model);
  public int insertScore(ScoreDto scoreDto);
  public void getScore(String isbn, Model model);
  public void deleteScore(ScoreDto scoreDto);
  public void insertWish(WishDto wishDto);
  public void updateCheckout(BookDto bookDto);
}
