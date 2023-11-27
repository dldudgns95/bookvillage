package kr.co.bookvillage.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.bookvillage.dto.NoticeDto;

public interface NoticeService {

	public boolean addNotice(MultipartHttpServletRequest multipartRequest) throws Exception;
	public Map<String, Object> getNoticeList(HttpServletRequest request);
	public void loadNotice(HttpServletRequest request, Model model);
	public NoticeDto getNotice(int ntNo);

}
 