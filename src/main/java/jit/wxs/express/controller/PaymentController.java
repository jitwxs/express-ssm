package jit.wxs.express.controller;

import jit.wxs.express.enums.ExpressStatusEnum;
import jit.wxs.express.enums.PaymentEnum;
import jit.wxs.express.pojo.Express;
import jit.wxs.express.pojo.ExpressPayment;
import jit.wxs.express.interactive.Msg;
import jit.wxs.express.service.ExpressPaymentService;
import jit.wxs.express.service.ExpressService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 支付Controller
 * @author jitwxs
 * @since 2018/5/13 22:51
 */
@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Value("${session.latest_express}")
    private String SESSION_LATEST_EXPRESS;
    @Value("${session.latest_payment}")
    private String SESSION_LATEST_PAYMENT;

    @Autowired
    private ExpressService expressService;
    @Autowired
    private ExpressPaymentService expressPaymentService;

    /**
     * 从客户Cookie中取出最新的一条订单信息
     * @author jitwxs
     * @since 2018/5/13 23:12
     */
    @GetMapping("/express")
    public Msg getExpress(HttpSession session) {
        Express express = (Express)session.getAttribute(SESSION_LATEST_EXPRESS);
        if(express != null) {
            return Msg.ok(null,express);
        } else {
            return Msg.error("订单好像丢失了，请重新下单...");
        }
    }

    /**
     * 订单支付
     * @param time 用于验证用户当前操作订单和Session中订单是否一致
     * @param type 支付类型
     * @param money 支付金额
     * @param num 订单号
     * @author jitwxs
     * @since 2018/5/14 8:53
     */
    @PostMapping("")
    public Msg payment(String time, Integer type, Double money, String num, HttpSession session) {
        Express express = (Express)session.getAttribute(SESSION_LATEST_EXPRESS);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = sdf.format(express.getCreateDate());
        System.out.println(time);
        System.out.println(createTime);

        if(!createTime.equals(time) || type == null) {
            return Msg.error("数据异常，请重新下单或提交反馈");
        }

        // 生成订单
        express.setStatus(ExpressStatusEnum.WAIT_DIST.getIndex());
        expressService.insert(express);
        // 生成支付信息
        ExpressPayment payment = new ExpressPayment();
        payment.setExpressId(express.getId());
        payment.setCreateDate(new Date());

        // 线下支付
        if(type == PaymentEnum.OFFLINE.getIndex()) {
            payment.setType(type);
            payment.setOfflinePayment(money);

        }
        // 线上支付
        if(type == PaymentEnum.ALI_PAY.getIndex() || type == PaymentEnum.WECHAT.getIndex()) {
            if(StringUtils.isNotBlank(num)) {
                payment.setType(type);
                payment.setOnlinePayment(money);
                payment.setOnlinePaymentNum(num);
            } else {
                return Msg.error("流水号参数错误");
            }
        }
        expressPaymentService.insert(payment);

        // 将支付信息写入session
        session.setAttribute(SESSION_LATEST_PAYMENT, payment);
        // 支付成功后删除Express的session
        session.removeAttribute(SESSION_LATEST_EXPRESS);

        return Msg.ok(null,"/payment/result");
    }

    /**
     * 关闭订单
     * @author jitwxs
     * @since 2018/5/14 10:57
     */
    @GetMapping("/close")
    public Msg closeExpress(HttpSession session) {
        session.removeAttribute(SESSION_LATEST_EXPRESS);
        // 清楚后跳转到首页
        return Msg.ok(null,"/");
    }

    /**
     * 支付结果页，获取支付信息
     * @author jitwxs
     * @since 2018/5/14 9:19
     */
    @GetMapping("/result/info")
    public Msg getPaymentResult(HttpSession session) {
        ExpressPayment payment = (ExpressPayment)session.getAttribute(SESSION_LATEST_PAYMENT);
        if(payment == null) {
            return Msg.error(null);
        } else {
            return Msg.ok(null,payment);
        }
    }
}
