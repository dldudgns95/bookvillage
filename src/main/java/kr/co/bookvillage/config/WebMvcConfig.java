package kr.co.bookvillage.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.co.bookvillage.interceptor.CheckAdminLoginInterceptor;
import kr.co.bookvillage.interceptor.RequiredLoginInterceptor;
import kr.co.bookvillage.interceptor.ShouldNotLoginInterceptor;
import lombok.RequiredArgsConstructor;

@EnableWebMvc
@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  private final RequiredLoginInterceptor requiredLoginInterceptor;
  private final ShouldNotLoginInterceptor shouldNotLoginInterceptor;
  private final CheckAdminLoginInterceptor checkAdminLoginInterceptor;
  
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(requiredLoginInterceptor)
      .addPathPatterns("/user/modifyPw.form");
    registry.addInterceptor(shouldNotLoginInterceptor)
      .addPathPatterns("/user/agree.form", "/user/join.form", "/user/login.form");
    registry.addInterceptor(checkAdminLoginInterceptor)
    .addPathPatterns("/admin/**");
  }
  
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/**")
      .addResourceLocations("classpath:/static/", "classpath:/templates/");
    registry.addResourceHandler("/admin/**")
    .addResourceLocations("file:/admin/");
 
  }
  
}