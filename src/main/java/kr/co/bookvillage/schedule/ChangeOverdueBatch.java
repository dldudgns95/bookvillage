package kr.co.bookvillage.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.co.bookvillage.service.AdminService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ChangeOverdueBatch {

  private final AdminService adminService;
  
  // 자정이 되면 반납예정일이 지난 도서 대출을 연체로 변경 + 연체중인 유저들은 대출 불가로 변경
  @Scheduled(cron = "0 0 0 * * *")  // 자정
  public void execute() {
    adminService.changeOverdueBatch();
  }
  
}

