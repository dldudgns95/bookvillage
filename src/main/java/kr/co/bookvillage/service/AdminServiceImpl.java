package kr.co.bookvillage.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.bookvillage.dao.AdminMapper;
import kr.co.bookvillage.dto.AttachFacDto;
import kr.co.bookvillage.dto.BookDto;
import kr.co.bookvillage.dto.FacilityDto;
import kr.co.bookvillage.util.AdminFileUtils;
import kr.co.bookvillage.util.AdminPageUtils;
import kr.co.bookvillage.util.MyFileUtils;
import kr.co.bookvillage.util.MyPageUtils;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;

@Transactional
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
  
  private final AdminMapper adminMapper;
  private final MyPageUtils myPageUtils;
  private final AdminPageUtils adminPageUtils;
  private final AdminFileUtils adminFileUtils;

  @Override
  public int insertBook(HttpServletRequest request){
    
    
    String apiURL = "http://www.aladin.co.kr/ttb/api/ItemList.aspx";
    String ttbkey = "ttbalsltksxk2011001";
    String QueryType = request.getParameter("QueryType");
    int count = 0;
    
    for(int i = 1; i <= 6; i++) {
      
      StringBuilder sb = new StringBuilder();
      sb.append(apiURL);
      sb.append("?ttbkey=" + ttbkey);
      sb.append("&QueryType=" + QueryType);
      sb.append("&MaxResults=50");
      sb.append("&Start=" + i);
      sb.append("&SearchTarget=Book");
      sb.append("&output=JS");
      sb.append("&Cover=Big");
      sb.append("&Version=20131101");
      
      JSONObject obj = null;
      try {
        // 요청
        URL url = new URL(sb.toString());
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("GET");  // 반드시 대문자로 작성
        
        // 응답
        BufferedReader reader = null;
        int responseCode = con.getResponseCode();
        if(responseCode == 200) {
          reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
          reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }
        String line = null;
        StringBuilder responseBody = new StringBuilder();
        while ((line = reader.readLine()) != null) {
          responseBody.append(line);
        }
        obj = new JSONObject(responseBody.toString());
      } catch (Exception e) {
        e.printStackTrace();
      }
      
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      
      JSONArray array = (JSONArray)obj.get("item");
      
      // for문으로 값 하나씩 insert하기
      for(Object lists : array) {
        try {
          JSONObject list = (JSONObject)lists;
          String pubdate = list.getString("pubDate");
          Date date = java.sql.Date.valueOf(pubdate);
          BookDto bookDto = BookDto.builder()
              .isbn(list.getString("isbn13"))
              .title(list.getString("title"))
              .cover(list.getString("cover"))
              .author(list.getString("author"))
              .publisher(list.getString("publisher"))
              .pubdate(date)
              .description(list.getString("description"))
              .categoryName(list.getString("categoryName"))
              .categoryId(list.getInt("categoryId"))
              .build();
          System.out.println(bookDto);
          adminMapper.insertBook(bookDto);
        } catch (Exception e) {
          // e.printStackTrace();
          count--;
        } finally {
          count++;
        }
      }
    }
    
    
    
    return count;
  }
  
  @Override
  public void getUserList(HttpServletRequest request, Model model) {
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = adminMapper.userTotalCount();
    int display = 10;
    
    adminPageUtils.setPaging(page, total, display);
    Map<String, Object> map = Map.of("begin", adminPageUtils.getBegin(), "end", adminPageUtils.getEnd());
    
    model.addAttribute("userList", adminMapper.getUserList(map));
    model.addAttribute("paging", adminPageUtils.getMvcPaging(request.getContextPath() + "/admin/userList.do"));
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("totalCount", total);
    
  }
  
  @Override
  public void getUserDetail(HttpServletRequest request, Model model) {
    
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    model.addAttribute("user", adminMapper.getUserDetail(userNo));
    model.addAttribute("bookCheckoutList", adminMapper.getUserBookCheckoutList(userNo));
    model.addAttribute("facApplyList", adminMapper.getUserFacApplyList(userNo));
    model.addAttribute("bookApplyList", adminMapper.getUserBookApplyList(userNo));
    
  }
  
  @Override
  public int deleteUser(HttpServletRequest request) {
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    return adminMapper.deleteUser(userNo);
  }
  
  @Override
  public void getSearchUserList(HttpServletRequest request, Model model) {
    
    String column = request.getParameter("column");
    String query = request.getParameter("query");
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = adminMapper.userSearchCount(Map.of("column", column, "query", query));
    int display = 10;
    
    adminPageUtils.setPaging(page, total, display);
    Map<String, Object> map = Map.of("begin", adminPageUtils.getBegin()
                                   , "end", adminPageUtils.getEnd()
                                   , "column", column
                                   , "query", query);
    
    model.addAttribute("userList", adminMapper.getSearchUserList(map));
    model.addAttribute("paging", adminPageUtils.getMvcPaging(request.getContextPath() + "/admin/userSearch.do", "column=" + column + "&query=" + query));
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("totalCount", total);
    
  }
  
  @Override
  public void getBookList(HttpServletRequest request, Model model) {

    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = adminMapper.bookTotalCount();
    int display = 10;
    
    adminPageUtils.setPaging(page, total, display);
    Map<String, Object> map = Map.of("begin", adminPageUtils.getBegin(), "end", adminPageUtils.getEnd());
    
    model.addAttribute("bookList", adminMapper.getBookList(map));
    model.addAttribute("paging", adminPageUtils.getMvcPaging(request.getContextPath() + "/admin/bookList.do"));
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("totalCount", total);
    
  }
  
  @Override
  public void getBookDetail(HttpServletRequest request, Model model) {
    long isbn = Long.parseLong(request.getParameter("isbn")); // isbn은 int로는 값이 커서 불가능
    model.addAttribute("book", adminMapper.getBookDetail(isbn));
  }
  
  @Override
  public void getSearchBookList(HttpServletRequest request, Model model) {
    
    String column = request.getParameter("column");
    String query = request.getParameter("query");
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = adminMapper.bookSearchCount(Map.of("column", column, "query", query));
    int display = 10;
    
    adminPageUtils.setPaging(page, total, display);
    Map<String, Object> map = Map.of("begin", adminPageUtils.getBegin()
                                   , "end", adminPageUtils.getEnd()
                                   , "column", column
                                   , "query", query);
    
    model.addAttribute("bookList", adminMapper.getSearchBookList(map));
    model.addAttribute("paging", adminPageUtils.getMvcPaging(request.getContextPath() + "/admin/bookSearch.do", "column=" + column + "&query=" + query));
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("totalCount", total);
    
  }
  
  @Override
  public int addFacility(MultipartHttpServletRequest multiRequest) throws Exception {
    
    String facName = multiRequest.getParameter("facName");
    String facContent = multiRequest.getParameter("facContent");
    facContent = facContent.replace("\r\n","<br>");
    System.out.println("serviceImpl:: facContent = " + facContent);
    int checkStatus = Integer.parseInt(multiRequest.getParameter("checkStatus"));
    FacilityDto facility = FacilityDto.builder()
                                      .facName(facName)
                                      .facContent(facContent)
                                      .build();
    int addResult = adminMapper.addFacility(facility);
    
    List<MultipartFile> files = multiRequest.getFiles("files");
    
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
          path = adminFileUtils.getFacPath();
        } else if(checkStatus == 1) {
          path = adminFileUtils.getFacMacImagePath();
        }
        File dir = new File(path);
        if(!dir.exists()) {
          dir.mkdirs();
        }
        
        String facOriginalFilename = multipartFile.getOriginalFilename();
        String facFilesystemName = adminFileUtils.getFilesystemName(facOriginalFilename);
        
        System.out.println("path : " + path);
        System.out.println("facOriginalFilename : " +facOriginalFilename);
        System.out.println("facFilesystemName : " +facFilesystemName);
        
        String url = path + "/" + facFilesystemName;
        
        Path paths = Paths.get(url).toAbsolutePath();
        
        File file = new File(dir, facFilesystemName);
        
        System.out.println("file : " + file);
        
          //multipartFile.transferTo(file); // 이거 안됨
          multipartFile.transferTo(paths.toFile());
        
      String contentType = Files.probeContentType(paths); // 이미지의 Content-Type : image/jpeg, image/png 등 image로 시작한다.
      int hasThumbnail = (contentType != null && contentType.startsWith("image")) ? 1 : 0;
      
      // 썸네일이 있으면 원본파일 썸네일로 만들기
      if(hasThumbnail == 1) {
        File thumbnail = new File(dir, "s_" + facFilesystemName); // small 이미지를 의미하는 s_를 덧붙임
        Thumbnails.of(file)
                  .size(100, 100)       // 가로 100px, 세로 100px
                  .toFile(thumbnail);
      }
      
      AttachFacDto attachFac = AttachFacDto.builder()
                                 .facPath(path)
                                 .facOriginalFilename(facOriginalFilename)
                                 .facFilesystemName(facFilesystemName)
                                 .facHasThumbnail(0)
                                 .build();
      System.out.println("attachFac : " + attachFac);
      attachCount += adminMapper.addFacImage(attachFac);
        
      }
    }
    return addResult;
  }
  
  @Override
  public void getFacList(Model model) {
    model.addAttribute("facList", adminMapper.getFacList());
  }
  
  @Override
  public Map<String, Object> getFacTotalList(HttpServletRequest request) {
    
    String facStart = request.getParameter("facStart");
    facStart = facStart.replaceAll("-", "");
    facStart = facStart.substring(2);
    
    return Map.of("availableFacList", adminMapper.availableFacList(facStart), "unavailableFacList", adminMapper.unavailableFacList(facStart));
  }
  
  @Override
  public int addFacApply(HttpServletRequest request) {
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    int facNo = Integer.parseInt(request.getParameter("facNo"));
    Date facStart = Date.valueOf(request.getParameter("facStart"));
    
    int addResult = adminMapper.addFacApply(Map.of("userNo", userNo
                                                 , "facNo", facNo
                                                 , "facStart", facStart));
    return addResult;
  }
  
  @Override
  public boolean checkFacApply(HttpServletRequest request) {
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    String facStart = request.getParameter("facStart");
    facStart = facStart.replaceAll("-", "");
    facStart = facStart.substring(2);
    boolean check = adminMapper.checkFacApply(Map.of("userNo", userNo, "facStart", facStart));
    return check;
  }
  
  @Override
  public void getBookApplyList(HttpServletRequest request, Model model) {
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = adminMapper.bookApplyCount();
    int display = 10;
    
    adminPageUtils.setPaging(page, total, display);
    Map<String, Object> map = Map.of("begin", adminPageUtils.getBegin(), "end", adminPageUtils.getEnd());
    
    model.addAttribute("bookApplyList", adminMapper.getBookApplyList(map));
    model.addAttribute("paging", adminPageUtils.getMvcPaging(request.getContextPath() + "/admin/bookApplyList.do"));
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("totalCount", total);
    
  }
  
  @Override
  public void getBookApplyDetail(HttpServletRequest request, Model model) {
    
    int applyNo = Integer.parseInt(request.getParameter("applyNo"));
    model.addAttribute("bookApply", adminMapper.getBookApplyDetail(applyNo));
  }
  
  @Override
  public void getBookCheckoutList(HttpServletRequest request, Model model) {
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = adminMapper.bookCheckoutCount();
    int display = 10;
    
    adminPageUtils.setPaging(page, total, display);
    Map<String, Object> map = Map.of("begin", adminPageUtils.getBegin(), "end", adminPageUtils.getEnd());
    
    model.addAttribute("bookCheckoutList", adminMapper.getBookCheckoutList(map));
    model.addAttribute("paging", adminPageUtils.getMvcPaging(request.getContextPath() + "/admin/bookCheckoutList.do"));
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("totalCount", total);
    
  }
  
  @Override
  public void getBookCheckoutSearchList(HttpServletRequest request, Model model) {
    
    String column = request.getParameter("column");
    String query = request.getParameter("query");
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = adminMapper.bookCheckoutSearchCount(Map.of("column", column, "query", query));
    int display = 10;
    
    adminPageUtils.setPaging(page, total, display);
    Map<String, Object> map = Map.of("begin", adminPageUtils.getBegin(), "end", adminPageUtils.getEnd()
                                   , "column", column, "query", query);
    
    model.addAttribute("bookCheckoutList", adminMapper.getBookCheckoutSearchList(map));
    model.addAttribute("paging", adminPageUtils.getMvcPaging(request.getContextPath() + "/admin/bookCheckoutSearchList.do"));
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("totalCount", total);
    
  }
  
  @Override
  public void getBookCheckoutReturnList(HttpServletRequest request, Model model) {

    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = adminMapper.bookCheckoutReturnCount();
    int display = 10;
    
    adminPageUtils.setPaging(page, total, display);
    Map<String, Object> map = Map.of("begin", adminPageUtils.getBegin(), "end", adminPageUtils.getEnd());
    
    model.addAttribute("bookCheckoutList", adminMapper.getBookCheckoutReturnList(map));
    model.addAttribute("paging", adminPageUtils.getMvcPaging(request.getContextPath() + "/admin/bookCheckoutReturnList.do"));
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("totalCount", total);
    
  }
  
  @Override
  public void getBookCheckoutReturnSearchList(HttpServletRequest request, Model model) {

    String column = request.getParameter("column");
    String query = request.getParameter("query");
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = adminMapper.bookCheckoutReturnSearchCount(Map.of("column", column, "query", query));
    int display = 10;
    
    adminPageUtils.setPaging(page, total, display);
    Map<String, Object> map = Map.of("begin", adminPageUtils.getBegin(), "end", adminPageUtils.getEnd()
                                   , "column", column, "query", query);
    
    model.addAttribute("bookCheckoutList", adminMapper.getBookCheckoutReturnSearchList(map));
    model.addAttribute("paging", adminPageUtils.getMvcPaging(request.getContextPath() + "/admin/bookCheckoutReturnSearchList.do"));
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("totalCount", total);
    
  }
  
  @Override
  public int approvalBookCheckout(HttpServletRequest request) {
    int checkoutNo = Integer.parseInt(request.getParameter("checkoutNo"));
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    int updateResult = adminMapper.approvalBookCheckout(checkoutNo);
    if(updateResult == 1) {
      updateResult = adminMapper.addUserBookCount(userNo);
    }
    return updateResult;
  }
  
  @Override
  public int approvalBookCheckoutReturn(HttpServletRequest request) {
    int checkoutNo = Integer.parseInt(request.getParameter("checkoutNo"));
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    int status = Integer.parseInt(request.getParameter("status"));
    String isbn = request.getParameter("isbn");
    int updateResult = adminMapper.approvalBookCheckoutReturn(checkoutNo);
    if(updateResult == 1) {
      if(status == 3) {
        adminMapper.updateActiveUser(userNo);
      }
      adminMapper.activeBook(isbn);
      adminMapper.minusBookCount(userNo);
    }
    
    return updateResult;
  }
  
  @Override
  public void getFacApplyList(HttpServletRequest request, Model model) {
    model.addAttribute("facApplyList", adminMapper.getFacApplyList());
  }
  
  @Override
  public Map<String, Object> getAddBookSearch(HttpServletRequest request) {

    String apiURL = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx";
    String ttbkey = "ttbalsltksxk2011001";
    String QueryType = request.getParameter("QueryType");
    String Query = request.getParameter("Query");
    System.out.println(Query);
    String Start = request.getParameter("Start");
    
    StringBuilder sb = new StringBuilder();
    sb.append(apiURL);
    sb.append("?ttbkey=" + ttbkey);
    sb.append("&Query=" + Query);
    sb.append("&QueryType=" + QueryType);
    sb.append("&MaxResults=10");
    sb.append("&Start=" + Start);
    sb.append("&SearchTarget=Book");
    sb.append("&output=JS");
    sb.append("&Cover=Big");
    sb.append("&Version=20131101");
    
    List<BookDto> bookList = new ArrayList<BookDto>();
    
    JSONObject obj = null;
    try {
      // 요청
      URL url = new URL(sb.toString());
      HttpURLConnection con = (HttpURLConnection)url.openConnection();
      con.setRequestMethod("GET");  // 반드시 대문자로 작성
      
      // 응답
      BufferedReader reader = null;
      int responseCode = con.getResponseCode();
      if(responseCode == 200) {
        reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      } else {
        reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
      }
      String line = null;
      StringBuilder responseBody = new StringBuilder();
      while ((line = reader.readLine()) != null) {
        responseBody.append(line);
      }
      obj = new JSONObject(responseBody.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    
    JSONArray array = (JSONArray)obj.get("item");
    
    // for문으로 값 하나씩 insert하기
    for(Object lists : array) {
      try {
        JSONObject list = (JSONObject)lists;
        String pubdate = list.getString("pubDate");
        Date date = java.sql.Date.valueOf(pubdate);
        BookDto bookDto = BookDto.builder()
            .isbn(list.getString("isbn13"))
            .title(list.getString("title"))
            .cover(list.getString("cover"))
            .author(list.getString("author"))
            .publisher(list.getString("publisher"))
            .pubdate(date)
            .description(list.getString("description"))
            .categoryName(list.getString("categoryName"))
            .categoryId(list.getInt("categoryId"))
            .build();
        bookList.add(bookDto);
      } catch (Exception e) {
          e.printStackTrace();
      }
    }
    
    System.out.println(bookList);
    
    return Map.of("bookList", bookList);
  }
  
  @Override
  public Map<String, Object> addBook(HttpServletRequest request) {
    
    String isbn = request.getParameter("isbn");
    String title = request.getParameter("title");
    String cover = request.getParameter("cover");
    String author = request.getParameter("author");
    String publisher = request.getParameter("publisher");
    long timestamp = Long.parseLong(request.getParameter("pubdate"));
    Date pubdate = new Date(timestamp);
    String description = request.getParameter("description");
    String categoryName = request.getParameter("categoryName");
    int categoryId = Integer.parseInt(request.getParameter("categoryId"));
    
    // 이미 등록된 책인지 체크
    boolean checkResult = adminMapper.checkAddBook(isbn);
    System.out.println("checkResult : " + checkResult);
    if(checkResult == true) {
      return Map.of("addResult", 2);
    } else {
      
      BookDto book = BookDto.builder()
          .isbn(isbn)
          .title(title)
          .cover(cover)
          .author(author)
          .publisher(publisher)
          .pubdate(pubdate)
          .description(description)
          .categoryName(categoryName)
          .categoryId(categoryId)
          .build();
      
      int addResult = adminMapper.insertBook(book);
      return Map.of("addResult", addResult);
      
    }
    
  }
  
  @Override
  public int updateBookApply(HttpServletRequest request) {
    int applyNo = Integer.parseInt(request.getParameter("applyNo"));
    return adminMapper.updateBookApply(applyNo);
  }
  
  @Override
  public int approveFacApply(HttpServletRequest request) {
    int facApplyNo = Integer.parseInt(request.getParameter("facApplyNo"));
    return adminMapper.approveFacApply(facApplyNo);
  }
  
  @Override
  public int refuseFacApply(HttpServletRequest request) {
    int facApplyNo = Integer.parseInt(request.getParameter("facApplyNo"));
    return adminMapper.refuseFacApply(facApplyNo);
  }
  
  @Override
  public int deleteFac(HttpServletRequest request) {
    int facNo = Integer.parseInt(request.getParameter("facNo"));
    return adminMapper.deleteFac(facNo);
  }
  
  @Override
  public int deleteBook(HttpServletRequest request) {
    String isbn = request.getParameter("isbn");
    return adminMapper.deleteBook(isbn);
  }
  
  @Override
  public int activeUser(HttpServletRequest request) {
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    return adminMapper.updateActiveUser(userNo);
  }
  
  @Override
  public int inactiveUser(HttpServletRequest request) {
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    return adminMapper.updateInactiveUser(userNo);
  }
  
  @Override
  public AttachFacDto getFacDetail(HttpServletRequest request) {
    int facNo = Integer.parseInt(request.getParameter("facNo"));
    return adminMapper.getFacDetail(facNo);
  }
  
  @Override
  public int editFacility(MultipartHttpServletRequest multiRequest) throws Exception {
    
    String facName = multiRequest.getParameter("facName");
    String facContent = multiRequest.getParameter("facContent");
    int facNo = Integer.parseInt(multiRequest.getParameter("facNo"));
    int facAttachNo = Integer.parseInt(multiRequest.getParameter("facAttachNo"));
    facContent = facContent.replace("\r\n","<br>");
    System.out.println("serviceImpl:: facContent = " + facContent);
    int checkStatus = Integer.parseInt(multiRequest.getParameter("checkStatus"));
    FacilityDto facility = FacilityDto.builder()
                                      .facNo(facNo)
                                      .facName(facName)
                                      .facContent(facContent)
                                      .build();
    int editResult = adminMapper.editFacility(facility);
    
    List<MultipartFile> files = multiRequest.getFiles("files");
    
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
          path = adminFileUtils.getFacPath();
        } else if(checkStatus == 1) {
          path = adminFileUtils.getFacMacImagePath();
        }
        File dir = new File(path);
        if(!dir.exists()) {
          dir.mkdirs();
        }
        
        String facOriginalFilename = multipartFile.getOriginalFilename();
        String facFilesystemName = adminFileUtils.getFilesystemName(facOriginalFilename);
        
        System.out.println("path : " + path);
        System.out.println("facOriginalFilename : " +facOriginalFilename);
        System.out.println("facFilesystemName : " +facFilesystemName);
        
        String url = path + "/" + facFilesystemName;
        
        Path paths = Paths.get(url).toAbsolutePath();
        
        File file = new File(dir, facFilesystemName);
        
        System.out.println("file : " + file);
        
          //multipartFile.transferTo(file); // 이거 안됨
          multipartFile.transferTo(paths.toFile());
        
      String contentType = Files.probeContentType(paths); // 이미지의 Content-Type : image/jpeg, image/png 등 image로 시작한다.
      int hasThumbnail = (contentType != null && contentType.startsWith("image")) ? 1 : 0;
      
      // 썸네일이 있으면 원본파일 썸네일로 만들기
      if(hasThumbnail == 1) {
        File thumbnail = new File(dir, "s_" + facFilesystemName); // small 이미지를 의미하는 s_를 덧붙임
        Thumbnails.of(file)
                  .size(100, 100)       // 가로 100px, 세로 100px
                  .toFile(thumbnail);
      }
      
      AttachFacDto attachFac = AttachFacDto.builder()
                                 .facAttachNo(facAttachNo)
                                 .facPath(path)
                                 .facOriginalFilename(facOriginalFilename)
                                 .facFilesystemName(facFilesystemName)
                                 .facHasThumbnail(0)
                                 .build();
      System.out.println("attachFac : " + attachFac);
      attachCount += adminMapper.editAttachFac(attachFac);
        
      }
    }
    return editResult;
  }
  
  @Override
  public int addDirectBook(MultipartHttpServletRequest multiRequest) throws Exception {
    
    String isbn = multiRequest.getParameter("isbn");
    String title = multiRequest.getParameter("title");
    String author = multiRequest.getParameter("author");
    String publisher = multiRequest.getParameter("publisher");
    String pubdate = multiRequest.getParameter("pubdate");
    pubdate = pubdate.replaceAll("/", "-");
    System.out.println("pubdate : " + pubdate);
    Date date = null;
    if(!pubdate.isEmpty()) {
      date = java.sql.Date.valueOf(pubdate);
    }
    String description = multiRequest.getParameter("description");
    description = description.replace("\r\n","<br>");
    String categoryName = multiRequest.getParameter("categoryName");
    String url = "";
    List<MultipartFile> files = multiRequest.getFiles("files");
    
    int attachCount;
    if(files.get(0).getSize() == 0) {
      attachCount = 1;
    } else {
      attachCount = 0;
    }
    
    for(MultipartFile multipartFile : files) {
      if(multipartFile != null && !multipartFile.isEmpty()) {
        String path = "";
          path = adminFileUtils.getBookImagePath();
        File dir = new File(path);
        if(!dir.exists()) {
          dir.mkdirs();
        }
        
        String originalFilename = multipartFile.getOriginalFilename();
        // String facFilesystemName = adminFileUtils.getFilesystemName(facOriginalFilename);
        String extName = null;
        String[] arr = originalFilename.split("\\.");  // [.] 또는 \\.
        extName = arr[arr.length - 1];
        String filesystemName = isbn + "." + extName;
        
        System.out.println("path : " + path);
        
        url = path + "/" + filesystemName;
        
        Path paths = Paths.get(url).toAbsolutePath();
        
        File file = new File(dir, filesystemName);
        
        // 같은 파일 있으면 지워버림
        if(file.exists()) {
          file.delete();
        }
        
        System.out.println("file : " + file);
        
          //multipartFile.transferTo(file); // 이거 안됨
          multipartFile.transferTo(paths.toFile());
        
      String contentType = Files.probeContentType(paths); // 이미지의 Content-Type : image/jpeg, image/png 등 image로 시작한다.
      int hasThumbnail = (contentType != null && contentType.startsWith("image")) ? 1 : 0;
      
      // 썸네일이 있으면 원본파일 썸네일로 만들기
      if(hasThumbnail == 1) {
        File thumbnail = new File(dir, "s_" + filesystemName); // small 이미지를 의미하는 s_를 덧붙임
        Thumbnails.of(file)
                  .size(100, 100)       // 가로 100px, 세로 100px
                  .toFile(thumbnail);
      }
      
      // System.out.println("attachFac : " + attachFac);
      // attachCount += adminMapper.addFacImage(attachFac);
        
      }
    }
    
    BookDto bookDto = BookDto.builder()
                             .isbn(isbn)
                             .title(title)
                             .author(author)
                             .publisher(publisher)
                             .pubdate(date)
                             .description(description)
                             .categoryName(categoryName)
                             .cover(url)
                             .build();
    
    return adminMapper.insertDirectBook(bookDto);
  }
  
  @Override
  public int editBook(MultipartHttpServletRequest multiRequest) throws Exception {
    
    String isbn = multiRequest.getParameter("isbn");
    String title = multiRequest.getParameter("title");
    String author = multiRequest.getParameter("author");
    String publisher = multiRequest.getParameter("publisher");
    String pubdate = multiRequest.getParameter("pubdate");
    pubdate = pubdate.replaceAll("/", "-");
    System.out.println("pubdate : " + pubdate);
    Date date = null;
    if(!pubdate.isEmpty()) {
      date = java.sql.Date.valueOf(pubdate);
    }
    String description = multiRequest.getParameter("description");
    description = description.replace("\r\n","<br>");
    String categoryName = multiRequest.getParameter("categoryName");
    String url = "";
    List<MultipartFile> files = multiRequest.getFiles("files");
    
    int attachCount;
    if(files.get(0).getSize() == 0) {
      attachCount = 1;
    } else {
      attachCount = 0;
    }
    
    // 파일을 첨부 안했으면 기존 cover를 저장
    if(attachCount == 1) {
      url = multiRequest.getParameter("cover");
    }
    
    for(MultipartFile multipartFile : files) {
      if(multipartFile != null && !multipartFile.isEmpty()) {
        String path = "";
          path = adminFileUtils.getBookImagePath();
        File dir = new File(path);
        if(!dir.exists()) {
          dir.mkdirs();
        }
        
        String originalFilename = multipartFile.getOriginalFilename();
        // String facFilesystemName = adminFileUtils.getFilesystemName(facOriginalFilename);
        String extName = null;
        String[] arr = originalFilename.split("\\.");  // [.] 또는 \\.
        extName = arr[arr.length - 1];
        String filesystemName = isbn + "." + extName;
        
        System.out.println("path : " + path);
        
        url = path + "/" + filesystemName;
        
        Path paths = Paths.get(url).toAbsolutePath();
        
        File file = new File(dir, filesystemName);
        
        // 같은 파일 있으면 지워버림
        if(file.exists()) {
          file.delete();
        }
        
        System.out.println("file : " + file);
        
          //multipartFile.transferTo(file); // 이거 안됨
          multipartFile.transferTo(paths.toFile());
        
      String contentType = Files.probeContentType(paths); // 이미지의 Content-Type : image/jpeg, image/png 등 image로 시작한다.
      int hasThumbnail = (contentType != null && contentType.startsWith("image")) ? 1 : 0;
      
      // 썸네일이 있으면 원본파일 썸네일로 만들기
      if(hasThumbnail == 1) {
        File thumbnail = new File(dir, "s_" + filesystemName); // small 이미지를 의미하는 s_를 덧붙임
        Thumbnails.of(file)
                  .size(100, 100)       // 가로 100px, 세로 100px
                  .toFile(thumbnail);
      }
      
      // System.out.println("attachFac : " + attachFac);
      // attachCount += adminMapper.addFacImage(attachFac);
        
      }
    }
    
    BookDto bookDto = BookDto.builder()
                             .isbn(isbn)
                             .title(title)
                             .author(author)
                             .publisher(publisher)
                             .pubdate(date)
                             .description(description)
                             .categoryName(categoryName)
                             .cover(url)
                             .build();
    
    return adminMapper.editBook(bookDto);
    
  }
  
  @Override
  public boolean checkBook(HttpServletRequest request) {
    return adminMapper.checkAddBook(request.getParameter("isbn"));
  }
  

  @Override
  public int inactiveBook(HttpServletRequest request) {
    return adminMapper.inactiveBook(request.getParameter("isbn"));
  }
  
  @Override
  public int activeBook(HttpServletRequest request) {
    return adminMapper.activeBook(request.getParameter("isbn"));
  }
  

}
