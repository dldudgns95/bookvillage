package kr.co.bookvillage.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import kr.co.bookvillage.dao.BookMapper;
import kr.co.bookvillage.dao.ScoreMapper;
import kr.co.bookvillage.dao.WishMapper;
import kr.co.bookvillage.dto.BookCheckoutDto;
import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.BookSearchDto;
import kr.co.bookvillage.dto.ScoreDto;
import kr.co.bookvillage.dto.WishDto;
import kr.co.bookvillage.util.AdminFileUtils;
import kr.co.bookvillage.util.AdminPageUtils;
import kr.co.bookvillage.util.MyPageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookServiceImpl implements BookService {

  @Autowired
  private BookMapper bookMapper;
  private final ScoreMapper scoreMapper;
  private final WishMapper wishMapper;
  private final MyPageUtils myPageUtils;
  private final AdminPageUtils adminPageUtils;
  private final AdminFileUtils adminFileUtils;
  
  
  // 책 검색
  @Override
  public void searchBook(BookSearchDto bookSearchDto, HttpServletRequest request, Model model) {
    List<BookDto> bookSearchList = bookMapper.getBook(bookSearchDto);
    model.addAttribute("bookSearchList", bookSearchList);
    
    //페이징
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = bookMapper.getBookCount(bookSearchDto);
    int display = 10;
    
    adminPageUtils.setPaging(page, total, display);
    Map<String, Object> map = Map.of("begin", adminPageUtils.getBegin(), "end", adminPageUtils.getEnd());
    
    model.addAttribute("paging", adminPageUtils.getMvcPaging(request.getContextPath() + "/book/search/result"));
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("totalCount", total);
    
  }
  
  // 책 상세
  @Override
  public void getBookDetail(String isbn, Model model) {
    List<BookDto> bookDetailList = bookMapper.getBookDetail(isbn);
    model.addAttribute("bookDetailList", bookDetailList);
  }
  
  // 별점, 한줄평 등록
  @Override
  public int insertScore(ScoreDto scoreDto) {
    int addScoreResult = scoreMapper.insertScore(scoreDto); 
    return addScoreResult;
  }
  
  // 한줄평 목록 가져오기
  @Override
  public void getScore(String isbn, Model model) {
    List<ScoreDto> scoreList = scoreMapper.getScore(isbn);
    model.addAttribute("scoreList", scoreList);
  }
  
  // 한줄평 삭제 (내 것만 가능)
  @Override
  public void deleteScore(ScoreDto scoreDto) {
    scoreMapper.deleteScore(scoreDto);        
  }
  
  // 한줄평 좋아요 (남의 것만 가능) --구현안됨.. 그냥 1올라가고 끝임 두번 눌러도 다른 사람이 눌러도 올라가지 않는다. 디비써야하나
  @Override
  public void likeScore(ScoreDto scoreDto, Model model) {
    scoreDto.setRecommend(scoreDto.getRecommend()+1);
    int scoreLike = scoreDto.getRecommend();
    model.addAttribute("scoreLike", scoreLike);
  }
  
  
  // 관심도서 목록
  // 관심도서 존재 확인
  @Override
  public int wishExists(WishDto wishDto) {
    int checkWish = wishMapper.wishExists(wishDto);// 관심도서 항목에 존재하지 않으면 0, 존재하면 1이 담긴다. 
    return checkWish;
  }
  // 관심도서 등록
  @Override
  public void insertWish(WishDto wishDto) {
    wishMapper.insertWish(wishDto);
  }
  // 관심도서 삭제
  @Override
  public void deleteWish(WishDto wishDto) {
    wishMapper.deleteWish(wishDto);
  }
  
  // 대출 처리
  @Override
  public void updateCheckout(BookDto bookDto) {
    bookMapper.insertCheckoutStatus(bookDto);    
  }
  @Override
  public void updateBook(BookDto bookDto) {
    bookMapper.updateBookStatus(bookDto);
  }
  
  
}
