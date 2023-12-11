package kr.co.bookvillage.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.co.bookvillage.service.AdminService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CancleBookCheckoutBatch {

  private final AdminService adminService;
  
  // 자정이 되면 도서 대출신청 상태로 3일이 지난 목록들 자동으로 취소 + 도서는 대출가능으로 변경
  @Scheduled(cron = "0 0 0 * * *")  // 자정
  public void execute() {
    adminService.cancleBookCheckoutBatch();
  }
  
}

