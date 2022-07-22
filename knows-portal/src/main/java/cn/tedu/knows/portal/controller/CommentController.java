package cn.tedu.knows.portal.controller;


import cn.tedu.knows.portal.exception.ServiceException;
import cn.tedu.knows.portal.model.Answer;
import cn.tedu.knows.portal.model.Comment;

import cn.tedu.knows.portal.service.IAnswerService;
import cn.tedu.knows.portal.service.ICommentService;
import cn.tedu.knows.portal.service.IUserService;
import cn.tedu.knows.portal.vo.CommentVO;
import cn.tedu.knows.portal.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
@RestController
@RequestMapping("/v1/comment")
@Slf4j
public class CommentController {
    @Autowired
    IAnswerService answerService;
    @Autowired
    private ICommentService commentService;
    @Autowired
    private IUserService userService;

    @PostMapping("")
    public Comment postComment(@Validated CommentVO commentVO,
                               BindingResult result, // BindingResult 一般出现在对 form表单的新增进行 @Validated 验证 modelVO
                               @AuthenticationPrincipal UserDetails userDetails
    ) {
        log.debug("接收到了表单信息" + commentVO);
        if (result.hasErrors()) {
            String msg = result.getFieldError().getDefaultMessage();
            throw new ServiceException(msg);
        }
        UserVO userVO = userService.getUserVO(userDetails.getUsername());
        Comment comment = commentService.saveComment(commentVO, userVO.getUsername());
        return comment;
    }

    @GetMapping("/commentEditPermissionByNowUser")
    public Map<Integer, Boolean> commentEditPermissionByNowUser(@AuthenticationPrincipal UserDetails userDetails,
                                                                Integer id
    ) {
        Map<Integer, Boolean> map = new HashMap<>();
        List<Answer> answersByQuestionId = answerService.getAnswersByQuestionId(id);
        List<Comment> allComment = new ArrayList<>();
        UserVO userVO = userService.getUserVO(userDetails.getUsername());
        for (Answer answer : answersByQuestionId) {
            List<Comment> commentList = commentService.getCommentByAnswerId(answer.getId());
            allComment.addAll(commentList);
        }
        boolean permission;
        for (Comment comment : allComment) {
            permission = comment.getUserId().equals(userVO.getId());
            map.put(comment.getId(), permission);
        }
        return map;
    }

    @DeleteMapping("/deleteByCommentId")
    public Integer deleteByCommentId(Integer id) {
        return commentService.deleteByCommentId(id);
    }
    @PostMapping("/update/{id}")
    public Comment updateComment(@Validated CommentVO commentVO,
                                 BindingResult result,
                                 @PathVariable Integer id,
                                 @AuthenticationPrincipal UserDetails userDetails){
        log.debug("表单信息"+commentVO);
        log.debug(commentVO.getContent().toString());
        log.debug("id"+id);
        Comment comment = commentService.updateComment(id,commentVO,userDetails.getUsername());


        return comment;
    }
}