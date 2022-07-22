package cn.tedu.knows.sys.mapper;


import cn.tedu.knows.commons.model.Permission;
import cn.tedu.knows.commons.model.Question;
import cn.tedu.knows.commons.model.Role;
import cn.tedu.knows.commons.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
@Repository
public interface UserMapper extends BaseMapper<User> {
     //根据用户id查询用户所有权限的方法
    @Select("select p.id,p.name from `user` u\n" +
            "left join user_role ur on u.id =ur.user_id \n" +
            "left join role r on r.id = ur.role_id\n" +
            "left join role_permission rp on rp.role_id = r.id\n" +
            "left join permission p on p.id = rp.permission_id\n" +
            "                                   where u.id = #{userId}")
    public List<Permission> selectPermissionsByUserId(int userId);
    @Select("select * from user where username=#{username}")
    public User selectByUserName(String username);
    // 根据用户名查询用户信息
    @Select("select * from user where username=#{username}")
    User findUserByUsername(String username);
    //查询所有讲师的方法
    @Select("select * from user where type=1")
    List<User> findTeachers();
    @Select("select * from ")
    List<Question> getMyCollectQuestions();
    @Select("SELECT r.id,r.name from user u left join user_role ur on ur.user_id = u.id left join role r\n" +
            "on r.id = ur.role_id\n" +
            "where u.id = #{id}")
    List<Role> findUserRoleById(Integer id);
    @Select("select * from user where nickname = #{teacher}")
    User findUserByNickName(String teacher);
    @Select("select * from user u left join comment c on u.id = c.user_id where c.id = #{id}")
    User findUserByComment(Integer id);
}
