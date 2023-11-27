package kr.co.bookvillage.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
