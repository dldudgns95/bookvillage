package kr.co.bookvillage.service;

import org.springframework.ui.Model;

import kr.co.bookvillage.dto.BookSearchDto;


public interface BookService {

  public void searchBook(BookSearchDto bookSearchDto, Model model);
  public void getBookDetail(String isbn, Model model);
}
