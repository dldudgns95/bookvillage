package kr.co.bookvillage.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.bookvillage.dto.AttachNtDto;
import kr.co.bookvillage.dto.NoticeDto;

@Mapper
public interface NoticeMapper {

	public int insertNotice(NoticeDto notice);
	public int insertAttach(AttachNtDto attach);
	public int getNoticeCount();
	public List<NoticeDto> getNoticeList(Map<String, Object> map);
	public NoticeDto getNotice(int ntNo);

}
