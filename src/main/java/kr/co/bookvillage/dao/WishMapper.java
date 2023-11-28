package kr.co.bookvillage.dao;

import org.apache.ibatis.annotations.Mapper;

import kr.co.bookvillage.dto.WishDto;

@Mapper
public interface WishMapper {

  public void insertWish(WishDto wishDto);
  
}
