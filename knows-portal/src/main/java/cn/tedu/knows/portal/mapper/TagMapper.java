package cn.tedu.knows.portal.mapper;

import cn.tedu.knows.portal.model.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface TagMapper extends BaseMapper<Tag> {
    //当前接口继承的BaseMapper接口
    //BaseMapper接口中包含了对指定表进行增删改查方法
    //而BaseMapper<T> 中的T就是指定的表
    //所以TagMapper会拥有父接口中包含的方法,既包含了对Tag表的基本增删改查
    //

}
