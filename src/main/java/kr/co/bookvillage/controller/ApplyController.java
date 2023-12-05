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

import kr.co.bookvillage.service.FacService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/apply")
@Controller
@RequiredArgsConstructor
public class ApplyController {
  
  private final FacService facService;
  
  
  @GetMapping("/facApplyList.do")
  public String facApplyList(HttpServletRequest request, Model model) {
    facService.getFacApplyList(request, model);
    return "apply/faclist";
  }
  
  @GetMapping("/facWrite.form")
  public String facWriteForm() {
    return "admin/facWrite";
  }
  
  @PostMapping("/facAdd.do")
  public String facAdd(MultipartHttpServletRequest multiRequest) throws Exception {
	  facService.addFacility(multiRequest);
    return "redirect:/admin/facApplyList.do";
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
  // 임시
  @GetMapping("/faclist.do")
  public String temp() {
    return "apply/faclist";
  }
  
}
