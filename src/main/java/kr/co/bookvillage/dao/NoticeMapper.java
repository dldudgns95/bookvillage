package kr.co.bookvillage.dao;

import org.apache.ibatis.annotations.Mapper;

import kr.co.bookvillage.dto.AttachNtDto;
import kr.co.bookvillage.dto.NoticeDto;

@Mapper
public interface NoticeMapper {

	public int insertNotice(NoticeDto notice);
	public int insertAttach(AttachNtDto attach);

}
