package kr.co.bookvillage.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import kr.co.bookvillage.dto.AttachFacDto;
import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.FacilityDto;

public interface MainService {
  
  public List<BookDto> getReviewTop3();
  public List<AttachFacDto> getFacList();
  
  public void getSearList(HttpServletRequest request, Model model);
  
  public List<FacilityDto> getSearchFacility(HttpServletRequest request, Model model);
  


}
