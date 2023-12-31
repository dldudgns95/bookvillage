package kr.co.bookvillage.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.bookvillage.dao.NoticeMapper;
import kr.co.bookvillage.dto.AttachNtDto;
import kr.co.bookvillage.dto.NoticeDto;
import kr.co.bookvillage.dto.UserDto;
import kr.co.bookvillage.util.AdminPageUtils;
import kr.co.bookvillage.util.MyFileUtils;
import kr.co.bookvillage.util.MyPageUtils;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;

@Transactional
@RequiredArgsConstructor
@Service
public class NoticeServiceImpl implements NoticeService {

  private final NoticeMapper noticeMapper;
  private final MyFileUtils myFileUtils;
  private final MyPageUtils myPageUtils;
  private final AdminPageUtils adminPageUtils;

  @Override
  public void getSearchNoticeList(HttpServletRequest request, Model model) {
	  String column = request.getParameter("column");
	  String query = request.getParameter("query");
	  Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
	    int page = Integer.parseInt(opt.orElse("1"));
	    int total = noticeMapper.noticeSearchCount(Map.of("column", column, "query", query));
	    int display = 10;
	    
	    adminPageUtils.setPaging(page, total, display);
	    Map<String, Object> map = Map.of("begin", adminPageUtils.getBegin()
	                                   , "end", adminPageUtils.getEnd()
	                                   , "column", column
	                                   , "query", query);
	    
	    model.addAttribute("noticeList", noticeMapper.getSearchNoticeList(map));
	    model.addAttribute("paging", adminPageUtils.getMvcPaging(request.getContextPath() + "/support/noticeSearch.do", "column=" + column + "&query=" + query));
	    model.addAttribute("beginNo", total - (page - 1) * display);
	    model.addAttribute("totalCount", total);
	  
  }
  
  @Override
  public int addNotice(MultipartHttpServletRequest multipartRequest) throws Exception {
    
	HttpSession session = multipartRequest.getSession();
	int userNo = ((UserDto)session.getAttribute("user")).getUserNo();
	  
    String ntTitle = multipartRequest.getParameter("ntTitle");
    String ntContent = multipartRequest.getParameter("ntContent");
    int checkStatus = Integer.parseInt(multipartRequest.getParameter("checkStatus"));
    
    NoticeDto notice = NoticeDto.builder()
                        .ntTitle(ntTitle)
                        .ntContent(ntContent)
                        .userDto(UserDto.builder()
                                  .userNo(userNo)
                                  .build())
                        .build();
    
    int addResult = noticeMapper.insertNotice(notice);
    
    List<MultipartFile> files = multipartRequest.getFiles("files");
    
    int attachCount;
    if(files.get(0).getSize() == 0) {
      attachCount = 1;
    } else {
      attachCount = 0;
    }
    
    for(MultipartFile multipartFile : files) {
        if(multipartFile != null && !multipartFile.isEmpty()) {
          String path = "";
          if(checkStatus == 0) {
             path = myFileUtils.getNoticeWindowPath();		
            } else if(checkStatus == 1) {
             path = myFileUtils.getNoticePath();
            }   
          
          File dir = new File(path);
          if(!dir.exists()) {
            dir.mkdirs();
          }
        
        String ntOriginalFilename = multipartFile.getOriginalFilename();
        String ntFilesystemName = myFileUtils.getFilesystemName(ntOriginalFilename);
        
        System.out.println("path:" + path);
        System.out.println("ntOriginalFilename:" + ntOriginalFilename);
        System.out.println("ntFilesystemName:" + ntFilesystemName);
       
        String url = path + "/" + ntFilesystemName;
        
        Path paths = Paths.get(url).toAbsolutePath();
        
        File file = new File(dir, ntFilesystemName);
        
        System.out.println("file :" + file);
        	multipartFile.transferTo(paths.toFile());
        
        
        String contentType = Files.probeContentType(paths);  // 이미지의 Content-Type은 image/jpeg, image/png 등 image로 시작한다.
        int ntHasThumbnail = (contentType != null && contentType.startsWith("image")) ? 1 : 0;
        
        if(ntHasThumbnail == 1) {
          File thumbnail = new File(dir, "s_" + ntFilesystemName);  // small 이미지를 의미하는 s_을 덧붙임
          Thumbnails.of(file)
                    .size(100, 100)      // 가로 100px, 세로 100px
                    .toFile(thumbnail);
        }
        
        AttachNtDto attachNt = AttachNtDto.builder()
                            .ntPath(path)
                            .ntOriginalFilename(ntOriginalFilename)
                            .ntFilesystemName(ntFilesystemName)
                            .ntHasThumbnail(ntHasThumbnail)
                            .build();
        System.out.println("attachNt: " + attachNt);
        attachCount += noticeMapper.insertAttach(attachNt);
        
      }  
      
    }
    return addResult;
        
  }
  

  @Transactional(readOnly=true)
  @Override
  public void loadNoticeList(HttpServletRequest request, Model model) {
  
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = noticeMapper.getNoticeCount();
    int display = 10;
    
    adminPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", adminPageUtils.getBegin()
                                   , "end", adminPageUtils.getEnd());
    
    List<NoticeDto> noticeList = noticeMapper.getNoticeList(map);
    
    model.addAttribute("noticeList", noticeList);
    model.addAttribute("paging", adminPageUtils.getMvcPaging(request.getContextPath() + "/support/list.do"));
    model.addAttribute("beginNo", total - (page - 1) * display);
    
  }
  
  @Override
  public ResponseEntity<Resource> download(HttpServletRequest request) {
    
    // 첨부 파일의 정보 가져오기
    int attachNtNo = Integer.parseInt(request.getParameter("attachNtNo"));
    AttachNtDto attach = noticeMapper.getAttach(attachNtNo);
    
    // 첨부 파일 File 객체 -> Resource 객체
    File file = new File(attach.getNtPath(), attach.getNtFilesystemName());
    Resource resource = new FileSystemResource(file);
    
    // 첨부 파일이 없으면 다운로드 취소
    if(!resource.exists()) {
      return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
    }
    
    // 다운로드 횟수 증가하기
    noticeMapper.updateDownloadCount(attachNtNo);
    
    // 사용자가 다운로드 받을 파일의 이름 결정 (User-Agent값에 따른 인코딩 처리)
    String ntOriginalFilename = attach.getNtOriginalFilename();
    String userAgent = request.getHeader("User-Agent");
    try {
      // IE
      if(userAgent.contains("Trident")) {
    	  ntOriginalFilename = URLEncoder.encode(ntOriginalFilename, "UTF-8").replace("+", " ");
      }
      // Edge
      else if(userAgent.contains("Edg")) {
    	  ntOriginalFilename = URLEncoder.encode(ntOriginalFilename, "UTF-8");
      }
      // Other
      else {
    	  ntOriginalFilename = new String(ntOriginalFilename.getBytes("UTF-8"), "ISO-8859-1");
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
    
    // 다운로드 응답 헤더 만들기
    HttpHeaders header = new HttpHeaders();
    header.add("Content-Type", "application/octet-stream");
    header.add("Content-Disposition", "attachment; filename=" + ntOriginalFilename);
    header.add("Content-Length", file.length() + "");
    
    // 응답
    return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
    
  }
  @Transactional(readOnly=true)
  @Override
  public void loadNotice(HttpServletRequest request, Model model) {
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("ntNo"));
    int uploadNo = Integer.parseInt(opt.orElse("0"));
    
    model.addAttribute("notice", noticeMapper.getNotice(uploadNo));
    model.addAttribute("attachList", noticeMapper.getAttachList(uploadNo));
    
  }
    
  @Override
  public ResponseEntity<Resource> downloadAll(HttpServletRequest request) {
    
    // 다운로드 할 모든 첨부 파일 정보 가져오기
    int ntNo = Integer.parseInt(request.getParameter("ntNo"));
    List<AttachNtDto> attachList = noticeMapper.getAttachList(ntNo);
    
    // 첨부 파일이 없으면 종료
    if(attachList.isEmpty()) {
      return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
    }
    
    // zip 파일을 생성할 경로
    File tempDir = new File(myFileUtils.getTempPath());
    if(!tempDir.exists()) {
      tempDir.mkdirs();
    }
    
    // zip 파일의 이름
    String zipName = myFileUtils.getTempFilename() + ".zip";
    
    // zip 파일의 File 객체
    File zipFile = new File(tempDir, zipName);
    
    // zip 파일을 생성하는 출력 스트림
    ZipOutputStream zout = null;
    
    // 첨부 파일들을 순회하면서 zip 파일에 등록하기
    try {
      
      zout = new ZipOutputStream(new FileOutputStream(zipFile));
      
      for(AttachNtDto attach : attachList) {
        
        // 각 첨부 파일들의 원래 이름으로 zip 파일에 등록하기 (이름만 등록)
        ZipEntry zipEntry = new ZipEntry(attach.getNtOriginalFilename());
        zout.putNextEntry(zipEntry);
        
        // 각 첨부 파일들의 내용을 zip 파일에 등록하기 (실제 파일 등록)
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(new File(attach.getNtPath(), attach.getNtFilesystemName())));
        zout.write(bin.readAllBytes());
        
        // 자원 반납
        bin.close();
        zout.closeEntry();
        
        // 다운로드 횟수 증가
        noticeMapper.updateDownloadCount(attach.getAttachNtNo());
        
      }
      
      // zout 자원 반납
      zout.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    // 다운로드할 zip 파일의 File 객체 -> Resource 객체
    Resource resource = new FileSystemResource(zipFile);
    
    // 다운로드 응답 헤더 만들기
    HttpHeaders header = new HttpHeaders();
    header.add("Content-Type", "application/octet-stream");
    header.add("Content-Disposition", "attachment; filename=" + zipName);
    header.add("Content-Length", zipFile.length() + "");
    
    // 응답
    return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
    
  }
  
  @Override
  public void removeTempFiles() {
    File tempDir = new File(myFileUtils.getTempPath());
    File[] targetList = tempDir.listFiles();
    if(targetList != null) {
      for(File target : targetList) {
        target.delete();
      }
    }
  }
  
  @Transactional(readOnly=true)
  @Override
  public NoticeDto getNotice(int ntNo) {
    return noticeMapper.getNotice(ntNo);
  }
  
  @Override
  public int modifyNotice(NoticeDto notice) {
    return noticeMapper.updateNotice(notice);
  }
  
  @Override
  public Map<String, Object> getAttachList(HttpServletRequest request) {
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("ntNo"));
    int ntNo = Integer.parseInt(opt.orElse("0"));
    
    return Map.of("attachList", noticeMapper.getAttachList(ntNo));
    
  }
  
  @Override
  public Map<String, Object> removeAttach(HttpServletRequest request) {
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("attachNtNo"));
    int attachNtNo = Integer.parseInt(opt.orElse("0"));
    
    // 파일 삭제
    AttachNtDto  attach = noticeMapper.getAttach(attachNtNo);
    File file = new File(attach.getNtPath(), attach.getNtFilesystemName());
    if(file.exists()) {
      file.delete();
    }
    
    // 썸네일 삭제
    if(attach.getNtHasThumbnail() == 1) {
      File thumbnail = new File(attach.getNtPath(), "s_" + attach.getNtFilesystemName());
      if(thumbnail.exists()) {
        thumbnail.delete();
      }
    }
    
    // ATTACH_T 삭제
    int removeResult = noticeMapper.deleteAttach(attachNtNo);
    
    return Map.of("removeResult", removeResult);
    
  }
  
  @Override
  public Map<String, Object> addAttach(MultipartHttpServletRequest multipartRequest) throws Exception {
    
    List<MultipartFile> files =  multipartRequest.getFiles("files");
    int ntNo = Integer.parseInt(multipartRequest.getParameter("ntNo"));
    int checkStatus = Integer.parseInt(multipartRequest.getParameter("checkStatus"));
    
    int attachCount;
    if(files.get(0).getSize() == 0) {
      attachCount = 1;
    } else {
      attachCount = 0;
    }

    for(MultipartFile multipartFile : files) {
        if(multipartFile != null && !multipartFile.isEmpty()) {
          String path = "";
          if(checkStatus == 0) {
            path = myFileUtils.getNoticeWindowPath();
          } else if(checkStatus == 1) {
            path = myFileUtils.getNoticePath();
          }
          File dir = new File(path);
          if(!dir.exists()) {
            dir.mkdirs();
          }
        
        String ntOriginalFilename = multipartFile.getOriginalFilename();
        String ntFilesystemName = myFileUtils.getFilesystemName(ntOriginalFilename);

        String url = path + "/" + ntFilesystemName;
        
        Path paths = Paths.get(url).toAbsolutePath();
        
        File file = new File(dir, ntFilesystemName);
        
        multipartFile.transferTo(paths.toFile());
        
        String contentType = Files.probeContentType(paths);  // 이미지의 Content-Type은 image/jpeg, image/png 등 image로 시작한다.
        int ntHasThumbnail = (contentType != null && contentType.startsWith("image")) ? 1 : 0;
        
        if(ntHasThumbnail == 1) {
          File thumbnail = new File(dir, "s_" + ntFilesystemName);  // small 이미지를 의미하는 s_을 덧붙임
          Thumbnails.of(file)
                    .size(100, 100)      // 가로 100px, 세로 100px
                    .toFile(thumbnail);
        }
        
        AttachNtDto attach = AttachNtDto.builder()
                            .ntPath(path)
                            .ntOriginalFilename(ntOriginalFilename)
                            .ntFilesystemName(ntFilesystemName)
                            .ntHasThumbnail(ntHasThumbnail)
                            .ntNo(ntNo)
                            .build();
        
        attachCount += noticeMapper.modifyAttach(attach);
        
      }  // if
      
    }  // for
    
    return Map.of("attachResult", files.size() == attachCount);
    
  }
  
  @Override
  public int removeNotice(int ntNo) {
    
    // 파일 삭제
    List<AttachNtDto> attachList = noticeMapper.getAttachList(ntNo);
    for(AttachNtDto attach : attachList) {
      
      File file = new File(attach.getNtPath(), attach.getNtFilesystemName());
      if(file.exists()) {
        file.delete();
      }
      
      if(attach.getNtHasThumbnail() == 1) {
        File thumbnail = new File(attach.getNtPath(), "s_" + attach.getNtFilesystemName());
        if(thumbnail.exists()) {
          thumbnail.delete();
        }
      }
      
    }
    
    // UPLOAD_T 삭제
    return noticeMapper.deleteNotice(ntNo);
    
  }
  @Override
	public void noticeImageBatch() {

	  // 1. 어제 작성된 블로그의 이미지 목록 (DB)
	    List<AttachNtDto> noticeImageList = noticeMapper.getNoticeImageInYesterday();
	    
	    // 2. List<BlogImageDto> -> List<Path> (Path는 경로+파일명으로 구성)
	    List<Path> noticeImagePathList = noticeImageList.stream()
	                                                .map(AttachNtDto -> new File(AttachNtDto.getNtPath(), AttachNtDto.getNtFilesystemName()).toPath())
	                                                .collect(Collectors.toList());
	    
	    // 3. 어제 저장된 블로그 이미지 목록 (디렉토리)
	    File dir = new File(myFileUtils.getNoticeImagePathInYesterday());
	    
	    // 4. 삭제할 File 객체들
	    File[] targets = dir.listFiles(file -> !noticeImagePathList.contains(file.toPath()));

	    // 5. 삭제
	    if(targets != null && targets.length != 0) {
	      for(File target : targets) {
	        target.delete();
	      }
	    }
	    
	}
}
