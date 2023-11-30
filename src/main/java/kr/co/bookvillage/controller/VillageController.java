package kr.co.bookvillage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequestMapping("/village")
@RequiredArgsConstructor
@Controller
public class VillageController {

  // 빌리지 입장
  @GetMapping("/list.do")
  public String village() {
    return "village/list";
  }
}
