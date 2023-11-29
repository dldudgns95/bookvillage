package kr.co.bookvillage.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.bookvillage.dto.NoticeDto;

public interface NoticeService {

	public boolean addNotice(MultipartHttpServletRequest multipartRequest) throws Exception;
	public Map<String, Object> getNoticeList(HttpServletRequest request);
	public void loadNotice(HttpServletRequest request, Model model);
	public ResponseEntity<org.springframework.core.io.Resource> download(HttpServletRequest request);
	public ResponseEntity<org.springframework.core.io.Resource> downloadAll(HttpServletRequest request);
	public void removeTempFiles();
	public NoticeDto getNotice(int ntNo);
	public int modifyNotice(NoticeDto notice);
	public Map<String, Object> getAttachList(HttpServletRequest request);
	public Map<String, Object> removeAttach(HttpServletRequest request);
	public Map<String, Object> addAttach(MultipartHttpServletRequest multipartRequest) throws Exception;
	public int removeNotice(int ntNo);

}
 