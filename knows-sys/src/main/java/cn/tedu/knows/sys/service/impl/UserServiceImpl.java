package cn.tedu.knows.sys.service.impl;


import cn.tedu.knows.commons.exception.ServiceException;
import cn.tedu.knows.commons.model.*;
import cn.tedu.knows.sys.mapper.ClassroomMapper;
import cn.tedu.knows.sys.mapper.UserMapper;
import cn.tedu.knows.sys.mapper.UserRoleMapper;
import cn.tedu.knows.sys.service.IUserService;
import cn.tedu.knows.sys.vo.RegisterVO;
import cn.tedu.knows.sys.vo.UserVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ClassroomMapper classroomMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Override
    public void registerStudent(RegisterVO registerVO) {
         // 1.根据用户输入的邀请码查询班级信息
        QueryWrapper<Classroom> query = new QueryWrapper<>();
        query.eq("invite_code",registerVO.getInviteCode());
        Classroom classroom = classroomMapper.selectOne(query);
        // 2.判断班级信息是否为空,如果为空则抛出异常
        if(classroom==null){
            throw new ServiceException("邀请马错误");
        }
         // 3.查询 用户输入的手机号,查询用户信息
         User user = userMapper.selectByUserName(registerVO.getPhone());
        // 4.查询到的用户信息不为空,证明手机号被注册了,抛出异常
        if(user!=null){
            throw new ServiceException("手机号已被注册");
        }
         // 5.将用户输入的密码 加密为 bcrypt
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String pwd = "{bcrypt}"+passwordEncoder.encode(registerVO.getPassword());
        // 6.实例化一个学生用户对象,像对象中赋值
        User student = new User();
        student.setUsername(registerVO.getPhone());
        student.setPassword(pwd);
        student.setNickname(registerVO.getNickname());
        student.setClassroomId(classroom.getId());
        student.setCreatetime(LocalDateTime.now());
        student.setEnabled(1);
        student.setLocked(0);
        student.setType(0);
         // 7.执行用户对象的新增
        int num = userMapper.insert(student);
        if(num!=1){
            throw new ServiceException("注册" +
                    "失败,服务器发生异常");
        }
         // 8.创建UserRole关系对象 ,并新增到数据库
        UserRole userRole = new UserRole().setRoleId(2).setUserId(student.getId());
        num = userRoleMapper.insert(userRole);
        if(num!=1){
            throw new ServiceException("注册失败,数据库异常");
        }


    }
    //声明两个讲师缓存的集合
    private List<User> teachers = new CopyOnWriteArrayList<>();
    private Map<String,User> teacherMap = new ConcurrentHashMap<>();
    @Override
    public List<User> findAllTeachers() {

        if(teachers.isEmpty()){
            synchronized (teachers){
                teachers.clear();
                teacherMap.clear();
                List<User> list = userMapper.findTeachers();
                teachers.addAll(list);
                for(User s:list){
                    teacherMap.put(s.getNickname(),s);
                }
            }
        }
        return teachers;
    }

    @Override
    public Map<String, User> getTeacherMap() {
        if(teacherMap.isEmpty()){
           findAllTeachers();
        }
        return teacherMap;
    }

    @Override
    public List<Question> getMyCollectQuestions() {
        return userMapper.getMyCollectQuestions();
    }



    @Override
    public UserVO getUserVO(String username) {
        User user = userMapper.selectByUserName(username);
        return new UserVO().
                setUsername(user.getUsername()).
                setNickname(user.getNickname()).
                setId(user.getId());

    }

    @Override
    public User getUserByUsername(String name) {
         return userMapper.findUserByUsername(name);
    }

    @Override
    public List<Permission> getPermissionsByUserId(Integer id) {
        return userMapper.selectPermissionsByUserId(id);
    }

    @Override
    public List<Role> getRolesByUserId(Integer id) {
        return userMapper.findUserRoleById(id);
    }

}
