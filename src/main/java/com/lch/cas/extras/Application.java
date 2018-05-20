package com.lch.cas.extras;

import com.lch.cas.extras.config.AppConfig;
import com.lch.cas.extras.config.JwtFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	@Bean
	public FilterRegistrationBean jwtFilter() {
		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new JwtFilter());
		registrationBean.addUrlPatterns("/api/*");

		return registrationBean;
	}
//
//	@Bean
//	AppConfig appConfig() {
//		return new AppConfig();
//	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
