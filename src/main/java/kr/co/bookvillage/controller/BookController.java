package kr.co.bookvillage.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.BookSearchDto;
import kr.co.bookvillage.dto.ScoreDto;
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
  public String detail(@RequestParam("isbn") String isbn, Model model) {
    bookService.getBookDetail(isbn, model);
    return "book/detail";
  }
  
  //리뷰 저장
  @PostMapping("/review.do")
  public ResponseEntity<String> saveReview(@RequestBody ScoreDto scoreDto) {
    System.out.println("별점: " + scoreDto.getStar());
    System.out.println("리뷰: " + scoreDto.getReview());
    return ResponseEntity.ok().body("평가가 성공적으로 저장되었습니다.");
  }
}
