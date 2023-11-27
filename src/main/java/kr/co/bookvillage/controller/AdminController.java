package kr.co.bookvillage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.bookvillage.service.AdminService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/admin")
@Controller
@RequiredArgsConstructor
public class AdminController {
  
  private final AdminService adminService;
  
  @GetMapping("/list.do")
  public String mainList() {
    return "admin/main";
  }
  
  @GetMapping("/memberList.do")
  public String memberList(HttpServletRequest request, Model model) {
    adminService.getUserList(request, model);
    return "admin/memberList";
  }
  
  @GetMapping("/insertBooks.do")
  public String insertBooks(HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
    redirectAttributes.addFlashAttribute("bookCount", adminService.insertBook(request));
    return "redirect:/admin/memberList.do";
  }
  
}
