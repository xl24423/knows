package cn.tedu.knows.faq.service;


import cn.tedu.knows.commons.model.Question;
import cn.tedu.knows.commons.model.Tag;
import cn.tedu.knows.faq.vo.QuestionVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

import java.rmi.ServerException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
public interface IQuestionService extends IService<Question> {

     PageInfo<Question> getMyQuestions(String username, Integer pageNum, Integer pageSize);
     void saveQuestion(QuestionVO questionVO, String username) throws ServerException;

     List<Question> viewsQuestions();

    PageInfo<Question> getTagQuestions(Integer id, Integer pageNum, Integer pageSize);

    List<Tag> getIdTag(Integer id);

    PageInfo<Question> getAllQuestions(Integer pageNum, Integer pageSize);

    PageInfo<Question> getTeacherQuestions(String username, Integer pageNum, Integer pageSize);

    Question getQuestionById(Integer id);
}
