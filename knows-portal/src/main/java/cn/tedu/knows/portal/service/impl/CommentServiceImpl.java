package cn.tedu.knows.portal.service.impl;

import cn.tedu.knows.portal.exception.ServiceException;
import cn.tedu.knows.portal.mapper.UserMapper;
import cn.tedu.knows.portal.model.Comment;
import cn.tedu.knows.portal.mapper.CommentMapper;
import cn.tedu.knows.portal.model.User;
import cn.tedu.knows.portal.service.ICommentService;
import cn.tedu.knows.portal.vo.CommentVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public Comment saveComment(CommentVO commentVO, String userName) {
        User user = userMapper.selectByUserName(userName);
        if (user == null) {
            throw new ServiceException("当前用户不存在");
        }
        Comment comment = new Comment();
        comment.setUserId(user.getId()).
                setAnswerId(commentVO.getAnswerId()).
                setContent(commentVO.getContent()).
                setCreatetime(LocalDateTime.now()).
                setUserNickName(user.getNickname());
        int insert = commentMapper.insert(comment);
        if (insert != 1) {
            throw new ServiceException("新增评论失败");
        }
        return comment;
    }

    @Override
    public List<Comment> getCommentByAnswerId(Integer id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("answer_id", id);
        return commentMapper.selectList(queryWrapper);
    }

    @Override
    public Integer deleteByCommentId(Integer id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", id);
        int delete = commentMapper.delete(queryWrapper);
        if (delete != 1) {
            throw new ServiceException("删除失败,服务器出错");
        }
        return delete;
    }

    @Override
    @Transactional
    public Comment updateComment(Integer commentId, CommentVO commentVO, String username) {
        User user = userMapper.findUserByComment(commentId);
        if (!user.getUsername().equals(username)) {
            throw new ServiceException("你没有权限修改");
        } else {
            Comment comment = commentMapper.selectById(commentId);
            comment.setContent(commentVO.getContent());
            comment.setCreatetime(LocalDateTime.now());
            int i = commentMapper.updateById(comment);
            if (i != 1) {
                throw new ServiceException("数据库忙");
            }
            return comment;
        }
    }
}
