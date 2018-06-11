package jit.wxs.express.controller;

import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import jit.wxs.express.alipay.AliPaySetting;
import jit.wxs.express.enums.PaymentStatusEnum;
import jit.wxs.express.interactive.Msg;
import jit.wxs.express.pojo.Express;
import jit.wxs.express.pojo.ExpressPayment;
import jit.wxs.express.service.ExpressPaymentService;
import jit.wxs.express.service.ExpressService;
import jit.wxs.express.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 支付Controller
 * @author jitwxs
 * @since 2018/5/13 22:51
 */
@Controller
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
    @Autowired
    private AliPaySetting aliPaySetting;
    @Autowired
    private AlipayClient alipayClient;


    /**
     * 从客户Cookie中取出最新的一条订单信息
     * @author jitwxs
     * @since 2018/5/13 23:12
     */
    @GetMapping("/express")
    @ResponseBody
    public Msg getExpress(HttpSession session) {
        Express express = (Express)session.getAttribute(SESSION_LATEST_EXPRESS);
        if(express != null) {
            return Msg.ok(null,express);
        } else {
            return Msg.error("订单好像丢失了，请重新下单...");
        }
    }

    /**
     * 线下支付方式
     * @param money 支付金额
     * @author jitwxs
     * @since 2018/5/14 8:53
     */
    @PostMapping("/offline")
    @ResponseBody
    public Msg paymentOffline(Double money, HttpSession session) {
        Express express = (Express)session.getAttribute(SESSION_LATEST_EXPRESS);

        if(express == null || money == null) {
            return Msg.error("参数错误");
        }

        // 创建订单
        String expressId = expressService.createExpress(express);
        // 创建订单支付信息
        ExpressPayment payment = expressPaymentService.createOfflinePayment(expressId, money);

        // 将支付信息写入session
        session.setAttribute(SESSION_LATEST_PAYMENT, payment);
        // 支付成功后删除Express的session
        session.removeAttribute(SESSION_LATEST_EXPRESS);

        return Msg.ok(null,"/payment/result");
    }

    /**
     * 支付宝支付方式
     * @param money 支付金额
     * @author jitwxs
     * @since 2018/5/14 8:53
     */
    @PostMapping("/alipay")
    public void paymentAlipay(Double money, HttpSession session, HttpServletResponse response) throws IOException {
        Express express = (Express)session.getAttribute(SESSION_LATEST_EXPRESS);

        if(express == null || money == null) {
            response.getWriter().write("参数错误，请重新下单");
        }

        // 金额保留两位
        money = (double) (Math.round(money * 100)) / 100;

        // 生成订单
        String expressId = expressService.createExpress(express);
        // 生成订单支付
        expressPaymentService.createAliPayment(expressId, money, aliPaySetting.getSellerId());

        // 1、设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        // 页面跳转同步通知页面路径
        alipayRequest.setReturnUrl(aliPaySetting.getReturnUrl());
        // 服务器异步通知页面路径
        alipayRequest.setNotifyUrl(aliPaySetting.getNotifyUrl());

        // 2、SDK已经封装掉了公共参数，这里只需要传入业务参数，请求参数查阅开头Wiki
        Map<String,String> map = new HashMap<>(16);
        map.put("out_trade_no", expressId);
        map.put("total_amount", String.valueOf(money));
        map.put("subject", "快递代拿");
        map.put("body", "快递代拿在线支付");
        // 销售产品码
        map.put("product_code","FAST_INSTANT_TRADE_PAY");

        alipayRequest.setBizContent(JsonUtils.objectToJson(map));

        response.setContentType("text/html;charset=utf-8");
        try{
            // 3、生成支付表单
            AlipayTradePagePayResponse alipayResponse = alipayClient.pageExecute(alipayRequest);
            if(alipayResponse.isSuccess()) {
                String result = alipayResponse.getBody();
                response.getWriter().write(result);
            } else {
                response.getWriter().write("error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/alipay/return")
    public String alipayReturn(HttpServletRequest request, HttpServletResponse response) {
        // 获取参数
        Map<String,String> params = getPayParams(request);
        try {
            // 验证订单
            boolean flag = expressPaymentService.validAlipay(params);
            if(flag) {
                // 验证成功后，修改订单状态为已支付
                String orderId = params.get("out_trade_no");
                /*
                 * 订单状态（与官方统一）
                 * WAIT_BUYER_PAY：交易创建，等待买家付款；
                 * TRADE_CLOSED：未付款交易超时关闭，或支付完成后全额退款；
                 * TRADE_SUCCESS：交易支付成功；
                 * TRADE_FINISHED：交易结束，不可退款
                 */
                // 获取支付宝订单号
                String tradeNo = params.get("trade_no");
                // 更新状态
                expressPaymentService.updateStatus(orderId, PaymentStatusEnum.TRADE_SUCCESS.getIndex(), tradeNo);

                HttpSession session = request.getSession();
                // 将支付信息写入session
                session.setAttribute(SESSION_LATEST_PAYMENT, expressPaymentService.selectById(orderId));
                // 支付成功后删除Express的session
                session.removeAttribute(SESSION_LATEST_EXPRESS);
            } else {
                throw new Exception("支付宝支付验签失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "payment_result";
    }

    /**
     * 服务器异步通知，获取支付宝POST过来反馈信息
     * 该方法无返回值，静默处理
     * 订单的状态已该方法为主，其他的状态修改方法为辅 *
     * （1）程序执行完后必须打印输出“success”（不包含引号）。
     * 如果商户反馈给支付宝的字符不是success这7个字符，支付宝服务器会不断重发通知，直到超过24小时22分钟。
     * （2）程序执行完成后，该页面不能执行页面跳转。
     * 如果执行页面跳转，支付宝会收不到success字符，会被支付宝服务器判定为该页面程序运行出现异常，而重发处理结果通知
     * （3）cookies、session等在此页面会失效，即无法获取这些数据
     * （4）该方式的调试与运行必须在服务器上，即互联网上能访问 *
     * @author jitwxs
     * @since 2018/6/4 14:45
     */
    @PostMapping("/alipay/notify")
    public void alipayNotify(HttpServletRequest request,  HttpServletResponse response){
        /*
         默认只有TRADE_SUCCESS会触发通知，如果需要开通其他通知，请联系客服申请
         触发条件名 	    触发条件描述 	触发条件默认值
        TRADE_FINISHED 	交易完成 	false（不触发通知）
        TRADE_SUCCESS 	支付成功 	true（触发通知）
        WAIT_BUYER_PAY 	交易创建 	false（不触发通知）
        TRADE_CLOSED 	交易关闭 	false（不触发通知）
        来源：https://docs.open.alipay.com/270/105902/#s2
         */
        // 获取参数
        Map<String,String> params = getPayParams(request);
        try{
            // 验证订单
            boolean flag = expressPaymentService.validAlipay(params);
            if(flag) {
                //商户订单号
                String orderId = params.get("out_trade_no");
                //支付宝交易号
                String tradeNo = params.get("trade_no");
                //交易状态
                String tradeStatus = params.get("trade_status");

                switch (tradeStatus) {
                    case "WAIT_BUYER_PAY":
                        expressPaymentService.updateStatus(orderId, PaymentStatusEnum.WAIT_BUYER_PAY.getIndex());
                        break;
                    /*
                     * 关闭订单
                     * （1)订单已创建，但用户未付款，调用关闭交易接口
                     * （2）付款成功后，订单金额已全部退款【如果没有全部退完，仍是TRADE_SUCCESS状态】
                     */
                    case "TRADE_CLOSED":
                        expressPaymentService.updateStatus(orderId, PaymentStatusEnum.TRADE_CLOSED.getIndex());
                        break;
                    /*
                     * 订单完成
                     * （1）退款日期超过可退款期限后
                     */
                    case "TRADE_FINISHED" :
                        expressPaymentService.updateStatus(orderId, PaymentStatusEnum.TRADE_FINISHED.getIndex());
                        break;
                    /*
                     * 订单Success
                     * （1）用户付款成功
                     */
                    case "TRADE_SUCCESS" :
                        expressPaymentService.updateStatus(orderId, PaymentStatusEnum.TRADE_SUCCESS.getIndex(), tradeNo);
                        break;
                    default:break;
                }
                response.getWriter().write("success");
            }else {
                response.getWriter().write("fail");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 支付结果页，获取支付信息
     * @author jitwxs
     * @since 2018/5/14 9:19
     */
    @GetMapping("/result/info")
    @ResponseBody
    public Msg getPaymentResult(HttpSession session) {
        ExpressPayment payment = (ExpressPayment)session.getAttribute(SESSION_LATEST_PAYMENT);
        if(payment == null) {
            return Msg.error(null);
        } else {
            return Msg.ok(null,payment);
        }
    }

    /**
     * 获取支付参数
     * @author jitwxs
     * @since 2018/6/4 16:39
     */
    private Map<String,String> getPayParams(HttpServletRequest request) {
        Map<String,String> params = new HashMap<>(16);
        Map<String,String[]> requestParams = request.getParameterMap();

        Iterator<String> iter = requestParams.keySet().iterator();
        while (iter.hasNext()) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
//            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        return params;
    }
}
