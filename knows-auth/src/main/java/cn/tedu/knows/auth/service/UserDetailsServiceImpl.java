package cn.tedu.knows.auth.service;

import cn.tedu.knows.commons.model.Permission;
import cn.tedu.knows.commons.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

// Component 和 Service 作用都是保存到 Spring容器中 不过 Service 是针对业务逻辑层
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // 1.根据用户名获取用户对象
        String ip = "http://sys-service";
        String url = "/v1/auth/user?username={1}";
        User user = restTemplate.getForObject(ip+url, User.class);
        // 2.判断查询出来的用户是否存在
        if (user == null){
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        // 3.根据用户id查询用户所有权限
        url = "/v1/auth/permission?id={1}";
        // Ribbon 请求的控制器返回的返回值是 list 时, 要使用该类型的数组接收
        Permission[] permissions = restTemplate.getForObject(ip+url,Permission[].class);
        // 4.根据用户id查询用户所有角色
        // 5.将权限和角色保存在 auth 数组中
        // 6.创建 UserDetails 对象
        // 7.返回
        return null;
    }
}
