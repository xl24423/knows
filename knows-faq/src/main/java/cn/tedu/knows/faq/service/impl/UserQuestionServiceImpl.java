package cn.tedu.knows.faq.service.impl;


import cn.tedu.knows.commons.model.UserQuestion;
import cn.tedu.knows.faq.mapper.UserQuestionMapper;
import cn.tedu.knows.faq.service.IUserQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
@Service
public class UserQuestionServiceImpl extends ServiceImpl<UserQuestionMapper, UserQuestion> implements IUserQuestionService {

}
