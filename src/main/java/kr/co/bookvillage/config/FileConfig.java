package kr.co.bookvillage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class FileConfig {

  @Bean
  public MultipartResolver multipartResolver() {
    CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
    commonsMultipartResolver.setDefaultEncoding("UTF-8");
    commonsMultipartResolver.setMaxUploadSize(1024 * 1024 * 100);       // 전체 첨부 파일의 최대 크기 100MB
    commonsMultipartResolver.setMaxUploadSizePerFile(1024 * 1024 * 10); // 개별 첨부 파일의 최대 크기 10MB
    return commonsMultipartResolver;
  }
  
}