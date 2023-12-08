package kr.co.bookvillage.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.bookvillage.dto.BookApplyDto;

public interface FacService  {
  public Map<String, Object> getFacTotalList(HttpServletRequest request);
  public int addFacApply(HttpServletRequest request);
  public boolean checkFacApply(HttpServletRequest request);
  public void getFacApplyList(HttpServletRequest request, Model model);
  public int addbook(HttpServletRequest request);
  public void getFacList(Model model);
}
