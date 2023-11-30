package kr.co.bookvillage.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.bookvillage.service.AdminService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/admin")
@Controller
@RequiredArgsConstructor
public class AdminController {
  
  private final AdminService adminService;
  
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
    adminService.getUserDetail(request, model);
    return "admin/userDetail";
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
    return "admin/facList";
  }
  
  @GetMapping("/facWrite.form")
  public String facWriteForm() {
    return "admin/facWrite";
  }
  
  @PostMapping("/facAdd.do")
  public String facAdd(MultipartHttpServletRequest multiRequest) throws Exception {
    System.out.println("facAdd.do::controller");
    adminService.addFacility(multiRequest);
    return "redirect:/admin/facList.do";
  }
  
  @GetMapping("/insertBooks.do")
  public String insertBooks(HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
    redirectAttributes.addFlashAttribute("bookCount", adminService.insertBook(request));
    return "redirect:/admin/bookList.do";
  }
  
  @GetMapping("/facList.do")
  public String facList(HttpServletRequest request, Model model) {
    model.addAttribute("facList", adminService.getFacTotalList(request));
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
  
  @PostMapping("/approvalBookCheckout.do")
  public String approvalBookCheckout(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("updateResult", adminService.approvalBookCheckout(request));
    return "redirect:/admin/bookCheckoutList.do";
  }
  
  @PostMapping("/approvalUserBookCheckout.do")
  public String approvalUserBookCheckout(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("updateCheckoutResult", adminService.approvalBookCheckout(request));
    redirectAttributes.addFlashAttribute("userNo", request.getParameter("userNo"));
    return "redirect:/admin/userDetail.do";
  }
  
  @PostMapping("/approvalBookCheckoutReturn.do")
  public String approvalBookCheckoutReturn(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("updateResult", adminService.approvalBookCheckoutReturn(request));
    return "redirect:/admin/bookCheckoutReturnList.do";
  }
  
  @PostMapping("/approvalUserBookCheckoutReturn.do")
  public String approvalUserBookCheckoutReturn(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("updateReturnResult", adminService.approvalBookCheckoutReturn(request));
    redirectAttributes.addFlashAttribute("userNo", request.getParameter("userNo"));
    return "redirect:/admin/userDetail.do";
  }
  
  
}
