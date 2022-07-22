package cn.tedu.knows.portal.service;

import cn.tedu.knows.portal.model.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
public interface IPermissionService extends IService<Permission> {

    boolean isHasAnswerPermissionByUserName(String username);

}
