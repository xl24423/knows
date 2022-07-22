package cn.tedu.knows.portal.service.impl;

import cn.tedu.knows.portal.model.Permission;
import cn.tedu.knows.portal.mapper.PermissionMapper;
import cn.tedu.knows.portal.service.IPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {
    @Autowired
    PermissionMapper permissionMapper;
    @Override
    public boolean isHasAnswerPermissionByUserName(String username) {
        List<Permission> permissions = permissionMapper.allPermissionByUserName(username);
        for (Permission permission : permissions){
            if (permission.getName().equals("/question/answer")){
                return true;
            }
        }
        return false;
    }
}
