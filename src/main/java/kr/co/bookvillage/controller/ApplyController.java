package kr.co.bookvillage.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.bookvillage.dto.BookApplyDto;
import kr.co.bookvillage.dto.FaqDto;
import kr.co.bookvillage.service.FacService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/apply")
@Controller
@RequiredArgsConstructor
public class ApplyController {
  
  private final FacService facService;
  
  
  @GetMapping("/facApplyList.do")
  public String facApply(HttpServletRequest request, Model model) {
    facService.getFacApplyList(request, model);
    return "apply/faclist";
  }
  
  @ResponseBody
  @GetMapping(value="/facTotalList.do", produces="application/json")
  public Map<String, Object> facTotalList(HttpServletRequest request) {
    return facService.getFacTotalList(request);
  }
  
  @ResponseBody
  @PostMapping(value="/addFacApply.do", produces="application/json")
  public Map<String, Object> addFacApply(HttpServletRequest request) {
    return Map.of("addResult", facService.addFacApply(request));
  }
  
  @ResponseBody
  @PostMapping(value="/checkFacApply.do", produces="application/json")
  public Map<String, Object> checkFacApply(HttpServletRequest request) {
    return Map.of("checkResult", facService.checkFacApply(request));
  }

  @GetMapping("/faclist.do")
  public String temp() {
    return "apply/faclist";
  }
  
  @GetMapping("/faclisttest.do")
  public String faclisttest() {
    return "apply/faclisttest";
  }
  
  @GetMapping("/fac.do")
  public String facList(Model model) {
    facService.getFacList(model);
    return "apply/fac";
  } 
    
  @GetMapping("/wishbook.do")
  public String wishbook() {
    return "apply/wishbook";
  }
  @GetMapping("/bookapply.do")
  public String bookapply() {
    return "apply/bookapply";
  }
  @PostMapping("/bookapplyadd.do")
  public String addBlog(HttpServletRequest request, RedirectAttributes redirectAttributes) {
	    int addResult = facService.addbook(request);
	    redirectAttributes.addFlashAttribute("addResult", addResult);
	    return "redirect:/apply/wishbook.do";
	}
}