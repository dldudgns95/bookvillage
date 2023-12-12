package kr.co.bookvillage.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.co.bookvillage.service.NoticeService;
import kr.co.bookvillage.service.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class noitceImageBatch {

  private final NoticeService noticeService;
  
  @Scheduled(cron="0 0 1 1/1 * ?")  
  public void execute() {  
    noticeService.noticeImageBatch();
    
  }
  
}

