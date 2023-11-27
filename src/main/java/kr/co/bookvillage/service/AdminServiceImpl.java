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
import java.text.SimpleDateFormat;
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
    String Start = request.getParameter("Start");
    
    StringBuilder sb = new StringBuilder();
    sb.append(apiURL);
    sb.append("?ttbkey=" + ttbkey);
    sb.append("&QueryType=" + QueryType);
    sb.append("&MaxResults=50");
    sb.append("&Start=" + Start);
    sb.append("&SearchTarget=Book");
    sb.append("&output=JS");
    sb.append("&Cover=Big");
    sb.append("&Version=20131101");
    
    JSONObject obj = null;
    int count = 0;
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
    
    System.out.println(array.length());
    
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
  public void addFacility(MultipartHttpServletRequest multiRequest) throws Exception {
    
    String facName = multiRequest.getParameter("facName");
    String facContent = multiRequest.getParameter("facContent");
    
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
        String path = adminFileUtils.getFacPath();
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
    
    
  }
  
}
