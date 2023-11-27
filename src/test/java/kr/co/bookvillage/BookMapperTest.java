package kr.co.bookvillage;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.co.bookvillage.dao.BookMapper;
import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.BookSearchDto;

@SpringBootTest
public class BookMapperTest {

  @Autowired
  private BookMapper bookMapper;
  
  @Test
  public void testGetBook() {
    // 상황
    BookSearchDto bookSearchDto = new BookSearchDto();
    bookSearchDto.setSs("total");
    bookSearchDto.setSt("바둑");

    // 실행
    List<BookDto> result = bookMapper.getBook(bookSearchDto);

    // 검증
    // 결과 출력
    System.out.println("Search result:");
    for (BookDto book : result) {
        System.out.println("ISBN: " + book.getIsbn());
        System.out.println("TITLE: " + book.getTitle());
        System.out.println("COVER: " + book.getCover());
        System.out.println("AUTHOR: " + book.getAuthor());
        System.out.println("--------------------------");
    }
}
}
