package cn.tedu.knows.portal.mapper;

import cn.tedu.knows.portal.model.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* <p>
    *  Mapper 接口
    * </p>
*
* @author tedu.cn
* @since 2022-03-04
*/
    @Repository
    public interface PermissionMapper extends BaseMapper<Permission> {
    @Select("select p.name from permission p left join role_permission rp on p.id = rp.permission_id where rp.role_id in (select ur.role_id from user_role ur left join user u on u.id = ur.user_id where u.username = #{username}\n" +
            " )")
    List<Permission> allPermissionByUserName(String username);
}
