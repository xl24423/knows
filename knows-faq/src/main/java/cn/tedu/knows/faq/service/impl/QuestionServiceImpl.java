package cn.tedu.knows.faq.service.impl;


import cn.tedu.knows.commons.exception.ServiceException;
import cn.tedu.knows.commons.model.*;
import cn.tedu.knows.faq.mapper.QuestionMapper;
import cn.tedu.knows.faq.mapper.QuestionTagMapper;
import cn.tedu.knows.faq.mapper.UserQuestionMapper;
import cn.tedu.knows.faq.service.IQuestionService;
import cn.tedu.knows.faq.service.ITagService;
import cn.tedu.knows.faq.vo.QuestionVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

    @Autowired
    private QuestionMapper questionMapper;
//    @Autowired
//    private UserMapper userMapper;
    @Value(value = "tagImage")
    String imgSrc;
    @Autowired
    private QuestionTagMapper questionTagMapper;
    @Autowired
    private UserQuestionMapper userQuestionMapper;
//    @Autowired
//    private AnswerMapper answerMapper;
//    @Autowired
//    private UserCollectMapper userCollectMapper;

    @Override
    public PageInfo<Question> getMyQuestions(String username, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        // 执行查询
        List<Question> list = questionMapper.getMyQuestions(username);
        //遍历查询到的所有问题对象
        for (Question q : list) {
            //将当前对象的tagNames转换为List<Tag>
            List<Tag> tags = tagNamesToTags(q.getTagNames());
            Tag tag = tags.get(0);
            q.setTamImage(imgSrc + "/" + tag.getId() + ".jpg");
            q.setTags(tags);
        }
        PageInfo<Question> pageInfo = new PageInfo<>(list);
        // 别忘了返回list
        return pageInfo;
    }

    // 编写TagNames转换为List<Tag>的方法
    // 需要获得ITagService业务逻辑层中的缓存Map
    @Autowired
    private ITagService tagService;

    private List<Tag> tagNamesToTags(String tagNames) {
        //tagNames:"Java基础,Java SE,面试题"
        String[] names = tagNames.split(",");
        // names:{"Java基础","Java SE","面试题"}
        Map<String, Tag> tagMap = tagService.getTagMap();
        // 循环遍历之前声明一个用于接收返回值的List
        List<Tag> tags = new ArrayList<>();
        // 遍历当前的names数组
        for (String name : names) {
            // 根据标签名称获得标签对象
            Tag t = tagMap.get(name);
            // 将获得的标签对象赋值到tags中
            tags.add(t);
        }
        // 返回tags
        return tags;
    }

    // Spring声明事务
    // 标记之后,当前被标记的方法中所有数据库操作保存在一个事务中
    // 方法运行正常,事务提交,数据库操作生效,方法运行发生异常,事务回滚,数据库操作撤销
    // 今后只要是包含两个及两个以上的数据库增删改操作的方法,都必须加该注解
    @Transactional
    public void saveQuestion(QuestionVO questionVO, String username) {
        // 1.根据用户名获取用户信息
//        User user = userMapper.findUserByUsername(username);
        // 2.将用户选中的标签数组拼接为标签名称字符串
        // {"Java基础","JavaSE","面试题",""}
        // 目标"Java基础, JavaSE, 面试题, "
        StringBuilder sb = new StringBuilder();
        for (String tagName : questionVO.getTagNames()) {
            sb.append(tagName).append(",");  // 这里最后会多出来一个逗号
        }
        sb.deleteCharAt(sb.length() - 1);      // 去掉这个逗号
        // 3.收集Question信息,实例化并赋值
        Question question = new Question().
                setTitle(questionVO.getTitle()).
                setContent(questionVO.getContent()).
//                setUserNickName(user.getNickname()).
//                setUserId(user.getId()).
                setCreatetime(LocalDateTime.now()).
                setStatus(0).
                setPageViews(0).
                setPublicStatus(0).
                setDeleteStatus(0).
                setTagNames(sb.toString());
        // 4.新增question到数据库
        int insert = questionMapper.insert(question);
        if (insert != 1) {
            throw new ServiceException("数据库忙");
        }
        // 5.新增question_tag关系表
        Map<String, Tag> tagMap = tagService.getTagMap();
        for (String tagName : questionVO.getTagNames()) {
            //遍历用户选中的所有标签
            Tag t = tagMap.get(tagName);
            //实例化QuestionTag实体类
            QuestionTag questionTag = new QuestionTag();
            questionTag.setQuestionId(question.getId()).
                    setTagId(t.getId());
            insert = questionTagMapper.insert(questionTag);
            if (insert != 1) {
                throw new ServiceException("数据库忙");
            }
        }
        // 6.新增user_question 学生关系表
        UserQuestion userQuestion = new UserQuestion().
//                setUserId(user.getId()).
                setQuestionId(question.getId()).
                setCreatetime(LocalDateTime.now());
        insert = userQuestionMapper.insert(userQuestion);
        if (insert != 1) {
            throw new ServiceException("数据库忙");
        }
        // 6.新增user_question 老师关系表
        for (String teacher : questionVO.getTeacherNicknames()){
//            User t = userMapper.findUserByNickName(teacher);
            userQuestion = new UserQuestion().
//                    setUserId(t.getId()).
                    setQuestionId(question.getId()).
                    setCreatetime(LocalDateTime.now());
            insert = userQuestionMapper.insert(userQuestion);
            if (insert != 1) {
                throw new ServiceException("数据库忙");
            }

        }
    }
//
//    @Autowired
//    NoticeMapper noticeMapper;

    @Override
    public List<Question> viewsQuestions() {
        // 得到浏览最多的问题
        List<Question> questions = questionMapper.selectViewsQuestions();
        // 得到浏览最多的问题的回答数量
//        List<Notice> notices = noticeMapper.selectNotice();
        // 将问题 id 和 回答数量 保存在 map中
        Map<Integer, Integer> map = new ConcurrentHashMap<>();
//        for (Notice n : notices) {
//            map.put(n.getQuestionId(), n.getReplyCount());
//        }
        // 遍历浏览最多的问题的集合
        for (Question q : questions) {
            // 将 对应问题id 的 浏览量 设置到问题 model中
            q.setReplyCount(map.get(q.getId()));
        }
        return questions;
    }

    @Override
    public PageInfo<Question> getTagQuestions(Integer id, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Question> list = questionMapper.getTagQuestions(id);
        //遍历查询到的所有问题对象
        for (Question q : list) {
            //将当前对象的tagNames转换为List<Tag>
            List<Tag> tags = tagNamesToTags(q.getTagNames());
            Tag tag = tags.get(0);
            q.setTamImage(imgSrc + "/" + tag.getId() + ".jpg");
            q.setTags(tags);
        }
        // 别忘了返回list
        return new PageInfo<>(list);
    }

    @Override
    public List<Tag> getIdTag(Integer id) {
        return questionMapper.getIdTag(id);
    }

    @Override
    public PageInfo<Question> getAllQuestions(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Question> list = questionMapper.getAllQuestions();
        //遍历查询到的所有问题对象
        for (Question q : list) {
            //将当前对象的tagNames转换为List<Tag>
            List<Tag> tags = tagNamesToTags(q.getTagNames());
            Tag tag = tags.get(0);
            q.setTamImage(imgSrc + "/" + tag.getId() + ".jpg");
            q.setTags(tags);
        }
        // 别忘了返回list
        return new PageInfo<>(list);
    }

//    @Override
    public PageInfo<Question> getTeacherQuestions(String username, Integer pageNum, Integer pageSize) {
//        User userByUsername = userMapper.findUserByUsername(username);
//        PageHelper.startPage(pageNum, pageSize);
//        List<Question> questionList = questionMapper.findTeacherQuestions(userByUsername.getId());
//        for (Question question : questionList){
//            List<Tag> tags = tagNamesToTags(question.getTagNames());
//            question.setTags(tags);
//        }
//        PageInfo<Question> pageInfo = new PageInfo<>(questionList);
//        return pageInfo;
        return null;
    }

    // 问题详情页回显数据
//    @Override
    public Question getQuestionById(Integer id) {
//        Question question = questionMapper.selectById(id);
//        List<Tag> tags = tagNamesToTags(question.getTagNames());
//        question.setTags(tags);
//        List<Answer> answersByQuestionId = answerMapper.findAnswersByQuestionId(question.getId());
//        question.setReplyCount(answersByQuestionId.size());
//        Integer collectCount = userCollectMapper.countCollectionsByQuestionId(question.getId());
//        question.setCollectCount(collectCount);
//        return question;
       return null;
    }


}
