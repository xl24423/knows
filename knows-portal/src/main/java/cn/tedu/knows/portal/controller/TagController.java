package cn.tedu.knows.portal.controller;


import cn.tedu.knows.portal.model.Tag;
import cn.tedu.knows.portal.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/v1/tags")
public class TagController {
     //添加业务逻辑层的依赖注入
    @Autowired
    private ITagService tagService;
    @GetMapping("") // @GetMapping("") 这个写法的含义指 localhost:8080/v1/tags
    public List<Tag> getTags(){
        return tagService.getTags();
    }
}
