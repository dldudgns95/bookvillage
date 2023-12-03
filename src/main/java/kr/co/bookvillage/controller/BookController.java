package kr.co.bookvillage.controller;

import javax.servlet.http.HttpServletRequest;

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

@RequestMapping("/book")
@RequiredArgsConstructor
@Controller
public class BookController {
  
  private final BookService bookService;

  /*페이지 이동*/
  // 자료검색 카테고리 클릭 후 '검색 페이지'로 이동
  @GetMapping("/search.do")
  public String search(Model model) {
    bookService.getNewBook(model);
    return "book/search";
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
  public String detail(@RequestParam("isbn") String isbn, ScoreDto scoreDto, Model model) {
    bookService.getBookDetail(isbn, model);
    bookService.getScoreList(scoreDto.getIsbn(), model);
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
    bookService.likeScore(scoreDto, model);
    return "redirect:/book/search/detail?isbn=" + scoreDto.getIsbn();
  }
  
  /*관심도서*/
  //관심도서 확인
  @PostMapping("/checkWish.do")
  @ResponseBody
  public String checkWish(@RequestBody WishDto wishDto) {
    int checkWish = bookService.wishExists(wishDto);
    return "{\"checkWish\":" + checkWish + "}";
  }
  
  //관심도서 추가
  @PostMapping("/addWish.do")
  public String addWish(@RequestBody WishDto wishDto) {
    bookService.insertWish(wishDto);
    return "redirect:/book/search/detail?isbn=" + wishDto.getIsbn();
  }
  //관심도서 삭제
  @PostMapping("/deleteWish.do")
  public String deleteWish(@RequestBody WishDto wishDto) {
    bookService.deleteWish(wishDto);
    return "redirect:/book/search/detail?isbn=" + wishDto.getIsbn();
  }
  
  /*대출*/
  //대출처리(book)
  @PostMapping("/updateBook.do")
  public String updateBook(@RequestBody BookDto bookDto) {
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
