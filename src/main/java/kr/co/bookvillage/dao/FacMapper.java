package kr.co.bookvillage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.bookvillage.dto.FacilityDto;

@Mapper
public interface FacMapper {

	public List<FacilityDto> getFacList();

}
