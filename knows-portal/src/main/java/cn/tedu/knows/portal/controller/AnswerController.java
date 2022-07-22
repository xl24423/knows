package cn.tedu.knows.portal.controller;


import cn.tedu.knows.portal.exception.ServiceException;
import cn.tedu.knows.portal.model.Answer;
import cn.tedu.knows.portal.service.IAnswerService;
import cn.tedu.knows.portal.service.IPermissionService;
import cn.tedu.knows.portal.service.IUserService;
import cn.tedu.knows.portal.vo.AnswerVO;
import cn.tedu.knows.portal.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
@RestController
@RequestMapping("/v1/answer")
@Slf4j
public class AnswerController {
    @Autowired
    IAnswerService answerService;
    @Autowired
    IPermissionService permissionService;
    @Autowired
    IUserService userService;

    // 得到所有的回答
    @GetMapping("/getAnswersByQuestionId")
    public List<Answer> getAnswersByQuestionId(Integer id) {
        List<Answer> answersByQuestionId = answerService.getAnswersByQuestionId(id);

        return answersByQuestionId;
    }

    @GetMapping("/getAnswerNum")
    public Integer backAnswerNum(Integer id) {
        int size = answerService.getAnswersByQuestionId(id).size();
        if (size <= 0) {
            throw new ServiceException("该问题还没有回答,请耐心等待老师回答或回答学生问题");
        }
        return size;
    }
    @DeleteMapping("/deleteAnswer")
    public Integer deleteAnswerById(Integer id,@AuthenticationPrincipal UserDetails userDetails)
    {
        UserVO userVO = userService.getUserVO(userDetails.getUsername());
        Answer answer = answerService.getAnswerById(id);
        if (answer.getUserId().equals(userVO.getId())){
            return answerService.deleteAnswerById(id);
        }else {
            throw new ServiceException("你没有权限删除");
        }
    }


    @PostMapping("")
    @PreAuthorize("hasAuthority('/question/answer')")   // 回答控制器
    public Answer postAnswer(@Validated AnswerVO answerVO,  // AnswerVO 加上 @Validated 用来验证字段是否为空之类
                             BindingResult result,  // BindingResult 用于接收验证的结果
                             @AuthenticationPrincipal UserDetails userDetails   // 用来接收当前回答用户
    ) {

        if (result.hasErrors()) {
            String defaultMessage = result.getFieldError().getDefaultMessage();
            throw new ServiceException(defaultMessage);
        }
        Answer answer = answerService.saveAnswers(answerVO, userDetails.getUsername());
        return answer;
    }

    @GetMapping("/editPermissionByNowUser")
    public Map<Integer,Boolean> editPermissionByNowUser(@AuthenticationPrincipal UserDetails userDetails, Integer id) {
        Map<Integer,Boolean> map = new ConcurrentHashMap<>();
        List<Answer> answersByQuestionId = answerService.getAnswersByQuestionId(id);
        boolean permission;
        for (Answer answer : answersByQuestionId) {
            Integer userId = userService.getUserVO(userDetails.getUsername()).getId();
            permission = userId.equals(answer.getUserId());
            map.put(answer.getId(),permission);
        }
        return map;
    }
    @GetMapping("/{answerId}/solved")
    public String questionSolved(@PathVariable Integer answerId ,  @AuthenticationPrincipal UserDetails userDetails){
        log.debug("要采纳的问题id:"+answerId+",用户的名字是:"+userDetails.getUsername());
        boolean accept = answerService.accept(answerId, userDetails.getUsername());
        if (accept){
            return "采纳完成";
        }
            return "您不能采纳别人的问题";
    }

}
