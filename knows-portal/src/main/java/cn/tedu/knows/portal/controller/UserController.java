package cn.tedu.knows.portal.controller;


import cn.tedu.knows.portal.mapper.UserMapper;
import cn.tedu.knows.portal.model.Permission;
import cn.tedu.knows.portal.model.Question;
import cn.tedu.knows.portal.model.User;
import cn.tedu.knows.portal.service.IUserService;
import cn.tedu.knows.portal.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
@RestController
@RequestMapping("/v1/users")
public class UserController {
        //@GetMapping("")专门用于处理get请求
      //@PostMapping("") 专门用于处理post请求
    @GetMapping("/demo")
    public String run(){
        return "hhhhh";
    }
    @GetMapping("/answer")
    // 下面的注解来规定当前用户必须包含answer授权
    //才能访问下面的控制器方法
    @PreAuthorize("hasAuthority('answer')")
    public String answer(){
        return "超级用户";
    }
    @GetMapping("/delete")
    @PreAuthorize("hasAuthority('delete')")
    public String delete(){
        return "允许删除回答";
    }

    @Autowired
    UserMapper userMapper;
    @Autowired
    IUserService userService;
    @GetMapping("/select")
    public String user(int id){
        List<Permission> user = userMapper.selectPermissionsByUserId(id);
        return user.toString();
    }
    @GetMapping("/master")
    public List<User> selectAllTeachers(){
        return userMapper.findTeachers();
    }
    @GetMapping("/me")
    public UserVO me(@AuthenticationPrincipal UserDetails user){
        return userService.getUserVO(user.getUsername());
    }
    @GetMapping("/myCollects")
    public List<Question> getMyCollectQuestions(){

        return userService.getMyCollectQuestions();
    }
    @GetMapping("")
    public String NowUserName(@AuthenticationPrincipal UserDetails userDetails){
        return userDetails.getUsername();
    }
}
