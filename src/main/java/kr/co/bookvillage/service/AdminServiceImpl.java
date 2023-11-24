package kr.co.bookvillage.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import kr.co.bookvillage.dao.AdminMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
  
  private final AdminMapper adminMapper;

  @Override
  public int insertBook(HttpServletRequest request) throws Exception {
    
    String apiURL = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx";
    String ttbkey = "ttbalsltksxk2011001";
    String QueryType = request.getParameter("QueryType");
    
    StringBuilder sb = new StringBuilder();
    sb.append(apiURL);
    sb.append("?ttbkey=" + ttbkey);
    sb.append("&QueryType=" + QueryType);
    sb.append("&MaxResults=10");
    sb.append("&start=1");
    sb.append("&SearchTarget=Book");
    sb.append("&output=JS");
    sb.append("&Version=20131101");
    
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
    
    JSONObject obj = new JSONObject(responseBody.toString());
    
    JSONArray array = (JSONArray)obj.get("item");
    for(Object list : array) {
      JSONObject l = (JSONObject)list;
      System.out.println(l);
    }
    
    return 0;
  }
  
}
