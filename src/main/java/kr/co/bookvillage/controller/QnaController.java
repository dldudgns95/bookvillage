package kr.co.bookvillage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.bookvillage.service.QnaService;
//import kr.co.bookvillage.service.QnaService;
import lombok.RequiredArgsConstructor;

@RequestMapping(value="/qna")
@RequiredArgsConstructor
@Controller
public class QnaController {
	
	private final QnaService qnaService;
	  
	@GetMapping("/list.do")
	public String list( ) {
		return "support/list";
	}
}