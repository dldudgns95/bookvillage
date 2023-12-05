package kr.co.bookvillage.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.bookvillage.dto.WishDto;

@Mapper
public interface WishMapper {

  public int wishExists(WishDto wishDto);
  public void insertWish(WishDto wishDto);
  public void deleteWish(WishDto wishDto);
  public void getMyWishList(String userNo);
}
