package kr.co.bookvillage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
  
  @GetMapping("/bookList.do")
  public String bookList(HttpServletRequest request, Model model) {
    adminService.getBookList(request, model);
    return "admin/bookList"; 
  }
  
  @GetMapping("/facList.do")
  public String facList() {
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
    return "admin/facList";
  }
  
  @GetMapping("/insertBooks.do")
  public String insertBooks(HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
    redirectAttributes.addFlashAttribute("bookCount", adminService.insertBook(request));
    return "redirect:/admin/userList.do";
  }
  
}
