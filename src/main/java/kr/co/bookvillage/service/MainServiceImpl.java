package kr.co.bookvillage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import kr.co.bookvillage.dao.AdminMapper;
import kr.co.bookvillage.dao.MainMapper;
import kr.co.bookvillage.dto.AttachFacDto;
import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.FacilityDto;
import kr.co.bookvillage.dto.NoticeDto;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class MainServiceImpl implements MainService {
  
  private final MainMapper mainMapper;
  private final AdminMapper adminMapper;

  @Override
    public List<BookDto> getReviewTop3() {
      return mainMapper.reviewTop3List();
    }
 
  @Transactional(readOnly = true)
  @Override
  public List<AttachFacDto> getFacList() {
    return adminMapper.getFacList();
  }
  
  @Transactional(readOnly = true)
  @Override
    public void getSearList(HttpServletRequest request, Model model) {
    
      String query = request.getParameter("query");
      
      Map<String, Object> map = new HashMap<>();
      map.put("query", query);

      List<BookDto> bookList = mainMapper.searchBookList(map);
      List<NoticeDto> noticeList = mainMapper.searchNoticeList(map);
      List<FacilityDto> facList = mainMapper.searchFacilityList(map);

      model.addAttribute("bookList",bookList);
      model.addAttribute("noticeList", noticeList);
      model.addAttribute("facList", facList);
      
      
    }
  
  @Override
  public List<FacilityDto> getSearchFacility(HttpServletRequest request, Model model) {
    
    String query = request.getParameter("query");
    
    Map<String, Object> map = new HashMap<>();
    map.put("query", query);
    
    return mainMapper.searchFacilityList();
  }
  

  
  
  
}
