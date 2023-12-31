package kr.co.bookvillage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.bookvillage.dto.ScoreDto;

@Mapper
public interface ScoreMapper {
  public int insertScore(ScoreDto scoreDto);
  public int checkScore (ScoreDto scoreDto);
  public List<ScoreDto> cntStar (String isbn);
  public List<ScoreDto> getScoreList(String isbn);
  public Double getStarAvg(String isbn);
  public List<ScoreDto> getMyScoreList(String userNo);
  public void deleteScore(ScoreDto scoreDto);
  public void likeScore(ScoreDto scoreDto);
  public List<ScoreDto> bestReview(String isbn);

}
