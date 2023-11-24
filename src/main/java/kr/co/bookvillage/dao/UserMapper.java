package kr.co.bookvillage.dao;

import java.util.Map;

import kr.co.bookvillage.dto.UserDto;

public interface UserMapper {
  
  public UserDto getUser(Map<String, Object> map);
  


}
