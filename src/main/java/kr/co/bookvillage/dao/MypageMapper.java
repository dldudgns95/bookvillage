package kr.co.bookvillage.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.bookvillage.dto.UserDto;

@Mapper
public interface MypageMapper {
  public UserDto getUser(Map<String, Object> map);  // 사용자정보 가져오기
  public int updateUser(UserDto userDto);           // 사용자정보 수정
  public int updateUserPw(UserDto userDto);         // 비밀번호 수정
}
