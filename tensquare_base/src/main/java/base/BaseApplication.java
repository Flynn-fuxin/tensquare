package base;

import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 基础模块的启动类
 */
@SpringBootApplication
@EnableEurekaClient//开启Eureka客户端功能
public class BaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class);
    }

    //定义IdWorker的Bean
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }

}
