package kr.co.bookvillage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class BookvillageApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookvillageApplication.class, args);
	}

}
