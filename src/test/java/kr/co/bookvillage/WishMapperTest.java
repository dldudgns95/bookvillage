package kr.co.bookvillage;

import java.sql.Timestamp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.co.bookvillage.dao.WishMapper;
import kr.co.bookvillage.dto.WishDto;

@SpringBootTest
public class WishMapperTest {

  @Autowired
  private WishMapper wishMapper;
  
  //@Test
  public void testInsertWish() {
      // 테스트할 데이터 생성
      WishDto wishDto = new WishDto();
      wishDto.setIsbn("9771228448004"); // ISBN 값 설정
      wishDto.setUserNo(1); // 사용자 번호 설정

      long currentTime = System.currentTimeMillis();
      Timestamp currentTimestamp = new Timestamp(currentTime);
      wishDto.setWishDate(currentTimestamp);

      // 데이터베이스에 삽입
      wishMapper.insertWish(wishDto);

  }
  
  @Test
  public void testWishExists() {
    // Given
    WishDto wishDto = new WishDto();
    wishDto.setIsbn("9772765332009");
    wishDto.setUserNo(1);

    // When
    int result = wishMapper.wishExists(wishDto);

    System.out.println(result);
  }
  
}
