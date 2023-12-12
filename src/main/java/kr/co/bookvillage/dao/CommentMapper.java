package kr.co.bookvillage.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.bookvillage.dto.AnswerDto;
import kr.co.bookvillage.dto.AskDto;
import kr.co.bookvillage.dto.AskImageDto;

@Mapper
public interface CommentMapper {
  
  public int insertAsk(AskDto ask);
  public int insertImg(AskImageDto askImg);
  public int getAskCount();
  public List<AskDto> getAskList(Map<String, Object> map);
  public int updateHit(int askNo);
  public AskDto getAsk(int askNo);
  public int updateAsk(AskDto ask);
  public List<AskImageDto> getAskImgList(int askNo);
  public int deleteAskImage(String filesystemName);
  public int deleteImageList(int aksNo);
  public int deleteAsk(int askNo);
  
  public int insertAns(AnswerDto ans);
  public int updateAnswerStatus(AnswerDto ans);

  
  
}
