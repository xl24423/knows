package cn.tedu.knows.portal.service;

import cn.tedu.knows.portal.model.Answer;
import cn.tedu.knows.portal.model.User;
import cn.tedu.knows.portal.vo.AnswerVO;
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
public interface IAnswerService extends IService<Answer> {

    List<Answer> getAnswersByQuestionId(Integer id);

    Answer saveAnswers(AnswerVO answerVO, String username);

    Integer deleteAnswerById(Integer id);

    Answer getAnswerById(Integer id);

    User findUserByAnswerId(Integer answerId);

    boolean accept(Integer answerId, String username);
}
