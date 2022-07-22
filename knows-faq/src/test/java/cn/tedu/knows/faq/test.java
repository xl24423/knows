package cn.tedu.knows.faq;

import cn.tedu.knows.commons.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@SpringBootTest
public class test {
    @Resource
    RedisTemplate<String , String> redisTemplate;
    @Test
    public void t1(){
        redisTemplate.opsForValue().set("myname","诸葛亮");
    }
    @Test
    public void get(){
        t1();
        System.out.println(redisTemplate.opsForValue().get("myname"));
    }
    @Resource
    RestTemplate restTemplate;
    @Test
    public void t2(){
        // 这里不适用固定的 localhost是因为要负载均衡,不能使用固定的 ip地址+端口,使用更为灵活的服务名称,以便负载均衡到空闲服务器
        // 服务器名称必须是 nacos 服务列表中存在的名称
        String url = "http://sys-service/v1/auth/demo";
        String forObject = restTemplate.getForObject(url,String.class);
        System.out.println(forObject);
    }
    @Test
    public void getUser(){
        // url路径中 ? 之后的内容就是 Ribbon请求的参数
        // 参数的名称必须与控制器方法 里面的形参名一致
        // 参数的值不直接赋值, 使用{1}来占位
        // 调用时有既定的赋值方式
        String url = "http://sys-service/v1/auth/user?username={1}";
        User user = restTemplate.getForObject(url,User.class,"st2");
        System.out.println(user);
    }
    @Test
    public void t3(){
        System.out.println(2&1);
    }
}
