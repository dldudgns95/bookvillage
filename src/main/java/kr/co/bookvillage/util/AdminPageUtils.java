package kr.co.bookvillage.util;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Component
public class AdminPageUtils {

  private int page;     // 현재 페이지 번호(요청 파라미터로 받는다.)
  private int total;    // 전체 항목의 개수(DB에서 구한 뒤 받는다.)
  private int display;  // 한 페이지에 표시할 항목의 개수(요청 파라미터로 받는다.)
  private int begin;    // 한 페이지에 표시되는 항목의 시작 번호(계산한다.)
  private int end;      // 한 페이지에 표시되는 항목의 종료 번호(계산한다.)
  
  private int totalPage;         // 전체 페이지의 개수(계산한다.)
  private int pagePerBlock = 10; // 한 블록에 표시되는 페이지의 개수(임의로 정한다.)
  private int beginPage;         // 한 블록에 표시되는 페이지의 시작 번호(계산한다.)
  private int endPage;           // 한 블록에 표시되는 페이지의 종료 번호(계산한다.)
  
  public void setPaging(int page, int total, int display) {
    
    /* 한 페이지를 나타낼 때 필요한 정보 */
    
    // 받은 정보 저장
    this.page = page;
    this.total = total;
    this.display = display;
    
    // 계산한 정보 저장
    begin = (page - 1) * display + 1;
    end = begin + display - 1;
    end = end > total ? total : end;
    
    
    /* 전체 페이지를 나타낼 때 필요한 정보 */
    
    // 전체 페이지 계산
    totalPage = (int)Math.ceil((double)total / display);
    System.out.println("totalPage : " + totalPage);
    
    // 각 블록의 시작 페이지와 종료 페이지 계산
    beginPage = ((page - 1) / pagePerBlock) * pagePerBlock + 1;
    endPage = beginPage + pagePerBlock - 1;
    endPage = endPage > totalPage ? totalPage : endPage;
    
  }
  
  public String getMvcPaging(String url) {
    
    /*
     *<nav aria-label="...">
        <ul class="pagination">
          <li class="page-item disabled">
            <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Previous</a>
          </li>
          <li class="page-item"><a class="page-link" href="#">1</a></li>
          <li class="page-item active" aria-current="page">
            <a class="page-link" href="#">2</a>
          </li>
          <li class="page-item"><a class="page-link" href="#">3</a></li>
          <li class="page-item">
            <a class="page-link" href="#">Next</a>
          </li>
        </ul>
      </nav>
     */
    
    StringBuilder sb = new StringBuilder();
    
    sb.append("<div><nav aria-label=\"...\">");
    sb.append("<ul class=\"pagination\">");
    
    
    // 이전 블록
    if(beginPage == 1) {
      sb.append("<li class=\"page-item disabled\">");
      sb.append("<a class=\"page-link\" tabindex=\"-1\" aria-disabled=\"true\">이전</a>");
      sb.append("</li>");
    } else {
      sb.append("<li class=\"page-item\">");
      sb.append("<a class=\"page-link\" href=\""+ url + "?page=" + (beginPage - 1) +"\" tabindex=\"-1\"\">이전</a>");
      sb.append("</li>");
    }
    
    
    // 페이지 번호
    for(int p = beginPage; p <= endPage; p++) {
      if(p == page) {
        sb.append("<li class=\"page-item active\" aria-current=\"page\"><a class=\"page-link\">"+p+"</a></li>");
      } else {
        sb.append("<li class=\"page-item\"><a class=\"page-link\" href=\""+ url + "?page=" + p + "\">" + p +"</a></li>");
      }
    }
    
    // 다음 블록
    if(endPage == totalPage) {
      sb.append("<li class=\"page-item disabled\"><a class=\"page-link\" aria-disabled=\"true\">다음</a></li>");
    } else {
      sb.append("<li class=\"page-item\"><a class=\"page-link\" href=\""+url + "?page=" + (endPage + 1)+"\">다음</a></li>");
    }
    
    sb.append("</ul></nav></div>");
    
    return sb.toString();
    
  }
  
  public String getMvcPaging(String url, String params) {
    
    
    StringBuilder sb = new StringBuilder();
    
    sb.append("<div><nav aria-label=\"...\">");
    sb.append("<ul class=\"pagination\">");
    
    // 이전 블록
    if(beginPage == 1) {
      sb.append("<li class=\"page-item disabled\">");
      sb.append("<a class=\"page-link\" href=\""+ url + "?page=" + (beginPage - 1) +"&" + params +"\" tabindex=\"-1\" aria-disabled=\"true\">이전</a>");
      sb.append("</li>");
    } else {
      sb.append("<li class=\"page-item\">");
      sb.append("<a class=\"page-link\" href=\""+ url + "?page=" + (beginPage - 1) +"&" + params +"\" tabindex=\"-1\"\">이전</a>");
      sb.append("</li>");
    }
    
    
    // 페이지 번호
    for(int p = beginPage; p <= endPage; p++) {
      if(p == page) {
        sb.append("<li class=\"page-item active\" aria-current=\"page\"><a class=\"page-link\" href=\"#\">"+p+"</a></li>");
      } else {
        sb.append("<li class=\"page-item\"><a class=\"page-link\" href=\""+ url + "?page=" + p + "&" + params +"\">" + p +"</a></li>");
      }
    }
    
    // 다음 블록
    if(endPage == totalPage) {
      sb.append("<li class=\"page-item disabled\"><a class=\"page-link\" href=\"#\" aria-disabled=\"true\">다음</a></li>");
    } else {
      sb.append("<li class=\"page-item\"><a class=\"page-link\" href=\""+url + "?page=" + (endPage + 1)+"&" + params +"\">다음</a></li>");
      
    }
    
    sb.append("</ul></nav></div>");
    
    return sb.toString();
  }

  public String getAjaxPaging() {
    
    StringBuilder sb = new StringBuilder();
    
    sb.append("<div>");
    
    // 이전 블록
    if(beginPage == 1) {
      sb.append("<a>이전</a>");
    } else {
      sb.append("<a href=\"javascript:fnAjaxPaging(" + (beginPage-1) + ")\">이전</a>");
    }
    
    // 페이지 번호
    for(int p = beginPage; p <= endPage; p++) {
      if(p == page) {
        sb.append("<a>" + p + "</a>");
      } else {
        sb.append("<a href=\"javascript:fnAjaxPaging(" + p + ")\">" + p + "</a>");
      }
    }
    
    // 다음 블록
    if(endPage == totalPage) {
      sb.append("<a>다음</a>");
    } else {
      sb.append("<a href=\"javascript:fnAjaxPaging(" + (endPage+1) + ")\">다음</a>");
    }
    
    sb.append("</div>");
    
    return sb.toString();
    
  }
  
}
