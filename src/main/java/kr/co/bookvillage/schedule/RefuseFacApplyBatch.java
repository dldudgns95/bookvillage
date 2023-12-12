package kr.co.bookvillage.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.co.bookvillage.service.AdminService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RefuseFacApplyBatch {

  private final AdminService adminService;
  
  // 자정이 되면 시설이용 신청 시작날짜가 지나서 승인대기 상태면 자동으로 거절
  @Scheduled(cron = "0 0 0 * * *")  // 자정
  public void execute() {
    adminService.updateFacApply();
  }
  
}

