package kr.co.bookvillage.service;

import java.util.List;

import kr.co.bookvillage.dto.AttachFacDto;
import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.FacilityDto;

public interface MainService {
  
  public List<BookDto> getReviewTop3();
  public List<AttachFacDto> getFacList();


}
