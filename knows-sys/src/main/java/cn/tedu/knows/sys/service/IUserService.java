package cn.tedu.knows.sys.service;


import cn.tedu.knows.commons.model.Permission;
import cn.tedu.knows.commons.model.Question;
import cn.tedu.knows.commons.model.Role;
import cn.tedu.knows.commons.model.User;
import cn.tedu.knows.sys.vo.RegisterVO;
import cn.tedu.knows.sys.vo.UserVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
public interface IUserService extends IService<User> {
    //定义学生注册的功能的业务逻辑代码
    void registerStudent(RegisterVO registerVO);
    //查询所有讲师的业务逻辑代码
    List<User> findAllTeachers();
    //查询所有讲师的Map的业务逻辑代码
    Map<String,User> getTeacherMap();

    List<Question> getMyCollectQuestions();

    UserVO getUserVO(String username);

    // 根据用户名返回用户对象
    User getUserByUsername(String name);
    // 根据用户id获取所有权限
    List<Permission> getPermissionsByUserId(Integer id);
    // 根据用户id获取所有角色
    List<Role> getRolesByUserId(Integer id);
}
