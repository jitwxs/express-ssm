package jit.wxs.express.service;

import com.baomidou.mybatisplus.service.IService;
import jit.wxs.express.pojo.ExpressPayment;

import java.util.Map;

/**
 * <p>
 * 订单支付表 服务类
 * </p>
 *
 * @author jitwxs
 * @since 2018-04-23
 */
public interface ExpressPaymentService extends IService<ExpressPayment> {
    /**
     * 创建线下订单
     * @author jitwxs
     * @since 2018/6/11 16:38
     */
    ExpressPayment createOfflinePayment(String expressId, Double money);

    /**
     * 创建支付宝订单
     * @author jitwxs
     * @since 2018/6/11 17:26
     */
    ExpressPayment createAliPayment(String expressId, Double money, String sellerId);

    boolean validAlipay(Map<String,String> params) throws Exception;

    /**
     * 改变订单状态
     * @param tradeNo 第三方交易号【仅在TRADE_SUCCESS时有效】
     * @author jitwxs
     * @since 2018/6/4 22:42
     */
    boolean updateStatus(String orderId, int status, String... tradeNo);
}
