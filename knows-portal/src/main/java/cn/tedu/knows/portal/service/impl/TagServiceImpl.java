package cn.tedu.knows.portal.service.impl;

import cn.tedu.knows.portal.model.Tag;
import cn.tedu.knows.portal.mapper.TagMapper;
import cn.tedu.knows.portal.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {
    @Autowired(required = false)
    TagMapper tagMapper;

    private List<Tag> tags = new CopyOnWriteArrayList<>();
    // tags属性用于充当保存所有标签的缓存对象
    // 因为TagServiceImpl默认是单例作用域的,所以tags属性也只有一份
    // 在今后任何方法的访问中,不会出现新的对象,重复占用内存
    // CopyOnWriteArrayList是jdk1.8开始支持的线程安全的集合对象
    private Map<String, Tag> tagMap = new ConcurrentHashMap();
    // ConcurrentHashMap是一个线程安全的Map集合类型对象,从jdk1.8开始


    @Override
    public List<Tag> getTags() {
        if (tags.isEmpty()) {
            synchronized (tags) {
                tags.clear();
                tagMap.clear();
                List<Tag> list = tagMapper.selectList(null);
                tags.addAll(list);
                for (Tag s : list) {
                    tagMap.put(s.getName(), s);
                }
                System.out.println("tags加载完毕");
            }
        }
        return tags;
    }

    @Override
    public Map<String, Tag> getTagMap() {
        // 判断tagMap是不是empty
        if (tagMap.isEmpty()) {
            // 如果tagMap是empty,证明getTags方法也没有运行过
            // 调用getTags方法为tagMap赋值即可
            getTags();
        }
        // 千万别忘了返回
        return tagMap;

    }
}
