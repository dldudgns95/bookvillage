package kr.co.bookvillage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequestMapping(value="/apply")
@RequiredArgsConstructor
@Controller
public class ApplyController {
	
	@GetMapping("/faclist.do")
	public String faclist( ) {
		return "apply/faclist";
	}
	
}
