package kr.co.bookvillage.service;

import java.util.List;

import kr.co.bookvillage.dto.FaqDto;

public interface FaqService {

	public List<FaqDto> getFaqList();
	public int addFaq(FaqDto faqDto);

}
