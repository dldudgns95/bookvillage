package kr.co.bookvillage.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import kr.co.bookvillage.dao.FaqMapper;
import kr.co.bookvillage.dto.FaqDto;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class FaqServiceImpl implements FaqService {
	
	private final FaqMapper faqMapper;
	
	@Transactional(readOnly=true)
	@Override
	public List<FaqDto> getFaqList() {
		return faqMapper.getFaqList();
	}
	@Override
	public int addFaq(FaqDto faqDto) {
		int addResult = faqMapper.insertFaq(faqDto);
		return addResult;
	}
	
	@Transactional(readOnly=true)
	@Override
	public void loadFaq(HttpServletRequest request, Model model) {
		
	    Optional<String> opt = Optional.ofNullable(request.getParameter("faqNo"));
	    int faqNo = Integer.parseInt(opt.orElse("0"));
	    
	    model.addAttribute("faq", faqMapper.getFaq(faqNo));	    		
		
	}
	@Override
	public int removeFaq(int faqNo) {
		return faqMapper.deleteFaq(faqNo);
	}
	
	@Transactional(readOnly=true)
	@Override
	public FaqDto getFaq(int faqNo) {
		return faqMapper.getFaq(faqNo);
	}
	
	@Override
	public int modifyFaq(FaqDto faq) {
		return faqMapper.updateFaq(faq);
	}
}
