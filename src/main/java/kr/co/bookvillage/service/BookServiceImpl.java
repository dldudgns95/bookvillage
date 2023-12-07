package kr.co.bookvillage.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

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
import kr.co.bookvillage.util.BookPageUtils;
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
  private final BookPageUtils bookPageUtils;
  
  // 신간 도서
  @Override
  @Transactional(readOnly=true)
  public void getNewBook(Model model) {
    List<BookDto> newBookList = bookMapper.getNewBook();
    model.addAttribute("newBookList",newBookList);
  }
  // 추천 도서
  @Override
  @Transactional(readOnly=true)
  public void getRecoBook(Model model) {
    List<BookDto> recoBookList = bookMapper.getRecoBook();
    model.addAttribute("recoBookList",recoBookList);
  }
  
  // 책 검색 & 정렬
  @Override
  @Transactional(readOnly=true)
  public void searchBook(BookSearchDto bookSearchDto, HttpServletRequest request, Model model) {
    
    //페이징
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1")); //page 값이 전달되지 않으면 1로 본다.
    int display = 10;
    int total = bookMapper.getBookCount(bookSearchDto);
    
    bookPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", bookPageUtils.getBegin(), "end", bookPageUtils.getEnd(),"ss", bookSearchDto.getSs(),"st", bookSearchDto.getSt(), "sortType", bookSearchDto.getSortType());
    
    List<BookDto> bookSearchList = bookMapper.getBook(map);
    model.addAttribute("bookSearchList", bookSearchList);
    
    model.addAttribute("paging", bookPageUtils.getMvcPaging(request.getContextPath() + "/book/search/result", "userNo="+bookSearchDto.getUserNo()+"&ss="+bookSearchDto.getSs()+"&st="+bookSearchDto.getSt()+"&sortType="+bookSearchDto.getSortType()));
    model.addAttribute("totalCount", total);
    
    
  }
  
  // 책 상세
  @Override
  @Transactional(readOnly=true)
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
  @Transactional(readOnly=true)
  public void getScoreList(String isbn, Model model) {
    List<ScoreDto> scoreList = scoreMapper.getScoreList(isbn);
    model.addAttribute("scoreList", scoreList);
  }
  
  // 그래프 그리기 위해서 책마다 별점 분포 체크 
  @Override
  @Transactional(readOnly=true)
  public List<ScoreDto> cntStar(String isbn) {
    return scoreMapper.cntStar(isbn);
  }
  
  // 한줄평 이전 등록 여부 체크
  @Override
  @Transactional(readOnly=true)
  public int checkScore(ScoreDto scoreDto) {
    return scoreMapper.checkScore(scoreDto);
  }
  
  // 베스트 리뷰
  @Override
  @Transactional(readOnly=true)
  public void bestReview(String isbn, Model model) {
    List<ScoreDto> bestReview = scoreMapper.bestReview(isbn);
    model.addAttribute("bestReview", bestReview);    
  }
    
  // 평균 별점
  @Override
  public void getStarAvg(String isbn, Model model) {
    Double starAvg = scoreMapper.getStarAvg(isbn);
    model.addAttribute("starAvg", (starAvg != null) ? starAvg : 0.0);   
  }
  
  // 한줄평 삭제 (내 것만 가능)
  @Override
  public void deleteScore(ScoreDto scoreDto) {
    scoreMapper.deleteScore(scoreDto);        
  }
  
  // 한줄평 좋아요 (남의 것만 가능)
  @Override
  public void likeScore(ScoreDto scoreDto) {
    scoreMapper.likeScore(scoreDto);
  }
  
  
  // 관심도서 목록
  // 관심도서 존재 확인
  @Override
  public int checkWish(WishDto wishDto) {
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
  @Transactional(readOnly=true)
  public int checkBookCOStatus(int userNo) {
    return bookMapper.checkBookCOStatus(userNo);
  }
  @Override
  public int checkUserStatus(int userNo) {
    return bookMapper.checkUserStatus(userNo);
  }
  @Override
  public void updateCheckout(BookDto bookDto) {
    bookMapper.insertCheckoutStatus(bookDto);    
  }
  @Override
  public void updateBook(BookDto bookDto) {
    bookMapper.updateBookStatus(bookDto);
  }
  
  
  // 카테고리 추출 --구현x
  @Override
  public void categoryParser(BookDto bookDto) {
    
    String inputString = bookDto.getCategoryName();
    
    // 정규표현식 패턴
    String regex = ">([^>]+)>";

    // 패턴과 입력 문자열을 사용하여 Matcher 생성
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(inputString);

    List<String> categories = new ArrayList<>();
    
    // 매칭된 부분 찾기
    while (matcher.find()) {
      // 첫 번째 그룹의 값 리스트에 추가
      String result = matcher.group(1);
      categories.add(result);
    }
    
    for (String category : categories) {
      System.out.println(category);
    }
  }
  
}
