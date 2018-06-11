package jit.wxs.express.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import jit.wxs.express.enums.ExpressStatusEnum;
import jit.wxs.express.mapper.ExpressMapper;
import jit.wxs.express.pojo.Express;
import jit.wxs.express.service.ExpressService;
import jit.wxs.express.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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
    @Autowired
    private ExpressMapper expressMapper;

    @Override
    public String createExpress(Express express) {
        // 生成订单号
        String expressId = RandomUtils.timeId();

        express.setId(expressId);
        express.setStatus(ExpressStatusEnum.WAIT_DIST.getIndex());
        express.setCreateDate(new Date());

        expressMapper.insert(express);

        return expressId;
    }
}
