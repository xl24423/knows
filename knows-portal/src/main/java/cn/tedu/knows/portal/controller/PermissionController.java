package cn.tedu.knows.portal.controller;


import cn.tedu.knows.portal.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
@RestController
@RequestMapping("/v1/permission")
public class PermissionController {
    @Autowired
    IPermissionService permissionService;
    @GetMapping("/hasAnswerPermission")
    public boolean IsHasAnswerPermission(@AuthenticationPrincipal UserDetails userDetails) throws InterruptedException {
       return permissionService.isHasAnswerPermissionByUserName(userDetails.getUsername());
    }
}
