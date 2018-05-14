package jit.wxs.express.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import jit.wxs.express.dto.ExpressDto;
import jit.wxs.express.dto.FeedbackDto;
import jit.wxs.express.dto.SysUserDto;
import jit.wxs.express.enums.*;
import jit.wxs.express.interactive.ExpressSelectWrapper;
import jit.wxs.express.interactive.FeedbackSelectWrapper;
import jit.wxs.express.interactive.SysUserSelectWrapper;
import jit.wxs.express.pojo.Express;
import jit.wxs.express.pojo.ExpressPayment;
import jit.wxs.express.pojo.Feedback;
import jit.wxs.express.pojo.SysUser;
import jit.wxs.express.service.ExpressPaymentService;
import jit.wxs.express.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 全局方法
 * @author jitwxs
 * @since 2018/5/14 18:05
 */
@Component
public class GlobalFunction {
    @Autowired
    private SysUserService userService;
    @Autowired
    private ExpressPaymentService expressPaymentService;

    public List<ExpressDto> express2dto(List<Express> expressList) {
        List<ExpressDto> result = new ArrayList<>();
        for(Express express : expressList) {
            result.add(express2dto(express));
        }
        return result;
    }

    public ExpressDto express2dto(Express express) {
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

    /**
     * 构造筛选条件
     * @author jitwxs
     * @since 2018/5/13 15:50
     */
    public EntityWrapper<Express> getExpressWrapper(ExpressSelectWrapper esw) {
        EntityWrapper<Express> entityWrapper = new EntityWrapper<>();
        // 前台传递，状态-1表示所有状态
        if(esw.getStatus() != null && esw.getStatus() != -1) {
            entityWrapper.eq("status", esw.getStatus());
        }
        if(StringUtils.isNotBlank(esw.getId())) {
            entityWrapper.eq("id", esw.getId());
        }
        if(StringUtils.isNotBlank(esw.getTel())){
            entityWrapper.eq("tel", esw.getTel());
        }
        if(StringUtils.isNotBlank(esw.getName())){
            entityWrapper.like("name", esw.getName());
        }
        if(StringUtils.isNotBlank(esw.getStaffName())){
            SysUser user = userService.getByUserName(esw.getStaffName());
            entityWrapper.eq("staff", user.getId());
        }
        if(StringUtils.isNotBlank(esw.getAddress())){
            entityWrapper.like("address", esw.getAddress());
        }
        if(esw.getHasDelete() != null) {
            entityWrapper.eq("has_delete", esw.getHasDelete());
        }
        if(esw.getIncludeDay() != null && esw.getIncludeDay() != -1) {
            Date nowDate = new Date();
            Date startDate = DateUtils.addDays(nowDate,-1 * esw.getIncludeDay());
            entityWrapper.between("create_date", startDate, nowDate);
        }

        if(esw.getStartDate() != null && esw.getEndDate() != null) {
            entityWrapper.between("create_date", esw.getStartDate(), esw.getEndDate());
        }

        return entityWrapper;
    }

    public List<FeedbackDto> feedback2dto(List<Feedback> list) {
        List<FeedbackDto> result = new ArrayList<>();
        for(Feedback feedback : list) {
            FeedbackDto dto = feedback2dto(feedback);
            result.add(dto);
        }
        return result;
    }

    public FeedbackDto feedback2dto(Feedback feedback) {
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
     * 构造筛选条件
     * @author jitwxs
     * @since 2018/5/13 15:50
     */
    public EntityWrapper<Feedback> getFeedbackWrapper(FeedbackSelectWrapper fsw) {
        EntityWrapper<Feedback> entityWrapper = new EntityWrapper<>();
        // 前台传递，-1表示所有状态
        if(fsw.getStatus() != null && fsw.getStatus() != -1) {
            entityWrapper.eq("status", fsw.getStatus());
        }
        // 前台传递，-1表示所有类型
        if(fsw.getType() != null && fsw.getType() != -1) {
            entityWrapper.eq("type", fsw.getType());
        }
        if(StringUtils.isNotBlank(fsw.getId())){
            entityWrapper.eq("id", fsw.getId());
        }
        if(StringUtils.isNotBlank(fsw.getTel())){
            entityWrapper.eq("tel", fsw.getTel());
        }
        if(StringUtils.isNotBlank(fsw.getName())){
            entityWrapper.like("name", fsw.getName());
        }
        if(StringUtils.isNotBlank(fsw.getStaffName())){
            SysUser user = userService.getByUserName(fsw.getStaffName());
            entityWrapper.eq("staff_id", user.getId());
        }
        return entityWrapper;
    }

    public List<SysUserDto> sysUser2dto(List<SysUser> userList) {
        List<SysUserDto> result = new ArrayList<>();
        for(SysUser user : userList) {
            result.add(sysUser2dto(user));
        }
        return result;
    }

    public SysUserDto sysUser2dto(SysUser user) {
        SysUserDto dto = new SysUserDto();
        BeanUtils.copyProperties(user,dto);

        // 设置角色名称
        String roleName = RoleEnum.getName(dto.getRoleId());
        dto.setRoleName(roleName);

        // 设置用户状态名称
        String statusName = SysUserStatusEnum.getName(dto.getStatus());
        dto.setStatusName(statusName);

        return dto;
    }

    /**
     * 构造筛选条件
     * @author jitwxs
     * @since 2018/5/13 15:50
     */
    public EntityWrapper<SysUser> getSysUserWrapper(SysUserSelectWrapper usw) {
        EntityWrapper<SysUser> entityWrapper = new EntityWrapper<>();

        // 前台传递，状态-1表示所有状态
        if(usw.getStatus() != null && usw.getStatus() != -1) {
            entityWrapper.eq("status", usw.getStatus());
        }
        if(StringUtils.isNotBlank(usw.getTel())){
            entityWrapper.eq("tel", usw.getTel());
        }
        if(StringUtils.isNotBlank(usw.getSex())) {
            entityWrapper.eq("sex", usw.getSex());
        }
        if(StringUtils.isNotBlank(usw.getName())){
            entityWrapper.like("username", usw.getName());
        }
        if(StringUtils.isNotBlank(usw.getAddress())){
            entityWrapper.like("address", usw.getAddress());
        }
        return entityWrapper;
    }

    /**
     * iso8859-1 转 utf-8
     * @author jitwxs
     * @since 2018/5/14 18:09
     */
    public String iso8859ToUtf8(String x) throws UnsupportedEncodingException {
        if(StringUtils.isNotBlank(x)) {
            return new String(x.getBytes("iso8859-1"),"utf-8");
        } else {
            return null;
        }
    }

    /**
     * 获取当前用户
     * @author jitwxs
     * @since 2018/5/14 18:47
     */
    public SysUser getUser() {
        String username = (String)SecurityUtils.getSubject().getPrincipal();
        return userService.getByUserName(username);
    }

    /**
     * 获取当前用户id
     * @author jitwxs
     * @since 2018/5/14 18:47
     */
    public String getUserId() {
        return getUser().getId();
    }
}
