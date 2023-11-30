package kr.co.bookvillage.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface AdminService  {
  public int insertBook(HttpServletRequest request);
  public void getUserList(HttpServletRequest request, Model model);
  public void getUserDetail(HttpServletRequest request, Model model);
  public void getSearchUserList(HttpServletRequest request, Model model);
  public void getBookList(HttpServletRequest request, Model model);
  public void getBookDetail(HttpServletRequest request, Model model);
  public void getSearchBookList(HttpServletRequest request, Model model);
  public void addFacility(MultipartHttpServletRequest multiRequest) throws Exception;
  public Map<String, Object> getFacTotalList(HttpServletRequest request);
  public int addFacApply(HttpServletRequest request);
  public boolean checkFacApply(HttpServletRequest request);
  public void getBookApplyList(HttpServletRequest request, Model model);
  public void getBookApplyDetail(HttpServletRequest request, Model model);
  public void getBookCheckoutList(HttpServletRequest request, Model model);
  public void getBookCheckoutSearchList(HttpServletRequest request, Model model);
  public void getBookCheckoutReturnList(HttpServletRequest request, Model model);
  public void getBookCheckoutReturnSearchList(HttpServletRequest request, Model model);
  public int approvalBookCheckout(HttpServletRequest request);
  public int approvalBookCheckoutReturn(HttpServletRequest request);
}
