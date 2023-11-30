package kr.co.bookvillage.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.bookvillage.dto.FaqDto;
import kr.co.bookvillage.service.FaqService;
import kr.co.bookvillage.service.NoticeService;
import lombok.RequiredArgsConstructor;

@RequestMapping(value="/support")
@RequiredArgsConstructor
@Controller
public class NoticeController {
	 
	private final NoticeService noticeService;
	private final FaqService faqService;
	  
	@GetMapping("/list.do")
	public String list( ) {
		return "support/list";
	}
	
	@GetMapping("/write.form")
	public String write( ) {
		return "support/write";
	}
	@PostMapping("/add.do")
	 public String add(MultipartHttpServletRequest multipartRequest
             , RedirectAttributes redirectAttributes) throws Exception {
		boolean addResult = noticeService.addNotice(multipartRequest);
		redirectAttributes.addFlashAttribute("addResult", addResult);
		return "redirect:/support/list.do";
	}
	
	@ResponseBody
	@GetMapping(value="/getList.do", produces="application/json")
	public Map<String, Object> getList(HttpServletRequest request){
		return noticeService.getNoticeList(request);
	}
	

	
	@GetMapping("/detail.do")
	public String detail(HttpServletRequest request, Model model) {
	    noticeService.loadNotice(request, model);
	    return "support/detail";
	}
	

    @GetMapping("/faqlist.do")
    public String list(Model model) {
    	List<FaqDto> faqList = faqService.getFaqList();
    	model.addAttribute("faqList", faqList);
    	return "support/faqlist";
    }

	@GetMapping("/faqwrite.form")
	public String faqwrite() {
		return "support/faqwrite";
	}
	
	@PostMapping("/faqadd.do")
	public String faqadd(FaqDto faqDto, RedirectAttributes redirectAttributes) {
	    int addResult = faqService.addFaq(faqDto);
	    redirectAttributes.addFlashAttribute("addResult", addResult);
	    return "redirect:/support/faqlist.do";
	}
	
	@GetMapping("/faqdetail.do")
	public String faqdetail(HttpServletRequest request, Model model) {
		faqService.loadFaq(request, model);
		return "support/faqdetail";
	}
	@PostMapping("/removeFaq.do")
	public String removeFaq(@RequestParam(value="faqNo", required=false, defaultValue="0") int faqNo
	                           , RedirectAttributes redirectAttributes) {
	    int removeResult = faqService.removeFaq(faqNo);
	    redirectAttributes.addFlashAttribute("removeResult", removeResult);
	    return "redirect:/support/faqlist.do";
	  }
	 @GetMapping("/editfaq.form")
	  public String edit(@RequestParam(value="faqNo", required=false, defaultValue="0") int faqNo
	                   , Model model) {
	    model.addAttribute("faq", faqService.getFaq(faqNo));
	    return "support/faqedit";
	  }
	  
	  @PostMapping("/modifyfaq.do")
	  public String modify(FaqDto faq, RedirectAttributes redirectAttributes) {
	    int modifyResult = faqService.modifyFaq(faq);
	    redirectAttributes.addFlashAttribute("modifyResult", modifyResult);
	    return "redirect:/support/faqdetail.do?faqNo=" + faq.getFaqNo();
	  }
}
