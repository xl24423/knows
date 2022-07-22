package cn.tedu.knows.portal.mapper;

import cn.tedu.knows.portal.model.Answer;
import cn.tedu.knows.portal.model.Question;
import cn.tedu.knows.portal.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
@Repository
public interface AnswerMapper extends BaseMapper<Answer> {
    // 对应AnswerMapper.xml文件中的内容
    // 根据问题id查询所有回答以及回答包含的评论方法
    // 方法名必须和xml文件中 <select> 标签的id 一致
    List<Answer> findAnswersByQuestionId(Integer questionId);

    User findUserByAnswerId(Integer id);

    int updateAcceptStatus(@Param("acceptStatus") Integer acceptStatus,@Param("answerId") Integer answerId);

}
