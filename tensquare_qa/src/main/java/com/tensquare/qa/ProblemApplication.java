package com.tensquare.qa;
import com.tensquare.qa.interceptor.JwtInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import utils.IdWorker;
import utils.JwtUtil;


/*
* 其实用更简单的话来说，就是如果选用的注册中心是eureka，
* 那么就推荐@EnableEurekaClient，如果是其他的注册中心，
* 那么推荐使用@EnableDiscoveryClient。
* */
@SpringBootApplication
@EnableEurekaClient//开启Eureka客户端功能
@EnableDiscoveryClient//用来标识开启服务发现功能
@EnableFeignClients//用来开启Feign功能
public class ProblemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProblemApplication.class, args);
	}

	@Bean
	public IdWorker idWorker(){
		return new IdWorker(1, 1);
	}
	@Bean
	public JwtUtil jwtUtil(){
		return new JwtUtil();
	}
	
}
