package kr.co.bookvillage.dao;

import java.util.Map;

import kr.co.bookvillage.dto.InactiveUserDto;
import kr.co.bookvillage.dto.UserDto;

public interface UserMapper {
  
  public UserDto getUser(Map<String, Object> map);
  public int insertAccess(String email);
  
  public InactiveUserDto getInactiveUser(Map<String, Object> map);
  
  public int insertUsesr(UserDto user);
  
  public String findId(String name, String mobile);
  
}
