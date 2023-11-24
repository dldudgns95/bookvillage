package kr.co.bookvillage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequestMapping("/book")
@RequiredArgsConstructor
@Controller
public class BookController {

  @GetMapping("/search.do")
  public String list(HttpServletRequest request, Model model) {
    //BookService.loadBookList(request, model);
    return "book/search";
  }
  
}
