package kr.co.bookvillage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

  // 자료검색 카테고리 클릭
  @GetMapping("/search.do")
  public String search() {
    //bookService.list(request, model);
    return "book/search";
  }
  
  // 검색 버튼 클릭 후 검색 리스트로 이동
  @GetMapping("/search/result")
  public String searchResult(BookSearchDto bookSearchDto,Model model) {
    bookService.searchBook(bookSearchDto, model);
    return "book/searchResult";
  }
  
  //검색 리스트에서 도서 상세로 이동 (하고있음)
  @GetMapping("/search/detail")
  public String detail(@RequestParam("isbn") String isbn, ScoreDto scoreDto, Model model) {
    bookService.getBookDetail(isbn, model);
    bookService.getScore(scoreDto.getIsbn(), model);
    return "book/detail";
  }
  
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
  
  //위시리스트 추가
  @PostMapping("/addWish.do")
  public String addWish(@RequestBody WishDto wishDto) {
    bookService.insertWish(wishDto);
    return "redirect:/book/search/detail?isbn=" + wishDto.getIsbn();
  }
  
  @PostMapping("/updateCheckout.do")
  public String updateCheckout(@RequestBody BookDto bookDto) {
    bookService.updateCheckout(bookDto);
    return "redirect:/book/search/detail?isbn=" + bookDto.getIsbn();
  }
}
