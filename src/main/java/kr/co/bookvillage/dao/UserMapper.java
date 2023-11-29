package kr.co.bookvillage.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.bookvillage.dto.InactiveUserDto;
import kr.co.bookvillage.dto.UserDto;

@Mapper
public interface UserMapper {
  
  public UserDto getUser(Map<String, Object> map);
  public int insertAccess(String email);
  
  public InactiveUserDto getInactiveUser(Map<String, Object> map);
  
  public int insertUsesr(UserDto user);
  
  
  public UserDto findId(String name, String mobile);
  //휴면회원
  public UserDto findIdInactiveUser(Map<String, Object> map);
  
  public int insertNaverUser(UserDto user);
  
  // 임시 비밀번호 받으면 업데이트
  public int updatetmpPw(Map<String, Object> map);
  public int tmpPwMdDay(UserDto user);

  
}

