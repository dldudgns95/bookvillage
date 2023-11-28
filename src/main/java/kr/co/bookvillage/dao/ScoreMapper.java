package kr.co.bookvillage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.bookvillage.dto.ScoreDto;

@Mapper
public interface ScoreMapper {
  public int insertScore(ScoreDto scoreDto);
  public List<ScoreDto> getScore(String isbn);
  public void deleteScore(ScoreDto scoreDto);

}
