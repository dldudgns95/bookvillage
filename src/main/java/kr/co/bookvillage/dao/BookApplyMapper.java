package kr.co.bookvillage.dao;

import org.apache.ibatis.annotations.Mapper;

import kr.co.bookvillage.dto.BookApplyDto;


@Mapper
public interface BookApplyMapper {
	public int insertBook(BookApplyDto bookapply);

}
