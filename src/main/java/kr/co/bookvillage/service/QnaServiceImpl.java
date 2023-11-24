package kr.co.bookvillage.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public interface QnaServiceImpl extends QnaService {
	/*
	//private final QnaMapper qnaMapper;
	//private final MyPageUtils myPageUtils;
	
	// 목록보기 + 페이징
	@Transactional(readOnly=true)
	@Override
	public void loadQnaList(HttpServletRequest request, Model model) {
		
		// 페이지 정보 
		Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
	    int page = Integer.parseInt(opt.orElse("1"));  // 페이지 번호가 전달되지 않았을때 1로 전달
	    
	    int display = 10;
	    
	    int total = qnaMapper.getQnaCount();  // 게시글의 개수 구하기
	}
 */
}