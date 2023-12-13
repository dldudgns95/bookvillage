package kr.co.bookvillage.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.bookvillage.dto.AnswerDto;
import kr.co.bookvillage.dto.AskDto;
import kr.co.bookvillage.dto.NoticeDto;
import kr.co.bookvillage.service.CommentService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/comment")
@Controller
@RequiredArgsConstructor
public class CommentController {
  
  private final CommentService commentService;
  
  @GetMapping("/commentList.do")
  public String commentList(HttpServletRequest request, Model model) {
    commentService.loadAskList(request, model);
    return "comment/commentList"; 
  }

  // 작성 페이지로 이동
  @GetMapping("/commentWrite.form")
  public String commentWrite() {
    return "comment/commentWrite";
  }
  
  @ResponseBody
  @PostMapping(value = "/imageUpload.do", produces = "application/json")
  public Map<String, Object> imageUpload(MultipartHttpServletRequest multipartRequest){
    return commentService.imageUpload(multipartRequest);
  }

  // 작성
  @PostMapping("/addAsk.do")
  public String addAsk(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    int addResult = commentService.addAsk(request);
    redirectAttributes.addFlashAttribute("addResult", addResult);
    return "redirect:/comment/commentList.do";
  }
  
  // 조회수
  @GetMapping("/increseHit.do")
  public String increseHit(@RequestParam(value = "askNo", required = false, defaultValue = "0") int askNo) {
    int increseResult = commentService.increseHit(askNo);
    if(increseResult == 1) {
      return "redirect:/comment/commentdetail.do?askNo=" + askNo;
    } else {
      return "redirect:/comment/commentList.do";
    }
  }

  
  //@RequestMapping(value="/commentDetail.do", method= {RequestMethod.GET, RequestMethod.POST})
  @GetMapping("/commentDetail.do")
  public String commentDetail(@RequestParam(value = "askNo" , required = false, defaultValue = "0") int askNo
                             , Model model) {
    System.out.println("확인");
    AskDto ask = commentService.getAsk(askNo);
    model.addAttribute("ask", ask);
    return "comment/commentDetail";
  }
  
  //수정 페이지로 이동 modifyAsk.do
  
  @PostMapping("/commentEdit.form")
  public String edit(@ModelAttribute("ask") AskDto ask) {
    return "comment/commentEdit";
  }
  
  @PostMapping("/commentModifyAsk.do")
  public String commentModifyAsk(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    int modifyResult = commentService.modifyAsk(request);
    redirectAttributes.addFlashAttribute("modifyResult", modifyResult);
    return "redirect:/comment/commentDetail.do?askNo=" + request.getParameter("askNo");
  }
  
  // 삭제
  @PostMapping("/commnetDelete.do")
  public String commnetDelete(@RequestParam(value = "askNo" , required = false, defaultValue = "0") int askNo
                                         , RedirectAttributes redirectAttributes) {
    int removeResult = commentService.removeAsk(askNo);
    redirectAttributes.addFlashAttribute("removeResult", removeResult);
    return "redirect:/comment/commentList.do";
    
  }
  
  
  // 댓글 삽입
  @ResponseBody
  @PostMapping(value = "/answerAdd.do", produces = "application/json")
  public Map<String, Object> answerAdd(HttpServletRequest request){
    return commentService.addtAnswer(request);
  }

  // 댓글 목록
  @ResponseBody
  @GetMapping(value = "/getAnsList.do", produces = "application/json")
  public List<AnswerDto> getAnsList() {
    return commentService.getAnsList();
  }
  
  
  
  
}
