package kr.co.bookvillage.service;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface NoticeService {

	public boolean addNotice(MultipartHttpServletRequest multipartRequest) throws Exception;

}
