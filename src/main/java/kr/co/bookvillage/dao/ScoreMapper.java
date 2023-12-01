package kr.co.bookvillage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.bookvillage.dto.ScoreDto;

@Mapper
public interface ScoreMapper {
  public int insertScore(ScoreDto scoreDto);
  public List<ScoreDto> getScoreList(String isbn);
  public List<ScoreDto> getMyScoreList(String userNo);
  public void deleteScore(ScoreDto scoreDto);

}
