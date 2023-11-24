package kr.co.bookvillage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.bookvillage.service.AdminService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/admin")
@Controller
@RequiredArgsConstructor
public class AdminController {
  
  private final AdminService adminService;
  
  @GetMapping("/list.do")
  public String adminList() {
    return "admin/list";
  }
  
  @GetMapping("/insertBooks.do")
  public String insertBooks(HttpServletRequest request, Model model) throws Exception {
    model.addAttribute("bookCount", adminService.insertBook(request));
    return "admin/list";
  }
  
}
