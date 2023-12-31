package kr.co.bookvillage.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.bookvillage.dto.BookCheckoutDto;
import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.BookSearchDto;
import kr.co.bookvillage.dto.ScoreDto;
import kr.co.bookvillage.dto.WishDto;
import kr.co.bookvillage.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/book")
@RequiredArgsConstructor
@Controller
@Slf4j
public class BookController {
  
  private final BookService bookService;

  /*페이지 이동*/
  // 자료검색 카테고리 클릭 후 '검색 페이지'로 이동
  @GetMapping("/search.do")
  public String search(Model model) {
    bookService.getNewBook(model);
    bookService.getRecoBook(model);
    return "book/search";
  }
  @GetMapping("/total.do")
  public String total(Model model) {
    return "book/total";
  }
  @GetMapping("/recommand.do")
  public String recommand(Model model) {
    return "book/recommand";
  }
  // 검색 버튼 클릭 후 '검색 결과 리스트 페이지'로 이동
  @GetMapping("/search/result")
  public String result(BookSearchDto bookSearchDto, HttpServletRequest request, Model model) {
    bookService.searchBook(bookSearchDto, request, model);
    String searchText = bookSearchDto.getSt();
    model.addAttribute("searchText", searchText);
    return "book/result";
  }
  // 상세 버튼 클릭 후 '도서 상세 페이지'로 이동
  @GetMapping("/search/detail")
  public String detail(@RequestParam("isbn") String isbn, ScoreDto scoreDto, WishDto wishDto, Model model) {
    bookService.getBookDetail(isbn, model);
    bookService.getScoreList(scoreDto.getIsbn(), model);
    bookService.bestReview(isbn, model);
    bookService.getStarAvg(isbn, model);
    int checkWish = bookService.checkWish(wishDto);
    model.addAttribute("checkWish", checkWish);
    //한줄평 중복 체크 위해
    int checkScore = bookService.checkScore(scoreDto);
    model.addAttribute("checkScore",checkScore);
    // 별점 그래프 위해
    List<ScoreDto> cntStar = bookService.cntStar(isbn);
    log.info("cntStar: {}", cntStar);
    model.addAttribute("cntStar",cntStar);
    //대출
    int checkBookCkCnt = bookService.checkBookCOStatus(wishDto.getUserNo()); //userNo 가져와야하는데 있는것 중 해결하려고 wishDto 고른거
    model.addAttribute("checkBookCkCnt", checkBookCkCnt);
    Integer checkUserStatus = bookService.checkUserStatus(wishDto.getUserNo()); //userNo 가져와야하는데 있는것 중 해결하려고 wishDto 고른거
    model.addAttribute("checkUserStatus", checkUserStatus);
    System.out.println("여기"+checkUserStatus);
    return "book/detail";
  }
  
  
  
  /*별점, 한줄평*/
  //평가 저장
  @PostMapping("/addScore.do")
  public String addScore(@RequestBody ScoreDto scoreDto) {
    bookService.insertScore(scoreDto);
    return "redirect:/book/search/detail?isbn=" + scoreDto.getIsbn();
  }
  // 한줄평 삭제
  @GetMapping("/deleteScore.do")
  public String deleteScore(@RequestParam("isbn") String isbn,
                            @RequestParam("userNo") int userNo,
                            ScoreDto scoreDto, 
                            Model model) {
    bookService.deleteScore(scoreDto);
    return "redirect:/book/search/detail?isbn=" + scoreDto.getIsbn();
  }
  // 한줄평 좋아요
  @GetMapping("/likeScore.do")
  public String likeScore(@RequestParam("isbn") String isbn,
                          @RequestParam("userNo") int userNo,
                          ScoreDto scoreDto, 
                          Model model) {
    bookService.likeScore(scoreDto);
    return "redirect:/book/search/detail?isbn=" + scoreDto.getIsbn();
  }
  
  /*관심도서*/
  //관심도서 확인
  @PostMapping("/checkWish.do")
  @ResponseBody
  public String checkWish(@RequestBody WishDto wishDto) {
    int checkWish = bookService.checkWish(wishDto);
    return "{\"checkWish\":" + checkWish + "}";
  }
  
  //관심도서 추가
  @PostMapping("/addWish.do")
  public String addWish(@RequestBody WishDto wishDto, Model model) {
    bookService.insertWish(wishDto);
    int wishStatus = wishDto.getStatus();
    System.out.println("여기추가"+wishStatus);
    model.addAttribute("wishStatus",wishStatus);
    return "redirect:/book/search/detail?isbn=" + wishDto.getIsbn();
  }
  //관심도서 삭제
  @PostMapping("/deleteWish.do")
  public String deleteWish(@RequestBody WishDto wishDto, Model model) {
    bookService.deleteWish(wishDto);
    int wishStatus = wishDto.getStatus();
    System.out.println("여기삭제"+wishStatus);
    model.addAttribute("wishStatus",wishStatus);
    return "redirect:/book/search/detail?isbn=" + wishDto.getIsbn();
  }
  
  /*대출*/
  //대출처리(book)
  @PostMapping("/updateBook.do")
  public String updateBook(@RequestBody BookDto bookDto, Model model) {
    bookService.updateBook(bookDto);
    return "redirect:/book/search/detail?isbn=" + bookDto.getIsbn();
  }
  //대출처리(checkout)
  @PostMapping("/updateCheckout.do")
  public String updateCheckout(@RequestBody BookDto bookDto) {
    bookService.updateCheckout(bookDto);
    return "redirect:/book/search/detail?isbn=" + bookDto.getIsbn();
  }

  
  
}
