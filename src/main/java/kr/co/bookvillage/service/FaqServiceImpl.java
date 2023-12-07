package kr.co.bookvillage.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import kr.co.bookvillage.dao.FaqMapper;
import kr.co.bookvillage.dto.FaqDto;
import kr.co.bookvillage.util.AdminPageUtils;
import kr.co.bookvillage.util.MyPageUtils;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class FaqServiceImpl implements FaqService {
	
	private final FaqMapper faqMapper;
	private final MyPageUtils myPageUtils;
	private final AdminPageUtils adminPageUtils;	
	
	  @Override
	  public void getSearchFaqList(HttpServletRequest request, Model model) {
		  String column = request.getParameter("column");
		  String query = request.getParameter("query");
		  Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
		    int page = Integer.parseInt(opt.orElse("1"));
		    int total = faqMapper.faqSearchCount(Map.of("column", column, "query", query));
		    int display = 10;
		    
		    adminPageUtils.setPaging(page, total, display);
		    Map<String, Object> map = Map.of("begin", adminPageUtils.getBegin()
		                                   , "end", adminPageUtils.getEnd()
		                                   , "column", column
		                                   , "query", query);
		    
		    model.addAttribute("faqList", faqMapper.getSearchFaqList(map));
		    model.addAttribute("paging", adminPageUtils.getMvcPaging(request.getContextPath() + "/support/faqSearch.do", "column=" + column + "&query=" + query));
		    model.addAttribute("beginNo", total - (page - 1) * display);
		    model.addAttribute("totalCount", total);
		  
	  }
	  
	 @Transactional(readOnly=true)
	  @Override
	  public void loadFaqList(HttpServletRequest request, Model model) {
	  
	    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
	    int page = Integer.parseInt(opt.orElse("1"));
	    int total = faqMapper.getFaqCount();
	    int display = 10;
	    
	    adminPageUtils.setPaging(page, total, display);
	    
	    Map<String, Object> map = Map.of("begin", adminPageUtils.getBegin()
	                                   , "end", adminPageUtils.getEnd());
	    
	    List<FaqDto> faqList = faqMapper.getFaqList(map);
	    
	    model.addAttribute("faqList", faqList);
	    model.addAttribute("paging", adminPageUtils.getMvcPaging(request.getContextPath() + "/support/faqlist.do"));
	    model.addAttribute("beginNo", total - (page - 1) * display);
	    
	  }
	 
	@Override
	public int addFaq(FaqDto faqDto) {
		int addResult = faqMapper.insertFaq(faqDto);
		return addResult;
	}
	
	@Transactional(readOnly=true)
	@Override
	public void loadFaq(HttpServletRequest request, Model model) {
		
	    Optional<String> opt = Optional.ofNullable(request.getParameter("faqNo"));
	    int faqNo = Integer.parseInt(opt.orElse("0"));
	    
	    model.addAttribute("faq", faqMapper.getFaq(faqNo));	    		
		
	}
	@Override
	public int removeFaq(int faqNo) {
		return faqMapper.deleteFaq(faqNo);
	}
	
	@Transactional(readOnly=true)
	@Override
	public FaqDto getFaq(int faqNo) {
		return faqMapper.getFaq(faqNo);
	}
	
	@Override
	public int modifyFaq(FaqDto faq) {
		return faqMapper.updateFaq(faq);
	}
}
