package jit.wxs.express.service.impl;

import jit.wxs.express.pojo.Feedback;
import jit.wxs.express.mapper.FeedbackMapper;
import jit.wxs.express.service.FeedbackService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户反馈表 服务实现类
 * </p>
 *
 * @author jitwxs
 * @since 2018-04-23
 */
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {

}
