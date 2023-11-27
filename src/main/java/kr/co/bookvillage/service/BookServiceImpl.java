package kr.co.bookvillage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import kr.co.bookvillage.dao.BookMapper;
import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.BookSearchDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

  @Autowired
  private BookMapper bookMapper;
  
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
}
