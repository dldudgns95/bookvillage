package kr.co.bookvillage.dao;

import java.util.List;
import java.util.Map;

import kr.co.bookvillage.dto.AttachAskDto;
import kr.co.bookvillage.dto.QnaDto;

public interface QnaMapper {
	  public int insertAsk(QnaDto qna);
	  public int insertAttach(AttachAskDto attach);
	  public int getAskCount();
	  public List<QnaDto> getAskList(Map<String, Object> map);
	  
}
