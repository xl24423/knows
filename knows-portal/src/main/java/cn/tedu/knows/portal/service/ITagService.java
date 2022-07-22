package cn.tedu.knows.portal.service;

import cn.tedu.knows.portal.model.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
public interface ITagService extends IService<Tag> {
    List<Tag> getTags();
    Map<String,Tag> getTagMap();
}
