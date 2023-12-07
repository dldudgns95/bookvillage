package kr.co.bookvillage.service;

import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.bookvillage.dto.AttachFacDto;

public interface AdminService  {
  public int insertBook(HttpServletRequest request);
  public void getUserList(HttpServletRequest request, Model model);
  public void getUserDetail(HttpServletRequest request, Model model);
  public int deleteUser(HttpServletRequest request);
  public void getSearchUserList(HttpServletRequest request, Model model);
  public void getBookList(HttpServletRequest request, Model model);
  public void getBookDetail(HttpServletRequest request, Model model);
  public void getSearchBookList(HttpServletRequest request, Model model);
  public int addFacility(MultipartHttpServletRequest multiRequest) throws Exception;
  public void getFacList(Model model);
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
  public void getFacApplyList(HttpServletRequest request, Model model);
  public Map<String, Object> getAddBookSearch(HttpServletRequest request);
  public Map<String, Object> addBook(HttpServletRequest request);
  public int updateBookApply(HttpServletRequest request);
  public int approveFacApply(HttpServletRequest request);
  public int refuseFacApply(HttpServletRequest request);
  public int deleteFac(HttpServletRequest request);
  public int deleteBook(HttpServletRequest request);
  public int activeUser(HttpServletRequest request);
  public int inactiveUser(HttpServletRequest request);
  public AttachFacDto getFacDetail(HttpServletRequest request);
  public int editFacility(MultipartHttpServletRequest multiRequest) throws Exception;
  public int addDirectBook(MultipartHttpServletRequest multiRequest) throws Exception;
  public int editBook(MultipartHttpServletRequest multiRequest) throws Exception;
  public boolean checkBook(HttpServletRequest request);
}
