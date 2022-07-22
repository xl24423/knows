package cn.tedu.knows.portal.service;

import cn.tedu.knows.portal.model.Comment;
import cn.tedu.knows.portal.vo.CommentVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
public interface ICommentService extends IService<Comment> {

    Comment saveComment(CommentVO commentVO, String userName);

    List<Comment> getCommentByAnswerId(Integer id);

    Integer deleteByCommentId(Integer id);

    Comment updateComment(Integer id, CommentVO commentVO, String username);
}
