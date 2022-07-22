package cn.tedu.knows.faq.controller;


import cn.tedu.knows.commons.model.Tag;
import cn.tedu.knows.faq.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@CrossOrigin(origins = "*",allowCredentials = "true",maxAge = 3600)
@RequestMapping(value = "/v2/tags")
public class TagController {
     //添加业务逻辑层的依赖注入
    @Autowired
    private ITagService tagService;
    @GetMapping("") // @GetMapping("") 这个写法的含义指 localhost:8080/v1/tags
    public List<Tag> getTags(){
        return tagService.getTags();
    }
}
