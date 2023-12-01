package kr.co.bookvillage.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.bookvillage.dao.FacMapper;
import kr.co.bookvillage.dto.FacilityDto;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class FacServiceImpl implements FacService {
	private final FacMapper facMapper;
	
	@Transactional(readOnly=true)
	@Override
	public List<FacilityDto> getFacList() {
		return facMapper.getFacList();
	}
	
	@Override
	public Map<String, Object> getFacTotalList(HttpServletRequest request) {
	    String facStart = request.getParameter("facStart");
	    facStart = facStart.replaceAll("-", "");
	    facStart = facStart.substring(2);
	    
	    return Map.of("availableFacList", facMapper.availableFacList(facStart), "unavailableFacList", facMapper.unavailableFacList(facStart));
	}
	
	@Override
	public int addFacApply(HttpServletRequest request) {
	  int userNo = Integer.parseInt(request.getParameter("userNo"));
	  int facNo = Integer.parseInt(request.getParameter("facNo"));
	  Date facStart = Date.valueOf(request.getParameter("facStart"));
	    
	  int addResult = facMapper.addFacApply(Map.of("userNo", userNo
	                                                 , "facNo", facNo
	                                                 , "facStart", facStart));
	  return addResult;
	}
	
	@Override
	public boolean checkFacApply(HttpServletRequest request) {
	  int userNo = Integer.parseInt(request.getParameter("userNo"));
	  String facStart = request.getParameter("facStart");
	  facStart = facStart.replaceAll("-", "");
	  facStart = facStart.substring(2);
	  boolean check = facMapper.checkFacApply(Map.of("userNo", userNo, "facStart", facStart));
      return check;
	}
	
	
}
