package cn.tedu.knows.sys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("cn.tedu.knows.sys.mapper")     // 连库就得用到这个注解
public class KnowsSysApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnowsSysApplication.class, args);
    }
    // 将下面方法的返回值返回给 spring容器
    @Bean
    // 负载均衡, 微服务之间的调用是不经过网关的, 所以网关中的负载均衡在微服务之间是无效的
    @LoadBalanced
    // 向spring容器中保存一个 RestTemplate 类型的对象,支持 Ribbon 调用
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
