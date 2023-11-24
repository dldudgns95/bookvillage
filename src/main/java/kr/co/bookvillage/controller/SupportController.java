package kr.co.bookvillage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequestMapping(value="/support")
@RequiredArgsConstructor
@Controller
public class SupportController {
	
	@GetMapping("/notice.do")
	public String noticelist( ) {
		return "support/notice";
	}

}
