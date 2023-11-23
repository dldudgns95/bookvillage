package kr.co.bookvillage.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {

  @Value("${jasypt.encryptor.password}")
  private String password;
  
  @Bean("jasyptStringEncryptor")
  public StringEncryptor stringEncryptor() {
      StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
      SimpleStringPBEConfig config = new SimpleStringPBEConfig();
      config.setPassword(password);
      config.setAlgorithm("PBEWITHMD5ANDDES");
      config.setKeyObtentionIterations("1000");
      config.setPoolSize("1");
      config.setProviderName("SunJCE");
      config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
      config.setIvGeneratorClassName("org.jasypt.iv.NoIvGenerator");
      config.setStringOutputType("base64");
      encryptor.setConfig(config);
      return encryptor;
  }
  
  // Jasypt Online Encryption and Decryption 사이트
  // https://www.devglan.com/online-tools/jasypt-online-encryption-decryption
  
}