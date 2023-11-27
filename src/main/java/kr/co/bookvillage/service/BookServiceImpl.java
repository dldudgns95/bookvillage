package kr.co.bookvillage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import kr.co.bookvillage.dao.BookMapper;
import kr.co.bookvillage.dao.ScoreMapper;
import kr.co.bookvillage.dao.WishMapper;
import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.BookSearchDto;
import kr.co.bookvillage.dto.ScoreDto;
import kr.co.bookvillage.dto.WishDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

  @Autowired
  private BookMapper bookMapper;
  private final ScoreMapper scoreMapper;
  private final WishMapper wishMapper;
  
  @Override
  public void searchBook(BookSearchDto bookSearchDto, Model model) {
    List<BookDto> bookSearchList = bookMapper.getBook(bookSearchDto);
    model.addAttribute("bookSearchList", bookSearchList);
  }
  
  @Override
  public void getBookDetail(String isbn, Model model) {
    List<BookDto> bookDetailList = bookMapper.getBookDetail(isbn);
    model.addAttribute("bookDetailList", bookDetailList);
  }
  
  @Override
  public int insertScore(ScoreDto scoreDto) {
    int addScoreResult = scoreMapper.insertScore(scoreDto); 
    return addScoreResult;
  }
  
  @Override
  public void getScore(String isbn, Model model) {
    List<ScoreDto> scoreList = scoreMapper.getScore(isbn);
    model.addAttribute("scoreList", scoreList);
  }
  
  @Override
  public void deleteScore(ScoreDto scoreDto) {
    scoreMapper.deleteScore(scoreDto);        
  }
  
  @Override
  public void insertWish(WishDto wishDto) {
    wishMapper.insertWish(wishDto);
  }
  
  @Override
  public void updateCheckout(BookDto bookDto) {
    bookMapper.updateStatus(bookDto);
  }
}
