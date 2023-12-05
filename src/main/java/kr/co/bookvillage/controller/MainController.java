package kr.co.bookvillage.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.FaqDto;
import kr.co.bookvillage.dto.NoticeDto;
import kr.co.bookvillage.service.BookService;
import kr.co.bookvillage.service.UserService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/main")

@Controller
@RequiredArgsConstructor
public class MainController {
  
  private final UserService userService;
  private final BookService bookService;
  
  
  @ResponseBody
  @GetMapping("/faqList.do")
  public List<FaqDto> faqList() {
      return userService.getFaqList();
  }
  
  @ResponseBody
  @GetMapping("/noticeList.do")
  public List<NoticeDto> noticeList(){
    return userService.getNoticeList();
  }
  
  
  @GetMapping("/mainSearch.form")
  public String mainSearch() {
    return "main/mainSearch";
  }
  
  // 책 이미지..??
//  @GetMapping(value = "/bookImageList.do")
//  public List<BookDto> bookImageList(Model model){
//     return userService.getBookList("book", book);
//  }
  

//  @GetMapping("/books")
//  public String getBooks(Model model) {
//      List<BookDto> newBooks = bookService.getNewBooks();
//      model.addAttribute("newBooks", newBooks);
//      return "books"; // books.html을 응답으로 사용
//  }
//  
  
  
  
  
}
