package com.uk.container.docker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.uk.container.docker.jwt.TimeInterceptor;

@SpringBootApplication
@ComponentScan(basePackages = {"com.uk"})
//@EnableJpaRepositories(basePackages = { "com.uk" })
public class DockerApplication extends WebMvcConfigurerAdapter {

	static Class<?> appClass = DockerApplication.class;

	public static void main(String[] args) {
		SpringApplication.run(appClass, args);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new TimeInterceptor());
	}
	
	
}
