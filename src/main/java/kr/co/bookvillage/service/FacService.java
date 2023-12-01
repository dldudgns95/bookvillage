package kr.co.bookvillage.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import kr.co.bookvillage.dto.FacilityDto;

public interface FacService {

	public List<FacilityDto> getFacList();

	public Map<String, Object> getFacTotalList(HttpServletRequest request);

	public int addFacApply(HttpServletRequest request);

	public boolean checkFacApply(HttpServletRequest request);

}
