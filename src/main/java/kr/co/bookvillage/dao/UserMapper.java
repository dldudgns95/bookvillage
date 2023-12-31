package kr.co.bookvillage.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.bookvillage.dto.AttachFacDto;
import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.FaqDto;
import kr.co.bookvillage.dto.InactiveUserDto;
import kr.co.bookvillage.dto.NoticeDto;
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
  //public String updatetmpPw(Map<String, Object> map);
  
  public int tmpPwMdDay(UserDto user);
  
  public UserDto changePw90(String email);
  
  public int updatePw90(UserDto user);
  
  public int kakaoJoin(UserDto user);
  public int autoupdatetmpPw(UserDto user);
  
  public List<FaqDto> getFaqList();
  public List<NoticeDto> getNoticeList();
  
  public List<BookDto> getBookList();
  
  // 휴면 회원처리
  public int insertInactiveUser();
  public int deleteUserForInactive();
  public int insertActiveUser(String email);
  public int deleteInactiveUser(String email);
  
  //시설
  public List<AttachFacDto> getFacList();

  
  
}

