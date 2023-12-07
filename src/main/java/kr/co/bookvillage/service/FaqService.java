package kr.co.bookvillage.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import kr.co.bookvillage.dto.FaqDto;

public interface FaqService {
	public int addFaq(FaqDto faqDto);
	public void loadFaq(HttpServletRequest request, Model model);
	public int removeFaq(int faqNo);
	public FaqDto getFaq(int faqNo);
	public int modifyFaq(FaqDto faq);
	public void loadFaqList(HttpServletRequest request, Model model);
    public void getSearchFaqList(HttpServletRequest request, Model model);

}
