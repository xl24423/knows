package cn.tedu.knows.portal.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 本控制器的目标是根据不同用户的角色, 跳转到对应的首页
// 而@RestController注解针对异步请求,控制器方法返回的信息都会以字符串
// 或者 json 格式 返回给 axios 来处理, 不会有页面跳转效果
// @Controller标记的控制器, 支持我们返回字符串特定格式时,能够实现重定向
// 到某个页面的效果
@Controller
public class HomeController {
    // Spring-Security 框架中角色\权限是框架设置好的类型
    // 要判断具体的某个角色 , 建议将这个角色声明为常量类型, 判断时使用
    public static final GrantedAuthority STUDENT =
                            new SimpleGrantedAuthority("ROLE_STUDENT");
    public static final GrantedAuthority TEACHER =
                            new SimpleGrantedAuthority("ROLE_TEACHER");
    public static final GrantedAuthority ADMIN =
                            new SimpleGrantedAuthority("ROLE_ADMIN");

    @GetMapping(value = {"/index.html","/"})
    public String index(@AuthenticationPrincipal UserDetails userDetails){
        // 判断当前登录用户是否包含讲师角色
        if(userDetails.getAuthorities().contains(TEACHER)){
            //如果包含讲师角色,跳转到讲师页面
            return "redirect:/index_teacher.html";
        }else if (userDetails.getAuthorities().contains(STUDENT)){
            return "redirect:/index_student.html";
        }
        // 既不是讲师也不是学生的用户暂不考虑,直接返回null
        return null;
    }

}
