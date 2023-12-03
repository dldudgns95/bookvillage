package kr.co.bookvillage.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface FacService  {
  public void addFacility(MultipartHttpServletRequest multiRequest) throws Exception;
  public Map<String, Object> getFacTotalList(HttpServletRequest request);
  public int addFacApply(HttpServletRequest request);
  public boolean checkFacApply(HttpServletRequest request);
  public void getFacApplyList(HttpServletRequest request, Model model);
}
