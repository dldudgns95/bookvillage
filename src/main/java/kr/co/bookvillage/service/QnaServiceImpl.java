package kr.co.bookvillage.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.bookvillage.dao.QnaMapper;
import kr.co.bookvillage.dto.AttachAskDto;
import kr.co.bookvillage.dto.QnaDto;
import kr.co.bookvillage.dto.UserDto;
import kr.co.bookvillage.util.MyFileUtils;
import kr.co.bookvillage.util.MyPageUtils;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;

@Transactional
@RequiredArgsConstructor
@Service
public class QnaServiceImpl implements QnaService {
	private QnaMapper qnaMapper;
	private final MyFileUtils myFileUtils;
	private final MyPageUtils myPageUtils;	
	
	
}
