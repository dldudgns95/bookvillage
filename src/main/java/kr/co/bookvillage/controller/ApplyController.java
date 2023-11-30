package kr.co.bookvillage.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.bookvillage.dto.FacilityDto;
import kr.co.bookvillage.dto.FaqDto;
import kr.co.bookvillage.service.FacService;
import lombok.RequiredArgsConstructor;

@RequestMapping(value="/apply")
@RequiredArgsConstructor
@Controller
public class ApplyController {
	private FacService facService;
	
	@GetMapping("/faclist.do")
	public String faclist( ) {
		return "apply/faclist";
	}
	
    @GetMapping("/getfaclist.do")
    public String getfaclist(Model model) {
    	List<FacilityDto> faclist = facService.getFacList();
    	model.addAttribute("faclist", faclist);
		return "apply/faclist";
    }
}
