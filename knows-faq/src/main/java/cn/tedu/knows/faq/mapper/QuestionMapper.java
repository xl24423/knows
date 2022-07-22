package cn.tedu.knows.faq.mapper;


import cn.tedu.knows.commons.model.Question;
import cn.tedu.knows.commons.model.Tag;
import cn.tedu.knows.commons.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* <p>
    *  Mapper 接口
    * </p>
*
* @author tedu.cn
* @since 2022-03-04
*/
    @Repository
    public interface QuestionMapper extends BaseMapper<Question> {
    @Select("select * from question where delete_status=0 order by page_views desc limit 0,10 ")
    List<Question> selectViewsQuestions();
    @Select("select count(id) from question where user_id = #{id} and delete_status = 0")
    int countQuestionsByUserId(Integer id);
    @Select(
            "select * from question q left join question_tag qt on q.id = qt.question_id LEFT JOIN tag t on t.id = qt.tag_id WHERE t.id = #{id} and q.public_status = 1"
    )
    List<Question> getTagQuestions(Integer id);
    @Select("select name from tag where id = #{id} ")
    List<Tag> getIdTag(Integer id);
    @Select("select * from question")
    List<Question> getAllQuestions();
    @Select("select * from question q left join user u on u.id =  q.user_id where username = #{username}")
    List<Question> getMyQuestions(String username);
    @Select("select distinct q.* from question q left join user_question uq on uq.question_id = q.id where uq.user_id = #{id} or q.user_id\n" +
            "= #{id} ORDER BY q.createtime desc")
    List<Question> findTeacherQuestions(Integer userId);
    @Update("update question set status=#{status} where id=#{questionId}")
    int updateStatus(@Param("status") Integer status, @Param("questionId") Integer questionId);
    @Select("select * from user u left join question q on q.user_id = u.id where q.id = #{id}")
    User selectUserByQuestionId(@Param("id") Integer id);
}
