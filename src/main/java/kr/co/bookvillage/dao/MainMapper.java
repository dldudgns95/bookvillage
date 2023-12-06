package kr.co.bookvillage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.bookvillage.dto.BookDto;

@Mapper
public interface MainMapper {
  
  public List<BookDto> reviewTop3List();
  
}
