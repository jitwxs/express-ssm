package jit.wxs.express.service.impl;

import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import jit.wxs.express.alipay.AliPaySetting;
import jit.wxs.express.enums.PaymentEnum;
import jit.wxs.express.enums.PaymentStatusEnum;
import jit.wxs.express.mapper.ExpressMapper;
import jit.wxs.express.mapper.ExpressPaymentMapper;
import jit.wxs.express.pojo.ExpressPayment;
import jit.wxs.express.service.ExpressPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 订单支付表 服务实现类
 * </p>
 *
 * @author jitwxs
 * @since 2018-04-23
 */
@Service
public class ExpressPaymentServiceImpl extends ServiceImpl<ExpressPaymentMapper, ExpressPayment> implements ExpressPaymentService {
    @Autowired
    private ExpressPaymentMapper paymentMapper;
    @Autowired
    private ExpressMapper expressMapper;
    @Autowired
    private AliPaySetting aliPaySetting;

    @Override
    public ExpressPayment createOfflinePayment(String expressId, Double money) {
        ExpressPayment payment = new ExpressPayment();
        payment.setOfflinePayment(money);
        payment.setType(PaymentEnum.OFFLINE.getIndex());
        payment.setExpressId(expressId);
        payment.setCreateDate(new Date());

        paymentMapper.insert(payment);

        return payment;
    }

    @Override
    public ExpressPayment createAliPayment(String expressId, Double money, String sellerId) {
        ExpressPayment payment = new ExpressPayment();
        payment.setOnlinePayment(money);
        payment.setType(PaymentEnum.ALI_PAY.getIndex());
        payment.setStatus(PaymentStatusEnum.WAIT_BUYER_PAY.getIndex());
        payment.setOnlineSeller(sellerId);
        payment.setExpressId(expressId);
        payment.setCreateDate(new Date());

        paymentMapper.insert(payment);

        return payment;
    }

    /**
     * 校验订单
     * 支付宝同步/异步回调时调用
     * @author jitwxs
     * @since 2018/6/4 16:40
     */
    @Override
    public boolean validAlipay(Map<String,String> params) throws Exception {
        /* 实际验证过程建议商户务必添加以下校验：
        1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
        2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
        3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
        4、验证app_id是否为该商户本身。
        */

        // 1、调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, aliPaySetting.getAlipayPublicKey(), "utf-8", aliPaySetting.getSignType());
        if(!signVerified) {
            return false;
        }
        // 获取订单数据
        String orderId = params.get("out_trade_no");
        ExpressPayment payment = paymentMapper.selectById(orderId);
        if(payment == null) {
            return false;
        }
        // 2、判断金额是否相等
        double money = Double.parseDouble(params.get("total_amount"));
        if(money != payment.getOnlinePayment()) {
            return false;
        }

        // 3、判断商户ID是否相等
        String sellerId = params.get("seller_id");
        if(!sellerId.equals(payment.getOnlineSeller())) {
            return false;
        }

        // 4、判断APP_ID是否相等
        String appId = params.get("app_id");
        if(!appId.equals(aliPaySetting.getAppId())) {
            return false;
        }

        return true;
    }

    /*
     * 订单状态（与官方统一）
     * WAIT_BUYER_PAY：交易创建，等待买家付款；
     * TRADE_CLOSED：未付款交易超时关闭，或支付完成后全额退款；
     * TRADE_SUCCESS：交易支付成功；
     * TRADE_FINISHED：交易结束，不可退款
     */

    @Override
    public boolean updateStatus(String orderId, int status, String... tradeNo) {
        // 判断参数是否合法
        ExpressPayment payment = paymentMapper.selectById(orderId);
        if(payment == null) {
            return false;
        }

        // 如果订单状态相同，不发生改变
        if(status == payment.getStatus()) {
            return true;
        }

        // 如果是TRADE_SUCCESS，设置第三方订单号
        if(PaymentStatusEnum.TRADE_SUCCESS.equals(status) && tradeNo.length > 0) {
            payment.setOnlinePaymentNum(tradeNo[0]);
        }

        payment.setStatus(status);
        paymentMapper.updateById(payment);
        return true;
    }
}
