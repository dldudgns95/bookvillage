package kr.co.bookvillage.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.FacilityDto;
import kr.co.bookvillage.dto.NoticeDto;

@Mapper
public interface MainMapper {
  
  public List<BookDto> reviewTop3List();
  
  public List<BookDto> searchBookList(Map<String, Object> map);
  public List<NoticeDto> searchNoticeList(Map<String, Object> map);
  public List<FacilityDto> searchFacilityList(Map<String, Object> map);
  
  
  public List<FacilityDto> searchFacilityList();
  
  
  
  public List<BookDto> getBook(Map<String, Object> map);

  
}
