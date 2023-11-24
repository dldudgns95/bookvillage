package kr.co.bookvillage.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import kr.co.bookvillage.dao.UserMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
  
  public final UserMapper userMapper;
  
  @Override
  public void login(HttpServletRequest request, Model model) {
    // TODO Auto-generated method stub
    
  }

}
