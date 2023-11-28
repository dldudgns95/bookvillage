package kr.co.bookvillage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.co.bookvillage.dao.ScoreMapper;
import kr.co.bookvillage.dto.ScoreDto;

@SpringBootTest
public class ScoreMapperTest {

  @Autowired
  private ScoreMapper scoreMapper;
  
  //@Test
  public void testInsertScore() {
      // 테스트할 데이터 생성
      ScoreDto scoreDto = new ScoreDto();
      scoreDto.setIsbn("9788932042336"); // ISBN 값 설정
      scoreDto.setUserNo(1); // 사용자 번호 설정

      // 현재 날짜로 java.sql.Date 생성
      long currentTime = System.currentTimeMillis();
      Date currentDate = new Date(currentTime);
      scoreDto.setReviewDate(currentDate);
      
      scoreDto.setStar(5); // 별점 설정
      scoreDto.setReview("훌륭한 책입니다."); // 리뷰 설정

      // 데이터베이스에 삽입
      scoreMapper.insertScore(scoreDto);

  }
  
  @Test
  public void testGetScore() {
    List<ScoreDto> scoreList = scoreMapper.getScore("9788932042336");

    assertNotNull(scoreList);
    assertFalse(scoreList.isEmpty());

    for (ScoreDto scoreDto : scoreList) {
        assertNotNull(scoreDto.getIsbn());
        assertNotNull(scoreDto.getUserNo());
        assertNotNull(scoreDto.getReviewDate());
        assertNotNull(scoreDto.getStar());
        assertNotNull(scoreDto.getReview());
        
        System.out.println("ISBN: " + scoreDto.getIsbn());
        System.out.println("UserNo: " + scoreDto.getUserNo());
        System.out.println("ReviewDate: " + scoreDto.getReviewDate());
        System.out.println("Star: " + scoreDto.getStar());
        System.out.println("Review: " + scoreDto.getReview());
    }
  }
}
