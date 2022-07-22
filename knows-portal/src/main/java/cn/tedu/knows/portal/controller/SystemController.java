package cn.tedu.knows.portal.controller;
import cn.tedu.knows.portal.model.User;
import cn.tedu.knows.portal.service.IUserService;
import cn.tedu.knows.portal.vo.RegisterVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
//lombok提供的注解@Slf4j,它能够在当前类中添加一个记录日志的对象叫log
//当前类中的任何方法都可以使用log对象输出日志信息
@Slf4j
public class SystemController {
    @Autowired
    private IUserService userService;


    @PostMapping("/register")
    public String register(
            @Validated RegisterVO registerVO, BindingResult result) {

        //使用  @Slf4j 提供的log对象将 registerVO 信息输出到日志

           if(result.hasErrors()){
               return result.getFieldError().getDefaultMessage();
           }
               userService.registerStudent(registerVO);
               return "ok";
    }
    @GetMapping("/getId")
    public int backId(){
        SecurityContext sc = SecurityContextHolder.getContext();
        Authentication auth = sc.getAuthentication();
        User user = (User) auth.getPrincipal();
        return user.getId();
    }
    @Value("${knows.resource.path}")
    private File resourcePath;
    @Value("${knows.resource.host}")
    private String resourceHost;

    // 上传文件必须是post请求
    @PostMapping("/upload/file")
    public String upload(MultipartFile imageFile) throws IOException {
        // 我们要确定文件保存的位置
        // 文件保存的路径是 D:/upload/年/月/日
        // 先获得当前日期的字符串做路径
        String path = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDateTime.now());
        // 确定要上传的文件夹
        File folder = new File(resourcePath,path);
        // 创建这个文件夹
        folder.mkdirs();
        // 下面要确定上传的文件名
        // 获得文件原名,以便获取后缀名
        String fileName = imageFile.getOriginalFilename();
        fileName = fileName.substring(fileName.lastIndexOf("."));
        System.out.println(fileName);
        String name = UUID.randomUUID().toString()+fileName;
        File newFile = new File(folder,name);
        log.debug("文件上传到:{}",newFile.getAbsolutePath());
        imageFile.transferTo(newFile);
        // 为了显示回显,我们需要返回可以访问上传的图片的路径
        // 我们上传的图片要想访问,需要访问静态资源服务器的路径,可能的格式如下
        // http://localhost:8899/2022/03/23/xxx-xxx-xxx.jpg
        String url = resourceHost+"/"+path+"/"+name;
        log.debug("回显图片的路径为:{}",url);
        // 返回成功上传文件的静态资源服务器路径
        return url;
    }


}
