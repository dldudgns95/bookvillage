package kr.co.bookvillage.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.bookvillage.dto.AskDto;
import kr.co.bookvillage.dto.AskImageDto;

public interface CommentService {
  
  public Map<String, Object> imageUpload(MultipartHttpServletRequest multipartRequest);
  public int addAsk(HttpServletRequest request); 
  
  public List<String> getEditorImageList(String contents);
  
  // public void blogImageBatch();
  
  public void loadAskList(HttpServletRequest request, Model model);
  public int increseHit(int askNo);
  public AskDto getAsk(int askNo);
  public int modifyAsk(HttpServletRequest request);
  public int removeAsk(int askNo);
  
  public Map<String, Object> addAns(HttpServletRequest request);
  public Map<String, Object> loadListAns(HttpServletRequest request);
  public Map<String, Object> addAnsReply(HttpServletRequest request);
  public Map<String, Object> removeAns(int ansNo);
  
}
