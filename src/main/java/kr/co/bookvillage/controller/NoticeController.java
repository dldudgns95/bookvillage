package kr.co.bookvillage.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
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
import kr.co.bookvillage.dto.NoticeDto;
import kr.co.bookvillage.service.FaqService;
import kr.co.bookvillage.service.NoticeService;
import kr.co.bookvillage.service.QnaService;
import lombok.RequiredArgsConstructor;

@RequestMapping(value="/support")
@RequiredArgsConstructor
@Controller
public class NoticeController {
	 
	private final NoticeService noticeService;
	private final FaqService faqService;
	private final QnaService qnaService;
	
	
     @GetMapping("/noticeSearch.do")
	 public String noticeSearch(HttpServletRequest request, Model model) {
	    noticeService.getSearchNoticeList(request, model);
	    return "support/list"; 
	}
     
	@GetMapping("/list.do")
	public String list(HttpServletRequest request, Model model) {
	    noticeService.loadNoticeList(request, model);
	    return "support/list";
	}
	
	@GetMapping("/write.form")
	public String write( ) {
		return "support/write";
	}
	
	@PostMapping("/add.do")
	 public String add(MultipartHttpServletRequest multipartRequest) throws Exception {
		System.out.println("noticeAdd.do::controller");
		noticeService.addNotice(multipartRequest);
		return "redirect:/support/list.do";
	}
	

	
	@GetMapping("/detail.do")
	  public String detail(@RequestParam(value="ntNo", required=false, defaultValue="0") int ntNo
              , Model model) {
				NoticeDto notice = noticeService.getNotice(ntNo);
				model.addAttribute("notice", notice);
			return "support/detail";
	}
	

	@GetMapping("/download.do")
	public ResponseEntity<org.springframework.core.io.Resource> download(HttpServletRequest request) {
	    return noticeService.download(request);
	}
	  
	  @GetMapping("/downloadAll.do")
	  public ResponseEntity<org.springframework.core.io.Resource> downloadAll(HttpServletRequest request) {
	    return noticeService.downloadAll(request);
	  }
	  
	  @GetMapping("/edit.form")
	  public String edit(@RequestParam(value="ntNo", required=false, defaultValue="0") int ntNo
	                   , Model model) {
	    model.addAttribute("notice", noticeService.getNotice(ntNo));
	    return "support/edit";
	  }
	  
	  @PostMapping("/modify.do")
	  public String modify(NoticeDto notice, RedirectAttributes redirectAttributes) {
	    int modifyResult = noticeService.modifyNotice(notice);
	    redirectAttributes.addFlashAttribute("modifyResult", modifyResult);
	    return "redirect:/support/detail.do?ntNo=" + notice.getNtNo();
	  }
	  
	  @ResponseBody
	  @GetMapping(value="/getAttachList.do", produces="application/json")
	  public Map<String, Object> getAttachList(HttpServletRequest request) {
	    return noticeService.getAttachList(request);
	  }
	  
	  @ResponseBody
	  @PostMapping(value="/removeAttach.do", produces="application/json")
	  public Map<String, Object> removeAttach(HttpServletRequest request) {
	    return noticeService.removeAttach(request);
	  }
	  
	  @ResponseBody
	  @PostMapping(value="/addAttach.do", produces="application/json")
	  public Map<String, Object> addAttach(MultipartHttpServletRequest multipartRequest) throws Exception {
	    return noticeService.addAttach(multipartRequest);
	  }
	  
	  @PostMapping("/removeNotice.do")
	  public String removeNotice(@RequestParam(value="ntNo", required=false, defaultValue="0") int ntNo
	                           , RedirectAttributes redirectAttributes) {
	    int removeResult = noticeService.removeNotice(ntNo);
	    redirectAttributes.addFlashAttribute("removeResult", removeResult);
	    return "redirect:/support/list.do";
	  }

	@GetMapping("/faqSearch.do")
		 public String faqSearch(HttpServletRequest request, Model model) {
		    faqService.getSearchFaqList(request, model);
		    return "support/faqlist"; 
	}
	
    @GetMapping("/faqlist.do")
    public String faqlist(HttpServletRequest request, Model model) {
      faqService.loadFaqList(request, model);
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
	  public String editfaq(@RequestParam(value="faqNo", required=false, defaultValue="0") int faqNo
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