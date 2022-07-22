package cn.tedu.knows.portal.mapper;

import cn.tedu.knows.portal.model.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
* <p>
    *  Mapper 接口
    * </p>
*
* @author tedu.cn
* @since 2022-03-04
*/
    @Repository
    public interface CommentMapper extends BaseMapper<Comment> {
    @Select("delete from comment where comment.id in ((select comment.id from comment left join answer on answer.id = comment.answer_id where answer.id = #{id}))")
    void deleteAllCommentsByAnswerId(Integer id);
}
