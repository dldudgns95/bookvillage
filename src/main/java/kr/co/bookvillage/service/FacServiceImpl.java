package kr.co.bookvillage.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.bookvillage.dao.FacMapper;
import kr.co.bookvillage.dto.FacilityDto;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class FacServiceImpl implements FacService {
	private final FacMapper facMapper;
	
	@Transactional(readOnly=true)
	@Override
	public List<FacilityDto> getFacList() {
		return facMapper.getFacList();
	}
	
	
}
