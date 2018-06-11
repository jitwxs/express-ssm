package jit.wxs.express.service;

import com.baomidou.mybatisplus.service.IService;
import jit.wxs.express.pojo.Express;

/**
 * <p>
 * 订单信息表 服务类
 * </p>
 *
 * @author jitwxs
 * @since 2018-05-02
 */
public interface ExpressService extends IService<Express> {

    /**
     * 创建订单
     * @return 订单号
     * @author jitwxs
     * @since 2018/6/11 16:32
     */
    String createExpress(Express express);
}
