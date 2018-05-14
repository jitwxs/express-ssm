package jit.wxs.express.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import jit.wxs.express.dto.ExpressDto;
import jit.wxs.express.enums.ExpressStatusEnum;
import jit.wxs.express.enums.PaymentEnum;
import jit.wxs.express.pojo.Express;
import jit.wxs.express.pojo.ExpressPayment;
import jit.wxs.express.pojo.SysUser;
import jit.wxs.express.pojo.interactive.ExpressSelectWrapper;
import jit.wxs.express.pojo.interactive.Msg;
import jit.wxs.express.service.ExpressPaymentService;
import jit.wxs.express.service.ExpressService;
import jit.wxs.express.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 管理员订单Controller
 * @author jitwxs
 * @date 2018/5/2 0:21
 */
@RestController
@RequestMapping("/admin/express")
public class ExpressController {
    @Autowired
    private ExpressService expressService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private ExpressPaymentService expressPaymentService;

    private List<ExpressDto> express2dto(List<Express> expressList) {
        List<ExpressDto> result = new ArrayList<>();
        for(Express express : expressList) {
            result.add(express2dto(express));
        }
        return result;
    }

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

    /**
     * 构造筛选条件
     * @author jitwxs
     * @since 2018/5/13 15:50
     */
    private EntityWrapper<Express> getExpressWrapper(ExpressSelectWrapper esw) {
        EntityWrapper<Express> entityWrapper = new EntityWrapper<>();
        // 前台传递，状态-1表示所有状态
        if(esw.getStatus() != null && esw.getStatus() != -1) {
            entityWrapper.eq("status", esw.getStatus());
        }
        if(StringUtils.isNotBlank(esw.getTel())){
            entityWrapper.eq("tel", esw.getTel());
        }
        if(StringUtils.isNotBlank(esw.getName())){
            entityWrapper.like("name", esw.getName());
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

    /**
     * 获取订单的状态列表
     * @author jitwxs
     * @since 2018/5/2 9:58
     */
    @GetMapping("/status")
    public Msg listExpressStatus() {
        List<Map<String,Object>> result = new ArrayList<>();
        for(ExpressStatusEnum enums :ExpressStatusEnum.values()) {
            Map<String,Object> map = new HashMap<>();
            map.put("id",enums.getIndex());
            map.put("name",enums.getName());
            result.add(map);
        }
        return Msg.ok(null,result);
    }

    /**
     * 订单列表
     * @param esw 筛选条件
     * @author jitwxs
     * @since 2018/5/2 0:33
     */
    @GetMapping("/list")
    public Map listExpress(Integer rows, Integer page, ExpressSelectWrapper esw) {
        // Get请求中文编码
        try {
            String name = esw.getName();
            if(StringUtils.isNotBlank(name)) {
                esw.setName(new String(name.getBytes("iso8859-1"),"utf-8"));
            }
            String address = esw.getAddress();
            if(StringUtils.isNotBlank(address)) {
                esw.setAddress(new String(address.getBytes("iso8859-1"),"utf-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 得到筛选条件
        EntityWrapper<Express> expressWrapper = getExpressWrapper(esw);
        Page<Express> selectPage = expressService.selectPage(new Page<>(page, rows), expressWrapper);

        List<ExpressDto> list = express2dto(selectPage.getRecords());

        Map<String,Object> map = new HashMap<>();
        map.put("total", selectPage.getTotal());
        map.put("rows", list);
        return map;
    }

    /**
     * 获取单个订单详情
     * @author jitwxs
     * @since 2018/5/2 14:04
     */
    @GetMapping("/{id}")
    public Msg getById(@PathVariable String id) {
        Express express = expressService.selectById(id);
        ExpressDto expressDto = express2dto(express);

        return Msg.ok(null,expressDto);
    }

    /**
     * 分配订单
     * @param ids 订单数组
     * @param staffId 派送员id
     * @author jitwxs
     * @since 2018/5/2 16:37
     */
    @PostMapping("/assign")
    public Msg assignExpress(String[] ids,String staffId) {
        for(String id : ids) {
            Express express = expressService.selectById(id);
            // 只有订单状态为WAIT_DIST时才要分配订单
            if(ExpressStatusEnum.WAIT_DIST.getName().equals(ExpressStatusEnum.getName(express.getStatus()))) {
                express.setStaff(staffId);
                express.setStatus(ExpressStatusEnum.TRANSPORT.getIndex());
                expressService.updateById(express);
            }
        }
        return Msg.ok();
    }

    /**
     * 确认订单
     * @author jitwxs
     * @since 2018/5/13 17:51
     */
    @PostMapping("/confirm")
    public Msg confirmExpress(String[] ids) {
        for(String id : ids) {
            Express express = expressService.selectById(id);
            // 只有订单状态为TRANSPORT时才要确认
            if(ExpressStatusEnum.TRANSPORT.getName().equals(ExpressStatusEnum.getName(express.getStatus()))) {
                express.setStatus(ExpressStatusEnum.COMPLTE.getIndex());
                expressService.updateById(express);
            }
        }
        return Msg.ok();
    }

    /**
     * 异常订单
     * @author jitwxs
     * @since 2018/5/13 17:51
     */
    @PostMapping("/error")
    public Msg errorExpress(String[] ids, String text) {
        for(String id : ids) {
            Express express = expressService.selectById(id);
            // 只有订单状态为TRANSPORT时才要确认
            if(ExpressStatusEnum.TRANSPORT.getName().equals(ExpressStatusEnum.getName(express.getStatus()))) {
                express.setStatus(ExpressStatusEnum.ERROR.getIndex());
                express.setStaffRemark(text);
                expressService.updateById(express);
            }
        }
        return Msg.ok();
    }

    /**
     * 删除订单
     * @author jitwxs
     * @since 2018/5/2 14:05
     */
    @PostMapping("/delete")
    public Msg deleteById(String[] ids) {
        for(String id : ids) {
            Express express = expressService.selectById(id);
            if(express != null) {
                // 设置删除标记为true
                express.setHasDelete(true);
                expressService.updateById(express);
            }
        }
        return Msg.ok();
    }

    /**
     * 恢复订单
     * @author jitwxs
     * @since 2018/5/2 14:05
     */
    @PostMapping("/recycle")
    public Msg recycleById(String[] ids) {
        for(String id : ids) {
            Express express = expressService.selectById(id);
            if(express != null) {
                // 设置删除标记为false
                express.setHasDelete(false);
                expressService.updateById(express);
            }
        }
        return Msg.ok();
    }

    /**
     * 彻底删除订单
     * @author jitwxs
     * @since 2018/5/2 14:05
     */
    @PostMapping("/clean")
    public Msg cleanById(String[] ids) {
        for(String id : ids) {
            expressService.deleteById(id);
        }
        return Msg.ok();
    }
}
