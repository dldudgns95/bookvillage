package kr.co.bookvillage.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.bookvillage.dao.CommentMapper;
import kr.co.bookvillage.dto.AnswerDto;
import kr.co.bookvillage.dto.AskDto;
import kr.co.bookvillage.dto.AskImageDto;
import kr.co.bookvillage.dto.UserDto;
import kr.co.bookvillage.util.AdminPageUtils;
import kr.co.bookvillage.util.CommentMyFileUtils;
import kr.co.bookvillage.util.MyPageUtils;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

   private final CommentMyFileUtils commentMyFileUtils;
   private final CommentMapper commentMapper;
   private final MyPageUtils myPageUtils;
   private final AdminPageUtils adminPageUtils;
   
   @Override
  public Map<String, Object> imageUpload(MultipartHttpServletRequest multipartRequest) {

     
     // 이미지가 저장될 경로
     String imagePath = commentMyFileUtils.getAskImagePath();
     //String imagePath = commentMyFileUtils.getUploadPath();
     File dir = new File(imagePath);
     if(!dir.exists()) {
       dir.mkdirs();
     }
     
     MultipartFile upload = multipartRequest.getFile("upload");
     
     String originalFileName = upload.getOriginalFilename();
     String filesystemName = commentMyFileUtils.getFilesystemName(originalFileName);
     
     File file = new File(dir, filesystemName);
     
     
     try {
       upload.transferTo(file);
    } catch (Exception e) {
      e.printStackTrace();
    }
     
     return Map.of("uploaded", true
                  , "url", multipartRequest.getContextPath() + imagePath + "/" + filesystemName);
     
  }

   // 작성
  @Override
  public int addAsk(HttpServletRequest request) {
    
    String askTitle = request.getParameter("askTitle");
    String askContent = request.getParameter("askContent");
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    
    AskDto ask = AskDto.builder()
                    .askTitle(askTitle)
                    .askContent(askContent)
                    .userDto(UserDto.builder()
                        .userNo(userNo)
                        .build())
                    .build();
    
    
    int addResult = commentMapper.insertAsk(ask);
    
    for(String editorImage : getEditorImageList(askContent)) {
      AskImageDto askImage = AskImageDto.builder()
                        .askNo(ask.getAskNo())
                        .imagePath(commentMyFileUtils.getAskImagePath())
                        .filesystemName(editorImage)
                        .build();
      commentMapper.insertImg(askImage);
    }
    
    
  return addResult;
    
  }
  
  
  @Override
    public List<String> getEditorImageList(String askContent) {
    
    List<String> editorImageList  = new ArrayList<>();
    
    Document document = Jsoup.parse(askContent);
    Elements elements = document.getElementsByTag("img");
    
    if(elements != null) {
      for(Element element : elements) {
        String src = element.attr("src");
        String filesystemName = src.substring(src.lastIndexOf("/") + 1);
        editorImageList.add(filesystemName);
      }
    }

    return editorImageList;
    }
  
  // 목록 불러오기
  @Override
  public void loadAskList(HttpServletRequest request, Model model) {

    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = commentMapper.getAskCount();
    int display = 10;
    
    adminPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", adminPageUtils.getBegin()
                                   , "end", adminPageUtils.getEnd());
    
    List<AskDto> askList = commentMapper.getAskList(map);
    
    model.addAttribute("askList", askList);
    model.addAttribute("paging", adminPageUtils.getMvcPaging("/comment/commentList.do"));
    model.addAttribute("beginNo", total - (page - 1) * display);
    
  }
  
  @Override
  public int increseHit(int askNo) {
    return commentMapper.updateHit(askNo);
  }
  
  @Transactional(readOnly = true)
  @Override
  public AskDto getAsk(int askNo) {
    return commentMapper.getAsk(askNo);
  }
  
  @Override
  public int modifyAsk(HttpServletRequest request) {

    String askTitle = request.getParameter("askTitle");
    String askContent = request.getParameter("askContent");
    int askNo = Integer.parseInt(request.getParameter("askNo"));
        
    List<AskImageDto> askImgDtoList = commentMapper.getAskImgList(askNo);
    List<String> askImgList = askImgDtoList.stream()
                                .map(AskImageDto -> AskImageDto.getFilesystemName())
                                .collect(Collectors.toList());
    
    List<String> editorImageList = getEditorImageList(askContent);

    editorImageList.stream()
    .filter(editorImage -> !askImgList.contains(editorImage))         // 조건 : Editor에 포함되어 있으나 기존 이미지에 포함되어 있지 않다.
    .map(editorImage -> AskImageDto.builder()                           // 변환 : Editor에 포함된 이미지 이름을 BlogImageDto로 변환한다.
                          .askNo(askNo)
                          .imagePath(commentMyFileUtils.getAskImagePath())
                          .filesystemName(editorImage)
                          .build())
    .forEach(askImageDto -> commentMapper.insertImg(askImageDto));  // 순회 : 변환된 BlogImageDto를 BLOG_IMAGE_T에 추가한다.
  
    List<AskImageDto> removeList = askImgDtoList.stream()
        .filter(askImageDto -> !editorImageList.contains(askImageDto.getFilesystemName()))  // 조건 : 기존 이미지 중에서 Editor에 포함되어 있지 않다.
        .collect(Collectors.toList());                                                        // 조건을 만족하는 blogImageDto를 리스트로 반환한다.

    for(AskImageDto askImageDto : removeList) {
      
      commentMapper.deleteAskImage(askImageDto.getFilesystemName());
      // 파일 삭제
      File file = new File(askImageDto.getImagePath(), askImageDto.getFilesystemName());
      if(file.exists()) {
        file.delete();
      }
    }
    
    AskDto ask = AskDto.builder()
                    .askTitle(askTitle)
                    .askContent(askContent)
                    .askNo(askNo)
                    .build();
    
    int modifyResult = commentMapper.updateAsk(ask);
    
    return modifyResult;
  }
  
  
  // 삭제
  @Override
  public int removeAsk(int askNo) {

    List<AskImageDto> askImageList = commentMapper.getAskImgList(askNo);
    for(AskImageDto askImage : askImageList) {
      File file = new File(askImage.getImagePath(), askImage.getFilesystemName());
      if(file.exists()) {
        file.delete();
      }
    }
    
    commentMapper.deleteImageList(askNo); 
    
    return commentMapper.deleteAsk(askNo);
  }
  
  
  
  // 댓글 삽입 + 답변 완료
  @Transactional
  @Override
  public Map<String, Object> addtAnswer(HttpServletRequest request) {
    
    String ansContent = request.getParameter("ansContent");
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    int askNo = Integer.parseInt(request.getParameter("askNo"));
    
    AnswerDto ans = AnswerDto.builder()
                       .ansContent(ansContent)
                       .userDto(UserDto.builder()
                           .userNo(userNo)
                           .build())
                       .askNo(askNo)
                       .build();
                       
    int addAnswerResult = commentMapper.insertAns(ans);
    
    commentMapper.updateAnswerStatus(ans);
    
    return Map.of("addAnswerResult", addAnswerResult);
  }
  
  
  @Override
  public List<AnswerDto> getAnsList() {
    return commentMapper.AnsList();
  }
  
  
  
  
}
