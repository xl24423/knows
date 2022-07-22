package cn.tedu.knows.portal.mapper;

import cn.tedu.knows.portal.model.UserCollect;
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
    public interface UserCollectMapper extends BaseMapper<UserCollect> {
    @Select("select count(id) from user_collect where user_id = #{id}")
    Integer countCollectionsByUserId(Integer id);
    @Select("select count(*) from user_collect uc left join question q on uc.question_id = q.id where q.id = #{id}")
    Integer countCollectionsByQuestionId(Integer id);
}
