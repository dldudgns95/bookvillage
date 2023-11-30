package kr.co.bookvillage.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import kr.co.bookvillage.dto.FaqDto;

public interface FaqService {

	public List<FaqDto> getFaqList();
	public int addFaq(FaqDto faqDto);
	public void loadFaq(HttpServletRequest request, Model model);
	public int removeFaq(int faqNo);
	public FaqDto getFaq(int faqNo);
	public int modifyFaq(FaqDto faq);

}
