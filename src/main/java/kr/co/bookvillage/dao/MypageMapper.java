package kr.co.bookvillage.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.bookvillage.dto.UserDto;

@Mapper
public interface MypageMapper {
  public UserDto getUser(Map<String, Object> map);
  public int updateUser(UserDto userDto);
  public int updateUserPw(UserDto userDto);
}
