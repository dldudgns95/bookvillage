package kr.co.bookvillage.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.co.bookvillage.service.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class InactiveUserBatch {

  private final UserService userService;
  
  @Scheduled(cron="0 0 0 * * *")  
  public void execute() {
    
    userService.inactiveUserBatch();
    
  }
  
}

