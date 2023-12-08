package kr.co.bookvillage.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.bookvillage.dto.FaqDto;
import kr.co.bookvillage.dto.UserDto;

@Mapper
public interface FaqMapper {

	public int insertFaq(FaqDto faqDto);
	public FaqDto getFaq(int faqNo);
	public int deleteFaq(int faqNo);
	public int updateFaq(FaqDto faq);
	public List<FaqDto> getFaqList(Map<String, Object> map);
	public int getFaqCount();
	public int faqSearchCount(Map<String, String> of);
	public List<UserDto> getSearchFaqList(Map<String, Object> map);
}