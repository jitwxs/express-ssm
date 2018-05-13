package jit.wxs.express.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import jit.wxs.express.mapper.ExpressMapper;
import jit.wxs.express.pojo.Express;
import jit.wxs.express.service.ExpressService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单信息表 服务实现类
 * </p>
 *
 * @author jitwxs
 * @since 2018-05-02
 */
@Service
public class ExpressServiceImpl extends ServiceImpl<ExpressMapper, Express> implements ExpressService {
}
