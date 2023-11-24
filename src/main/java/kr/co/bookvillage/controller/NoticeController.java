package kr.co.bookvillage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.bookvillage.service.NoticeService;
import lombok.RequiredArgsConstructor;

@RequestMapping(value="/support")
@RequiredArgsConstructor
@Controller
public class NoticeController {
	 
	private final NoticeService supportService;
	  
	@GetMapping("/notice.do")
	public String noticelist( ) {
		return "support/notice";
	}
	
	@GetMapping("/noticewrite.form")
	public String noticewrite( ) {
		return "support/noticewrite";
	}
	@PostMapping("/add.do")
	 public String add(MultipartHttpServletRequest multipartRequest
             , RedirectAttributes redirectAttributes) throws Exception {
		boolean addResult = supportService.addNotice(multipartRequest);
		redirectAttributes.addFlashAttribute("addResult", addResult);
		return "redirect:/upload/list.do";
}	
	@GetMapping("/faq.do")
	public String faqlist( ) {
		return "support/faq";
	}
	

}
