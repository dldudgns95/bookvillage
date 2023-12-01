package kr.co.bookvillage.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.bookvillage.dto.AttachFacDto;
import kr.co.bookvillage.dto.FacilityDto;

@Mapper
public interface FacMapper {

	public List<FacilityDto> getFacList();
	public List<AttachFacDto> availableFacList(String facStart);
	public List<AttachFacDto> unavailableFacList(String facStart);
	public int addFacApply(Map<String, Object> map);
	public boolean checkFacApply(Map<String, Object> map);
}
