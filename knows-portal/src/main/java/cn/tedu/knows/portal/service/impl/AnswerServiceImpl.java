package cn.tedu.knows.portal.service.impl;

import cn.tedu.knows.portal.exception.ServiceException;
import cn.tedu.knows.portal.mapper.CommentMapper;
import cn.tedu.knows.portal.mapper.QuestionMapper;
import cn.tedu.knows.portal.mapper.UserMapper;
import cn.tedu.knows.portal.model.Answer;
import cn.tedu.knows.portal.mapper.AnswerMapper;
import cn.tedu.knows.portal.model.Question;
import cn.tedu.knows.portal.model.User;
import cn.tedu.knows.portal.service.IAnswerService;
import cn.tedu.knows.portal.vo.AnswerVO;
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
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements IAnswerService {
    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public List<Answer> getAnswersByQuestionId(Integer id) {
        List<Answer> answersByQuestionId = answerMapper.findAnswersByQuestionId(id);
        return answersByQuestionId;
    }

    @Transactional
    @Override
    public Answer saveAnswers(AnswerVO answerVO, String username) {
        User user = userMapper.findUserByUsername(username);
        Answer answer = new Answer();
        answer.setContent(answerVO.getContent()).
                setUserId(user.getId()).
                setQuestId(answerVO.getQuestionId()).
                setUserNickName(user.getNickname()).
                setAcceptStatus(0).
                setLikeCount(0).
                setCreatetime(LocalDateTime.now());
        int insert = answerMapper.insert(answer);
        if (insert != 1) {
            throw new ServiceException("数据库异常");
        }
        return answer;

    }

    @Override
    public Integer deleteAnswerById(Integer id) {
        commentMapper.deleteAllCommentsByAnswerId(id);  // 先删除回答下面的所有评论

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", id);
        int delete = answerMapper.delete(queryWrapper);
        if (delete != 1) {
            throw new ServiceException("服务器异常,请联系管理员");
        }
        return delete;
    }

    @Override
    public Answer getAnswerById(Integer id) {
        return answerMapper.selectById(id);
    }

    @Override
    public User findUserByAnswerId(Integer answerId) {
        return answerMapper.findUserByAnswerId(answerId);
    }

    @Transactional
    @Override
    public boolean accept(Integer answerId, String username) {
        Answer answer = answerMapper.selectById(answerId);
        log.debug("AAA:"+answer);
        Question question = questionMapper.selectById(answer.getQuestId());
        log.debug("BBB:"+question);
        User user = questionMapper.selectUserByQuestionId(question.getId());
        log.debug("CCC"+user);
        boolean equals = user.getUsername().equals(username);
        if (!equals) {
            throw new ServiceException("你没有权限这么做");
        }
        int i = answerMapper.updateAcceptStatus(1, answerId);
        if (i != 1) {
            throw new ServiceException("数据库异常,请稍后再试");
        }

        int i1 = questionMapper.updateStatus(Question.SOLVED, question.getId());
        if (i1 != 1) {
            throw new ServiceException("数据库异常,请稍后再试");
        }
        log.debug("接收到了参数:" + answerId + "," + username);
        return true;
    }
}
