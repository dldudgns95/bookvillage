package kr.co.bookvillage.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.bookvillage.dao.AdminMapper;
import kr.co.bookvillage.dao.MainMapper;
import kr.co.bookvillage.dto.AttachFacDto;
import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.FacilityDto;
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
  
  @Override
  public List<AttachFacDto> getFacList() {
    return adminMapper.getFacList();
  }
  
  
}
