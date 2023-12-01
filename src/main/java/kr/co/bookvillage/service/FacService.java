package kr.co.bookvillage.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface FacService {

	public Map<String, Object> getFacTotalList(HttpServletRequest request);

	public int addFacApply(HttpServletRequest request);

	public boolean checkFacApply(HttpServletRequest request);

}
