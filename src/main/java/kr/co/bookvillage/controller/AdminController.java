package kr.co.bookvillage.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.service.AdminService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/admin")
@Controller
@RequiredArgsConstructor
public class AdminController {
  
  private final AdminService adminService;
  private final ObjectMapper objectMapper;
  
  @GetMapping("/main.do")
  public String mainList() {
    return "admin/main";
  }
  
  @GetMapping("/userList.do")
  public String userList(HttpServletRequest request, Model model) {
    adminService.getUserList(request, model);
    return "admin/userList";
  }
  
  @GetMapping("/userSearch.do")
  public String userSearch(HttpServletRequest request, Model model) {
    adminService.getSearchUserList(request, model);
    return "admin/userList";
  }
  
  @RequestMapping(value="/userDetail.do", method={RequestMethod.GET, RequestMethod.POST})
  public String userDetail(HttpServletRequest request, Model model) {
    System.out.println("controller:: userNo : " + request.getParameter("userNo"));
    adminService.getUserDetail(request, model);
    return "admin/userDetail";
  }
  
  @PostMapping("/userDelete.do")
  public String userDelete(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("deleteResult", adminService.deleteUser(request));
    return "redirect:/admin/userList.do";
  }
  
  @GetMapping("/bookList.do")
  public String bookList(HttpServletRequest request, Model model) {
    adminService.getBookList(request, model); 
    return "admin/bookList"; 
  }
  
  @GetMapping("/bookSearch.do")
  public String bookSearch(HttpServletRequest request, Model model) {
    adminService.getSearchBookList(request, model);
    return "admin/bookList"; 
  }
  
  @GetMapping("/bookDetail.do")
  public String bookDetail(HttpServletRequest request, Model model) {
    adminService.getBookDetail(request, model);
    return "admin/bookDetail";
  }
  
  @GetMapping("/facApplyList.do")
  public String facApplyList(HttpServletRequest request, Model model) {
    adminService.getFacApplyList(request, model);
    return "admin/facApplyList";
  }
  
  @GetMapping("/facWrite.form")
  public String facWriteForm() {
    return "admin/facWrite";
  }
  
  @PostMapping("/facAdd.do")
  public String facAdd(MultipartHttpServletRequest multiRequest, RedirectAttributes redirectAttributes) throws Exception {
    redirectAttributes.addFlashAttribute("addResult", adminService.addFacility(multiRequest));
    return "redirect:/admin/facList.do";
  }
  
  @GetMapping("/insertBooks.do")
  public String insertBooks(HttpServletRequest request, RedirectAttributes redirectAttributes){
    redirectAttributes.addFlashAttribute("bookCount", adminService.insertBook(request));
    return "redirect:/admin/bookList.do";
  }
  
  @GetMapping("/facList.do")
  public String facList(HttpServletRequest request, Model model) {
    adminService.getFacList(request, model);
    return "admin/facList";
  }
  
  @ResponseBody
  @GetMapping(value="/facTotalList.do", produces="application/json")
  public Map<String, Object> facTotalList(HttpServletRequest request) {
    return adminService.getFacTotalList(request);
  }
  
  @ResponseBody
  @PostMapping(value="/addFacApply.do", produces="application/json")
  public Map<String, Object> addFacApply(HttpServletRequest request) {
    return Map.of("addResult", adminService.addFacApply(request));
  }
  
  @ResponseBody
  @PostMapping(value="/checkFacApply.do", produces="application/json")
  public Map<String, Object> checkFacApply(HttpServletRequest request) {
    return Map.of("checkResult", adminService.checkFacApply(request));
  }
  
  @GetMapping("/bookApplyList.do")
  public String bookApplyList(HttpServletRequest request, Model model) {
    adminService.getBookApplyList(request, model);
    return "admin/bookApplyList";
  }
  
  @GetMapping("/bookApplyDetail.do")
  public String bookApplyDetail(HttpServletRequest request, Model model) {
    adminService.getBookApplyDetail(request, model);
    return "admin/bookApplyDetail";
  }
  
  @GetMapping("/bookCheckoutList.do")
  public String bookCheckoutList(HttpServletRequest request, Model model) {
    adminService.getBookCheckoutList(request, model);
    return "admin/bookCheckoutList";
  }
  
  @GetMapping("/bookCheckoutSearch.do")
  public String bookCheckoutSearch(HttpServletRequest request, Model model) {
    adminService.getBookCheckoutSearchList(request, model);
    return "admin/bookCheckoutList";
  }
  
  @GetMapping("/bookCheckoutReturnList.do")
  public String bookCheckoutReturnList(HttpServletRequest request, Model model) {
    adminService.getBookCheckoutReturnList(request, model);
    return "admin/bookCheckoutReturnList";
  }
  
  @GetMapping("/bookCheckoutReturnSearch.do")
  public String bookCheckoutReturnSearch(HttpServletRequest request, Model model) {
    adminService.getBookCheckoutReturnSearchList(request, model);
    return "admin/bookCheckoutReturnList";
  }
  
  // 도서 대출 승인
  @PostMapping("/approvalBookCheckout.do")
  public String approvalBookCheckout(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("updateResult", adminService.approvalBookCheckout(request));
    return "redirect:/admin/bookCheckoutList.do";
  }
  
  // 유저 상세에서 사용하는 도서 대출
  @PostMapping("/approvalUserBookCheckout.do")
  public String approvalUserBookCheckout(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("updateCheckoutResult", adminService.approvalBookCheckout(request));
    System.out.println("controller:: userNo : " + request.getParameter("userNo"));
    return "redirect:/admin/userDetail.do?userNo=" + request.getParameter("userNo");
  }
  
  // 도서 반납 승인
  @PostMapping("/approvalBookCheckoutReturn.do")
  public String approvalBookCheckoutReturn(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("updateResult", adminService.approvalBookCheckoutReturn(request));
    return "redirect:/admin/bookCheckoutReturnList.do";
  }
  
  // 유저 상세에서 사용하는 도서 반납
  @PostMapping("/approvalUserBookCheckoutReturn.do")
  public String approvalUserBookCheckoutReturn(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("updateReturnResult", adminService.approvalBookCheckoutReturn(request));
    System.out.println("controller:: userNo : " + request.getParameter("userNo"));
    return "redirect:/admin/userDetail.do?userNo=" + request.getParameter("userNo");
  }
  
  @GetMapping("/addBookList.do")
  public String addBookList() {
    return "admin/addBookList";
  }
  
  @GetMapping("/goBookSearch.do")
  public String goBookSearch(HttpServletRequest request, RedirectAttributes redirectAttributes) { 
    redirectAttributes.addFlashAttribute("searchBookName", request.getParameter("searchBookName"));
    return "redirect:/admin/addBookList.do";
  }
  
  @ResponseBody
  @GetMapping(value="/addBookSearch.do", produces="application/json")
  public Map<String, Object> addBookSearch(HttpServletRequest request) {
    return adminService.getAddBookSearch(request);
  }
  
  @ResponseBody
  @GetMapping(value="/addBook.do", produces="application/json")
  public Map<String, Object> addBook(HttpServletRequest request) {
    return adminService.addBook(request);
  }
  
  @GetMapping("/updateBookApply.do")
  public String updateBookApply(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("updateResult", adminService.updateBookApply(request));
    return "redirect:/admin/bookApplyList.do";
  }
  
  @PostMapping("/approveFacApply.do")
  public String approveFacApply(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("updateApproveResult", adminService.approveFacApply(request));
    return "redirect:/admin/facApplyList.do";
  }
  
  @PostMapping("/refuseFacApply.do")
  public String refuseFacApply(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("updateRefuseResult", adminService.refuseFacApply(request));
    return "redirect:/admin/facApplyList.do";
  }
  
  @PostMapping("/approveUserFacApply.do")
  public String approveUserFacApply(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("updateApproveResult", adminService.approveFacApply(request));
    return "redirect:/admin/userDetail.do?userNo=" + request.getParameter("userNo");
  }
  
  @PostMapping("/refuseUserFacApply.do")
  public String refuseUserFacApply(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("updateRefuseResult", adminService.refuseFacApply(request));
    return "redirect:/admin/userDetail.do?userNo=" + request.getParameter("userNo");
  }
  
  @PostMapping("/facDelete.do")
  public String facDelete(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("deleteResult", adminService.deleteFac(request));
    return "redirect:/admin/facList.do";
  }
  
  @PostMapping("/deleteBook.do")
  public String deleteBook(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("deleteResult", adminService.deleteBook(request));
    return "redirect:/admin/bookList.do";
  }
  
  @PostMapping("/activeUser.do")
  public String activeUser(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("updateActiveUserResult", adminService.activeUser(request));
    return "redirect:/admin/userDetail.do?userNo=" + request.getParameter("userNo");
  }
  
  @PostMapping("/inactiveUser.do")
  public String inactiveUser(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("updateInactiveUserResult", adminService.inactiveUser(request));
    return "redirect:/admin/userDetail.do?userNo=" + request.getParameter("userNo");
  }
  
  @GetMapping("/facEdit.form")
  public String facEditForm(HttpServletRequest request, Model model) {
    model.addAttribute("facApply", adminService.getFacDetail(request));
    return "admin/facEdit";
  }
  
  @PostMapping("/facEdit.do")
  public String facEdit(MultipartHttpServletRequest multiRequest, RedirectAttributes redirectAttributes) throws Exception {
    redirectAttributes.addFlashAttribute("editResult", adminService.editFacility(multiRequest));
    return "redirect:/admin/facList.do";
  }
  
  @GetMapping("/addBook.form")
  public String addBookForm() {
    return "admin/addBook";
  }
  
  @PostMapping("/bookDirectAdd.do")
  public String bookDirectAdd(MultipartHttpServletRequest multiRequest, RedirectAttributes redirectAttributes) throws Exception {
    redirectAttributes.addFlashAttribute("insertResult", adminService.addDirectBook(multiRequest));
    return "redirect:/admin/addBookList.do";
  }
  
  @GetMapping("/bookEdit.form")
  public String bookEditForm(HttpServletRequest request, Model model) {
    adminService.getBookDetail(request, model);
    return "admin/editBook";
  }
  
  @PostMapping("/editBook.do")
  public String editBook(MultipartHttpServletRequest multiRequest, RedirectAttributes redirectAttributes) throws Exception {
    redirectAttributes.addFlashAttribute("editResult", adminService.editBook(multiRequest));
    return "redirect:/admin/bookDetail.do?isbn=" + multiRequest.getParameter("isbn");
  }
  
  @ResponseBody
  @GetMapping(value="/checkBook.do", produces="application/json")
  public Map<String, Object> checkBook(HttpServletRequest request) {
    return Map.of("checkResult", adminService.checkBook(request));
  }
  
  @PostMapping("/inactiveBook.do")
  public String inactiveBook(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("inactiveResult", adminService.inactiveBook(request));
    return "redirect:/admin/bookDetail.do?isbn=" + request.getParameter("isbn");
  }
  
  @PostMapping("/activeBook.do")
  public String activeBook(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("activeResult", adminService.activeBook(request));
    return "redirect:/admin/bookDetail.do?isbn=" + request.getParameter("isbn");
  }
  
  @PostMapping("/approveCheckBookCheckout.do")
  public String approveCheckBookCheckout(@RequestParam("numbers") String numbersJson, RedirectAttributes redirectAttributes) throws IOException {
    List<String> list = objectMapper.readValue(numbersJson, List.class);
    redirectAttributes.addFlashAttribute("updateResult", adminService.approveBookCheckoutByNumbers(list));
    // List<Integer> numbers = objectMapper.readValue(numbersJson, List.class);
    // String str = StringUtils.join(numbers, ",");
    return "redirect:/admin/bookCheckoutList.do";
  }
  
  @PostMapping("/approveCheckBookCheckoutReturn.do")
  public String approveCheckBookCheckoutReturn(@RequestParam("numbers") String numbersJson, RedirectAttributes redirectAttributes) throws IOException {
    List<String> list = objectMapper.readValue(numbersJson, List.class);
    redirectAttributes.addFlashAttribute("updateResult", adminService.approveBookCheckoutReturnByNumbers(list));
    return "redirect:/admin/bookCheckoutReturnList.do";
  }
  
  @ResponseBody
  @PostMapping(value="/ajaxBookCheckoutPaing.do", produces="application/json")
  public Map<String, Object> ajaxBookCheckoutPaing(@RequestBody Map<String, Object> params){
    return adminService.getAjaxBookCheckoutPaing(params);
  }
  
  @ResponseBody
  @PostMapping(value="/ajaxFacApplyPaging.do", produces="application/json")
  public Map<String, Object> ajaxFacApplyPaging(@RequestBody Map<String, Object> params){
    return adminService.getAjaxFacApplyPaing(params);
  }
  
  @ResponseBody
  @PostMapping(value="/ajaxBookApplyPaging.do", produces="application/json")
  public Map<String, Object> ajaxBookApplyPaging(@RequestBody Map<String, Object> params){
    return adminService.getAjaxBookApplyPaing(params);
  }
  
  @GetMapping("/deleteBookApply.do")
  public String deleteBookApply(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("deleteResult", adminService.deleteBookApply(request));
    return "redirect:/admin/bookApplyList.do";
  }
  
  // 임시
  @GetMapping("/temp.do")
  public String temp() {
    return "admin/facApplyList2";
  }
  
}
