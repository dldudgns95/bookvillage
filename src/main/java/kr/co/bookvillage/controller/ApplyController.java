package kr.co.bookvillage.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.bookvillage.service.FacService;
import lombok.RequiredArgsConstructor;

@RequestMapping(value="/apply")
@RequiredArgsConstructor
@Controller
public class ApplyController {
	private FacService facService;
	
    @GetMapping("/faclist.do")
	public String facList(HttpServletRequest request, Model model) {
	    model.addAttribute("facList", facService.getFacTotalList(request));
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
}
