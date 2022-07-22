package cn.tedu.knows.portal.mapper;

import cn.tedu.knows.portal.model.Notice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
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
    public interface NoticeMapper extends BaseMapper<Notice> {
    @Select("select question_id,COUNT(notice.user_id) replyCount from notice LEFT JOIN question on notice.\n" +
            "            question_id = question.id where question.delete_status = 0 GROUP BY question_id ORDER BY question.page_views desc limit 0,10 ")
    List<Notice> selectNotice();
}
