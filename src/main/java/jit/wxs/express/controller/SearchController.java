package jit.wxs.express.controller;

import jit.wxs.express.dto.ExpressDto;
import jit.wxs.express.dto.FeedbackDto;
import jit.wxs.express.enums.ExpressStatusEnum;
import jit.wxs.express.enums.FeedbackTypeEnum;
import jit.wxs.express.enums.PaymentEnum;
import jit.wxs.express.pojo.Express;
import jit.wxs.express.pojo.ExpressPayment;
import jit.wxs.express.pojo.Feedback;
import jit.wxs.express.pojo.SysUser;
import jit.wxs.express.pojo.interactive.Msg;
import jit.wxs.express.service.ExpressPaymentService;
import jit.wxs.express.service.ExpressService;
import jit.wxs.express.service.FeedbackService;
import jit.wxs.express.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 查询Controller
 * @author jitwxs
 * @since 2018/5/14 12:44
 */
@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private ExpressService expressService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private ExpressPaymentService expressPaymentService;
    @Autowired
    private FeedbackService feedbackService;

    private ExpressDto express2dto(Express express) {
        ExpressDto dto = new ExpressDto();
        BeanUtils.copyProperties(express,dto);

        // 设置配送人员信息
        if(StringUtils.isNotBlank(dto.getStaff())) {
            SysUser user = userService.selectById(dto.getStaff());
            if(user.getUsername() != null) {
                dto.setStaffName(user.getUsername());
            }
            if(user.getTel() != null) {
                dto.setStaffTel(user.getTel());
            }
        }

        // 设置订单状态
        dto.setStatusName(ExpressStatusEnum.getName(express.getStatus()));

        // 设置订单支付信息
        ExpressPayment payment = expressPaymentService.selectById(express.getId());
        if(payment != null) {
            dto.setPaymentType(PaymentEnum.getName(payment.getType()));
            if(payment.getOnlinePayment() != null) {
                dto.setOnlinePayment(payment.getOnlinePayment());
            }

            if(payment.getOfflinePayment() != null) {
                dto.setOfflinePayment(payment.getOfflinePayment());
            }
        }
        return dto;
    }

    private FeedbackDto feedback2dto(Feedback feedback) {
        FeedbackDto dto = new FeedbackDto();
        BeanUtils.copyProperties(feedback, dto);
        // 设置反馈类型名
        dto.setTypeName(FeedbackTypeEnum.getName(feedback.getType()));
        // 设置处理人名
        if(StringUtils.isNotBlank(feedback.getStaffId())) {
            SysUser user = userService.selectById(feedback.getStaffId());
            dto.setStaffName(user.getUsername());
        }
        String statusName="";
        switch (feedback.getStatus()) {
            case 0:
                statusName = "未处理";
                break;
            case 1:
                statusName = "已处理";
                break;
            default:
                break;
        }
        dto.setStatusName(statusName);
        return dto;
    }

    /**
     * 订单查询
     * @author jitwxs
     * @since 2018/5/14 12:49
     */
    @PostMapping("")
    public Msg search(String id) {
        // 订单号为纯数字,反馈号为数字+字母
        if(StringUtils.isNumeric(id)) {
            Express express = expressService.selectById(id);
            if(express == null) {
                return Msg.error("没有查到相关信息");
            } else {
                return Msg.ok("0",express2dto(express));
            }
        } else {
            Feedback feedback = feedbackService.selectById(id);
            if(feedback == null) {
                return Msg.error("没有查到相关信息");
            } else {
                return Msg.ok("1",feedback2dto(feedback));
            }
        }
    }
}
