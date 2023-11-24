package kr.co.bookvillage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.bookvillage.service.QnaService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/qna")
@RequiredArgsConstructor
@Controller
public class QnaController {
	
	 private final QnaService qnaService;
 
	@GetMapping("/list.do")
	  public String list(HttpServletRequest request, Model model) {
		qnaService.loadQnaList(request, model);
	    return "qna/list";
	  }
}
