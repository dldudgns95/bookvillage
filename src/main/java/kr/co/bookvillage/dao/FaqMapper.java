package kr.co.bookvillage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.bookvillage.dto.FaqDto;

@Mapper
public interface FaqMapper {

	public List<FaqDto> getFaqList();
	public int insertFaq(FaqDto faqDto);

}
