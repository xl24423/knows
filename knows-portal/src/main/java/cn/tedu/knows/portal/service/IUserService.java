package cn.tedu.knows.portal.service;

import cn.tedu.knows.portal.model.Question;
import cn.tedu.knows.portal.model.User;
import cn.tedu.knows.portal.vo.RegisterVO;
import cn.tedu.knows.portal.vo.UserVO;
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
    List<User> findAllTeahers();
    //查询所有讲师的Map的业务逻辑代码
    Map<String,User> getTeacherMap();

    List<Question> getMyCollectQuestions();

    UserVO getUserVO(String username);

}
