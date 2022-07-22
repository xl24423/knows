package cn.tedu.knows.faq.controller;


import cn.tedu.knows.commons.model.Question;
import cn.tedu.knows.commons.model.Tag;
import cn.tedu.knows.faq.service.IQuestionService;
import cn.tedu.knows.faq.vo.QuestionVO;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerException;
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
@RequestMapping("/v2/questions")
@Slf4j
public class QuestionController {
    @Autowired
    private IQuestionService questionService;
    @GetMapping("/my")
    public PageInfo<Question> my(
            //@AuthenticationPrincipal注解效果
            // 从Spring-Security框架获得当前登录用户的UserDetails对象
            // 赋值给注解之后的参数
            @AuthenticationPrincipal UserDetails user, Integer pageNum, Integer pageSize
    ){
        PageInfo<Question> questions=questionService
                .getMyQuestions(user.getUsername(),pageNum,pageSize);
        // 返回业务逻辑层查询出的所有问题列表

        return questions;
    }
    @PostMapping("")
    public String createQuestion(
            @Validated QuestionVO questionVO,
            BindingResult result, @AuthenticationPrincipal UserDetails user
            ) throws ServerException {

        if(result.hasErrors()){
            return result.getFieldError().getDefaultMessage();
        }
        questionService.saveQuestion(questionVO,user.getUsername());
        return "ok";

    }
    @GetMapping("/viewsQuestions")
    public List<Question> viewsQuestions(){
        return questionService.viewsQuestions();
    }

    @GetMapping("/getIdQuestion")
    public PageInfo<Question> tagQuestions(Integer id, Integer pageNum, Integer pageSize){
        if (id==0){
           return questionService.getAllQuestions(pageNum, pageSize);
        }
        return questionService.getTagQuestions(id, pageNum, pageSize);
    }
    @GetMapping("/IdTag")
    public List<Tag> IdTag(Integer id){
        return questionService.getIdTag(id);
    }
    @GetMapping("/myQuestions")
    public PageInfo<Question> getMyQuestions(@AuthenticationPrincipal UserDetails userDetails, Integer pageNum, Integer pageSize){
        return questionService.getMyQuestions(userDetails.getUsername(),pageNum,pageSize);
    }
    @GetMapping("/teacher")
    // 必须有ROLE_TEACHER这个身份才能查询任务列表
    // 使用Spring-Security提供的权限\角色的验证功能,才能实现限制效果
    // PreAuthorize注解设置当前用户必须包含ROLE_TEACHER角色
    // 否则会发生没有权限访问的错误(403)
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public PageInfo<Question> teacher(@AuthenticationPrincipal UserDetails userDetails,
                                      Integer pageNum, Integer pageSize){
            if (pageNum==null){
                pageNum = 1;
            }
            if (pageSize == null){
                pageSize = 8;
            }
            PageInfo<Question> pageInfo = questionService.getTeacherQuestions(userDetails.getUsername(), pageNum, pageSize);
            log.debug("老师的问题"+pageInfo);
            return pageInfo;
    }
    @GetMapping("/questionDetail")
    public Question backQuestionDetail(Integer id){
        Question questionById = questionService.getQuestionById(id);

        return questionById;
    }

}
